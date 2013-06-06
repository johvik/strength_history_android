package strength.history.ui.exercise;

import java.util.Collection;
import java.util.Comparator;

import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ExercisesActivity extends CustomTitleFragmentActivity implements
		ExerciseListFragment.Listener, ExerciseEditFragment.Listener,
		ExerciseProvider.Events {
	private static final int REQUEST_CODE = 1;
	private static final String CUR_CHOICE = "curChoice";
	private static final String CUR_EXERCISE = "curExer";

	private SortedList<Exercise> exerciseList = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					int c = lhs.getName().compareTo(rhs.getName());
					if (c == 0) {
						c = lhs.compareTo(rhs);
					}
					return c;
				}
			}, true);
	private ExerciseAdapter exerciseAdapter = new ExerciseAdapter(this,
			exerciseList, false);
	private ExerciseEditFragment exerciseEditFragment;
	private TextView textSelectExerciseToEdit;
	private FrameLayout frameLayoutExerciseEditFragment;
	private ListView listViewExercises;
	private boolean mDualPane = false;
	private int mCurCheckPosition = -1;
	private Exercise mExercise = null;
	private DataProvider dataProvider = null;
	private AlertDialog alertDialogDeleteConfirm = null;
	private View viewMenuSave = null;
	private View viewMenuDelete = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
		mDualPane = findViewById(R.id.fragmentExerciseEdit) != null;
		if (mDualPane) {
			textSelectExerciseToEdit = (TextView) findViewById(R.id.textSelectExerciseToEdit);
			frameLayoutExerciseEditFragment = (FrameLayout) findViewById(R.id.frameLayoutExerciseEditFragment);
		}
		listViewExercises = (ListView) findViewById(R.id.listViewExercises);
		listViewExercises.setAdapter(exerciseAdapter);

		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt(CUR_CHOICE);
			mExercise = savedInstanceState.getParcelable(CUR_EXERCISE);
		}
		if (mDualPane) {
			listViewExercises.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		alertDialogDeleteConfirm = new AlertDialog.Builder(this)
				.setMessage(R.string.dialog_exercise_delete)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteCallback();
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();
		addMenuItem(createMenuItem(R.drawable.ic_action_plus,
				R.string.create_exercise, new OnClickListener() {
					@Override
					public void onClick(View v) {
						onExerciseCreateClick();
					}
				}));
		viewMenuSave = createMenuItem(R.drawable.ic_action_save,
				R.string.save_exercise, new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveCallback(exerciseEditFragment.getExercise());
					}
				});
		viewMenuDelete = createMenuItem(R.drawable.ic_action_delete,
				R.string.delete_exercise, new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialogDeleteConfirm.show();
					}
				});
		setCustomProgressBarVisibility(true);
		dataProvider = DataListener.add(this);
		// get exercises
		dataProvider.queryExercise(getApplicationContext());

		setTitle(R.string.exercises);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_exercises;
	}

	@Override
	protected void onPause() {
		super.onPause();
		alertDialogDeleteConfirm.dismiss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataListener.remove(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mDualPane) {
			listViewExercises.setItemChecked(mCurCheckPosition, true);
		}
		listViewExercises.setSelection(mCurCheckPosition);
		if (mExercise != null) {
			continueEditExercise();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CUR_CHOICE, mCurCheckPosition);
		outState.putParcelable(CUR_EXERCISE, mExercise);
	}

	private void continueEditExercise() {
		if (mExercise != null) {
			if (mDualPane) {
				textSelectExerciseToEdit.setVisibility(View.GONE);
				frameLayoutExerciseEditFragment.setVisibility(View.VISIBLE);
				exerciseEditFragment.setExercise(mExercise);
				removeMenuItem(viewMenuDelete);
				removeMenuItem(viewMenuSave);
				addMenuItem(viewMenuDelete, 0);
				addMenuItem(viewMenuSave, 0);
				setTitle(R.string.edit_exercise);
			} else {
				Intent intent = new Intent(this, ExerciseEditActivity.class);
				intent.putExtra(ExerciseEditActivity.EXERCISE, mExercise);
				startActivityForResult(intent, REQUEST_CODE);
			}
		}
	}

	private void starteEditExercise(Exercise e) {
		if (e != null) {
			mExercise = e.copy();
			continueEditExercise();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				saveCallback((Exercise) data
						.getParcelableExtra(ExerciseEditActivity.EXERCISE));
			} else if (resultCode == RESULT_CANCELED) {
				reset();
			} else if (resultCode == ExerciseEditActivity.RESULT_ORIENTATION) {
				mExercise = data
						.getParcelableExtra(ExerciseEditActivity.EXERCISE);
				continueEditExercise();
			} else if (resultCode == ExerciseEditActivity.RESULT_DELETE) {
				deleteCallback();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void reset() {
		if (mDualPane) {
			listViewExercises.setItemChecked(mCurCheckPosition, false);
			textSelectExerciseToEdit.setVisibility(View.VISIBLE);
			frameLayoutExerciseEditFragment.setVisibility(View.GONE);
			removeMenuItem(viewMenuDelete);
			removeMenuItem(viewMenuSave);
			setTitle(R.string.exercises);
		}
		mCurCheckPosition = -1;
		mExercise = null;
	}

	@Override
	public void saveCallback(Exercise e) {
		if (e != null) {
			if (mCurCheckPosition != -1) {
				exerciseList.remove(mCurCheckPosition);
			}
			if (e.getName().trim().length() == 0) {
				e.setName(getString(R.string.default_name));
			}
			exerciseList.add(e);
			exerciseAdapter.notifyDataSetChanged();
			long id = e.getId();
			if (id == -1) { // new
				dataProvider.insert(e, getApplicationContext());
			} else {
				dataProvider.update(e, getApplicationContext());
			}
		}
		reset();
	}

	@Override
	public void deleteCallback() {
		if (mCurCheckPosition != -1) {
			Exercise e = exerciseList.remove(mCurCheckPosition);
			exerciseAdapter.notifyDataSetChanged();
			dataProvider.delete(e, getApplicationContext());
		}
		reset();
	}

	@Override
	public void onExerciseItemClick(int position) {
		mCurCheckPosition = position;
		starteEditExercise(exerciseList.get(position));
	}

	@Override
	public void onExerciseCreateClick() {
		listViewExercises.setItemChecked(mCurCheckPosition, false);
		mCurCheckPosition = -1;
		// TODO Default muscle group
		starteEditExercise(new Exercise("", MuscleGroup.ARMS));
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		if (!ok) {
			exerciseList.add(e);
			exerciseAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		if (!ok) {
			exerciseList.remove(e);
			exerciseAdapter.notifyDataSetChanged();
		} else {
			// update the id...
			long id = e.getId();
			e.setId(-1);
			exerciseList.remove(e);
			e.setId(id);
			exerciseList.add(e);
		}
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		exerciseList.addAll(e);
		exerciseAdapter.notifyDataSetChanged();
		if (done) {
			setCustomProgressBarVisibility(false);
		}
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		if (!ok) {
			if (old != null) {
				exerciseList.remove(e);
				exerciseList.add(old);
				exerciseAdapter.notifyDataSetChanged();
			}
		}
	}
}
