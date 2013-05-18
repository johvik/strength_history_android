package strength.history;

import java.util.Comparator;

import strength.history.data.SortedList;
import strength.history.data.structure.Exercise;
import strength.history.ui.ExerciseAdapter;
import strength.history.ui.ExerciseEditFragment;
import strength.history.ui.ExerciseListFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ExercisesActivity extends FragmentActivity implements
		ExerciseListFragment.Creator, ExerciseEditFragment.Creator {
	private SortedList<Exercise> exerciseList;
	private ExerciseAdapter exerciseAdapter;
	private ExerciseEditFragment exerciseEditFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises);
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
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
	}

	@Override
	public ExerciseAdapter getExerciseAdapter() {
		return exerciseAdapter;
	}

	@Override
	public void saveCallback(Exercise e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void cancelCallback() {
		// TODO Auto-generated method stub
		exerciseEditFragment.setExercise(null);
	}
}
