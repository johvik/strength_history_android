package strength.history;

import java.util.Comparator;

import strength.history.data.SortedList;
import strength.history.data.structure.Exercise;
import strength.history.ui.ExerciseAdapter;
import strength.history.ui.ExerciseEditFragment;
import strength.history.ui.ExerciseListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ExercisesActivity extends FragmentActivity implements
		ExerciseListFragment.Listener, ExerciseEditFragment.Listener {
	private static final int REQUEST_CODE = 1;
	private static final String CUR_CHOICE = "curChoice";
	private static final String CUR_EXERCISE = "curExer";

	private SortedList<Exercise> exerciseList;
	private ExerciseAdapter exerciseAdapter;
	private ExerciseEditFragment exerciseEditFragment;
	private TextView textSelectExerciseToEdit;
	private FrameLayout frameLayoutExerciseEditFragment;
	private ListView listViewExercises;
	private boolean mDualPane = false;
	private int mCurCheckPosition = -1;
	private Exercise mExercise = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises);
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
		mDualPane = findViewById(R.id.fragmentExerciseEdit) != null;
		if (mDualPane) {
			textSelectExerciseToEdit = (TextView) findViewById(R.id.textSelectExerciseToEdit);
			frameLayoutExerciseEditFragment = (FrameLayout) findViewById(R.id.frameLayoutExerciseEditFragment);
		}
		exerciseList = new SortedList<Exercise>(new Comparator<Exercise>() {
			@Override
			public int compare(Exercise lhs, Exercise rhs) {
				int c = lhs.getName().compareTo(rhs.getName());
				if (c == 0) {
					c = lhs.compareTo(rhs);
				}
				return c;
			}
		});
		exerciseList.add(new Exercise("a"));
		exerciseList.add(new Exercise("aasd"));
		exerciseList.add(new Exercise("aqwe"));
		exerciseList.add(new Exercise("aqweqwe"));
		exerciseList.add(new Exercise("wewa"));
		exerciseAdapter = new ExerciseAdapter(this, exerciseList);
		listViewExercises = (ListView) findViewById(R.id.listViewExercises);
		listViewExercises.setAdapter(exerciseAdapter);

		if (savedInstanceState != null) {
			mCurCheckPosition = savedInstanceState.getInt(CUR_CHOICE);
			mExercise = savedInstanceState.getParcelable(CUR_EXERCISE);
		}
		if (mDualPane) {
			listViewExercises.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mDualPane) {
			listViewExercises.setItemChecked(mCurCheckPosition, true);
		}
		listViewExercises.setSelection(mCurCheckPosition);
		if (mCurCheckPosition != -1) {
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
				cancelCallback();
			} else if (resultCode == ExerciseEditActivity.RESULT_ORIENTATION) {
				mExercise = data
						.getParcelableExtra(ExerciseEditActivity.EXERCISE);
				continueEditExercise();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void saveCallback(Exercise e) {
		exerciseList.remove(mCurCheckPosition);
		exerciseList.add(e);
		exerciseAdapter.notifyDataSetChanged();
		// TODO Save for real
		if (mDualPane) {
			listViewExercises.setItemChecked(mCurCheckPosition, false);
			textSelectExerciseToEdit.setVisibility(View.VISIBLE);
			frameLayoutExerciseEditFragment.setVisibility(View.GONE);
		}
		mCurCheckPosition = -1;
		mExercise = null;
	}

	@Override
	public void cancelCallback() {
		if (mDualPane) {
			listViewExercises.setItemChecked(mCurCheckPosition, false);
			textSelectExerciseToEdit.setVisibility(View.VISIBLE);
			frameLayoutExerciseEditFragment.setVisibility(View.GONE);
		}
		mCurCheckPosition = -1;
		mExercise = null;
	}

	@Override
	public void onExerciseItemClick(int position) {
		mCurCheckPosition = position;
		starteEditExercise(exerciseList.get(position));
	}

	@Override
	public void onExerciseCreateClick() {
		starteEditExercise(new Exercise(""));
	}
}
