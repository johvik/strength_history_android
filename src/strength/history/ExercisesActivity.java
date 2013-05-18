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
import android.widget.ListView;

public class ExercisesActivity extends FragmentActivity implements
		ExerciseListFragment.Listener, ExerciseEditFragment.Creator {
	private static final int REQUEST_CODE = 1;

	private SortedList<Exercise> exerciseList;
	private ExerciseAdapter exerciseAdapter;
	private ExerciseEditFragment exerciseEditFragment;
	private ListView listViewExercises;
	private boolean mDualPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises);
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
		mDualPane = exerciseEditFragment != null;
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
		starteEditExercise(ExerciseEditFragment.editExercise);
	}

	private void starteEditExercise(Exercise e) {
		if (e != null) {
			e.backup();
			// TODO Cancel previous
			ExerciseEditFragment.editExercise = e;
			// listViewExercises.setItemChecked(position, true);
			if (mDualPane) {
				ExerciseEditFragment f = (ExerciseEditFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragmentExerciseEdit);
				f.update();
			} else {
				Intent intent = new Intent(this, ExerciseEditActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				saveCallback();
			} else if (resultCode == RESULT_CANCELED) {
				cancelCallback();
			} else if (resultCode == ExerciseEditActivity.RESULT_ORIENTATION) {
				starteEditExercise(ExerciseEditFragment.editExercise);
				// TODO Select?
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void saveCallback() {
		Exercise e = ExerciseEditFragment.editExercise;
		if (e != null) {
			if (exerciseList.remove(e)) {
				e.commit();
				exerciseList.add(e);
				exerciseAdapter.notifyDataSetChanged();
			}
			listViewExercises.clearChoices();
		}
		ExerciseEditFragment.editExercise = null;
		if (exerciseEditFragment != null) {
			exerciseEditFragment.update();
		}
	}

	@Override
	public void cancelCallback() {
		Exercise e = ExerciseEditFragment.editExercise;
		if (e != null) {
			e.revert();
		}
		listViewExercises.clearChoices();
		ExerciseEditFragment.editExercise = null;
		if (exerciseEditFragment != null) {
			exerciseEditFragment.update();
		}
	}

	@Override
	public void onExerciseItemClick(int position) {
		Exercise e = exerciseList.get(position);
		starteEditExercise(e);
	}

	@Override
	public void onExerciseCreateClick() {
		starteEditExercise(new Exercise(""));
	}
}
