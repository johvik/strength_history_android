package strength.history.ui.workout;

import java.util.Collection;
import java.util.Comparator;

import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.structure.Workout;
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

public class WorkoutsActivity extends CustomTitleFragmentActivity implements
		WorkoutListFragment.Listener, WorkoutEditFragment.Listener,
		WorkoutProvider.Events {
	private static final int REQUEST_CODE = 1;
	private static final String CUR_CHOICE = "curChoice";
	private static final String CUR_WORKOUT = "curWork";

	private SortedList<Workout> workoutList = new SortedList<Workout>(
			new Comparator<Workout>() {
				@Override
				public int compare(Workout lhs, Workout rhs) {
					int c = lhs.getName().compareTo(rhs.getName());
					if (c == 0) {
						c = lhs.compareTo(rhs);
					}
					return c;
				}
			}, true);
	private WorkoutAdapter workoutAdapter = new WorkoutAdapter(this,
			workoutList, false);
	private WorkoutEditFragment workoutEditFragment;
	private TextView textSelectWorkoutToEdit;
	private FrameLayout frameLayoutWorkoutEditFragment;
	private ListView listViewWorkouts;
	private boolean mDualPane = false;
	private int mCurCheckPosition = -1;
	private Workout mWorkout = null;
	private DataProvider dataProvider = null;
	private AlertDialog alertDialogDeleteConfirm = null;
	private View viewMenuSave = null;
	private View viewMenuDelete = null;
	private boolean fragmentLoaded = false;
	private boolean workoutsLoaded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataProvider = DataListener.add(this);
		workoutEditFragment = (WorkoutEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentWorkoutEdit);
		mDualPane = findViewById(R.id.fragmentWorkoutEdit) != null;
		if (mDualPane) {
			textSelectWorkoutToEdit = (TextView) findViewById(R.id.textSelectWorkoutToEdit);
			frameLayoutWorkoutEditFragment = (FrameLayout) findViewById(R.id.frameLayoutWorkoutEditFragment);
		}
		listViewWorkouts = (ListView) findViewById(R.id.listViewWorkouts);
		listViewWorkouts.setAdapter(workoutAdapter);

		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt(CUR_CHOICE);
			mWorkout = savedInstanceState.getParcelable(CUR_WORKOUT);
		}
		if (mDualPane) {
			listViewWorkouts.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
		alertDialogDeleteConfirm = new AlertDialog.Builder(this)
				.setMessage(R.string.dialog_workout_delete)
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
				R.string.create_workout, new OnClickListener() {
					@Override
					public void onClick(View v) {
						onWorkoutCreateClick();
					}
				}));
		viewMenuSave = createMenuItem(R.drawable.ic_action_save,
				R.string.save_workout, new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveCallback(workoutEditFragment.getWorkout());
					}
				});
		viewMenuDelete = createMenuItem(R.drawable.ic_action_delete,
				R.string.delete_workout, new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialogDeleteConfirm.show();
					}
				});
		updateProgressBar();
		// get workouts
		dataProvider.queryWorkout(getApplicationContext());

		setTitle(R.string.workouts);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_workouts;
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
			listViewWorkouts.setItemChecked(mCurCheckPosition, true);
		}
		listViewWorkouts.setSelection(mCurCheckPosition);
		if (mWorkout != null) {
			continueEditWorkout();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(CUR_CHOICE, mCurCheckPosition);
		outState.putParcelable(CUR_WORKOUT, mWorkout);
	}

	private void continueEditWorkout() {
		if (mWorkout != null) {
			if (mDualPane) {
				textSelectWorkoutToEdit.setVisibility(View.GONE);
				frameLayoutWorkoutEditFragment.setVisibility(View.VISIBLE);
				workoutEditFragment.setWorkout(mWorkout);
				removeMenuItem(viewMenuDelete);
				removeMenuItem(viewMenuSave);
				addMenuItem(viewMenuDelete, 0);
				addMenuItem(viewMenuSave, 0);
				setTitle(R.string.edit_workout);
			} else {
				Intent intent = new Intent(this, WorkoutEditActivity.class);
				intent.putExtra(WorkoutEditActivity.WORKOUT, mWorkout);
				startActivityForResult(intent, REQUEST_CODE);
			}
		}
	}

	private void starteEditWorkout(Workout e) {
		if (e != null) {
			mWorkout = e.copy();
			continueEditWorkout();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				saveCallback((Workout) data
						.getParcelableExtra(WorkoutEditActivity.WORKOUT));
			} else if (resultCode == RESULT_CANCELED) {
				reset();
			} else if (resultCode == WorkoutEditActivity.RESULT_ORIENTATION) {
				mWorkout = data.getParcelableExtra(WorkoutEditActivity.WORKOUT);
				continueEditWorkout();
			} else if (resultCode == WorkoutEditActivity.RESULT_DELETE) {
				deleteCallback();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void reset() {
		if (mDualPane) {
			listViewWorkouts.setItemChecked(mCurCheckPosition, false);
			textSelectWorkoutToEdit.setVisibility(View.VISIBLE);
			frameLayoutWorkoutEditFragment.setVisibility(View.GONE);
			removeMenuItem(viewMenuDelete);
			removeMenuItem(viewMenuSave);
			setTitle(R.string.workouts);
		}
		mCurCheckPosition = -1;
		mWorkout = null;
	}

	@Override
	public void saveCallback(Workout e) {
		if (e != null) {
			if (mCurCheckPosition != -1) {
				workoutList.remove(mCurCheckPosition);
			}
			if (e.getName().trim().length() == 0) {
				e.setName(getString(R.string.default_name));
			}
			workoutList.add(e);
			workoutAdapter.notifyDataSetChanged();
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
			Workout e = workoutList.remove(mCurCheckPosition);
			workoutAdapter.notifyDataSetChanged();
			dataProvider.delete(e, getApplicationContext());
		}
		reset();
	}

	@Override
	public void onWorkoutItemClick(int position) {
		mCurCheckPosition = position;
		starteEditWorkout(workoutList.get(position));
	}

	@Override
	public void onWorkoutCreateClick() {
		listViewWorkouts.setItemChecked(mCurCheckPosition, false);
		mCurCheckPosition = -1;
		starteEditWorkout(new Workout(""));
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		if (!ok) {
			workoutList.add(e);
			workoutAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		if (!ok) {
			workoutList.remove(e);
			workoutAdapter.notifyDataSetChanged();
		} else {
			// update the id...
			long id = e.getId();
			e.setId(-1);
			workoutList.remove(e);
			e.setId(id);
			workoutList.add(e);
		}
	}

	@Override
	public void workoutQueryCallback(Collection<Workout> e, boolean done) {
		workoutList.addAll(e);
		workoutAdapter.notifyDataSetChanged();
		if (done) {
			workoutsLoaded = true;
			updateProgressBar();
		}
	}

	@Override
	public void updateCallback(Workout old, Workout e, boolean ok) {
		if (!ok) {
			if (old != null) {
				workoutList.remove(e);
				workoutList.add(old);
				workoutAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void setLoaded(boolean loaded) {
		fragmentLoaded = loaded;
		updateProgressBar();
	}

	private void updateProgressBar() {
		if ((fragmentLoaded && workoutsLoaded)
				|| (!mDualPane && workoutsLoaded)) {
			setCustomProgressBarVisibility(false);
		} else {
			setCustomProgressBarVisibility(true);
		}
	}
}
