package strength.history.ui.workout;

import android.os.Bundle;
import android.util.Pair;
import strength.history.R;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.workout.active.ActiveExerciseEditFragment;

public class RunWorkoutActivity extends CustomTitleFragmentActivity {
	public static final String WORKOUT = "rwork";
	public static final String TIME = "rtime";
	private Workout workout = null;
	private long time = -1;
	private WorkoutData workoutData = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workout = getIntent().getParcelableExtra(WORKOUT);
		time = getIntent().getLongExtra(TIME, -1);
		if (workout != null && time != -1) {
			setTitle(workout.getName());
			workoutData = new WorkoutData(time, workout.getId());
			for (Long l : workout) {
				workoutData.add(new ExerciseData(l));
			}
			int index = 0;
			if (index < workoutData.size()) {
				// TODO
				ActiveExerciseEditFragment activeExerciseEditFragment = (ActiveExerciseEditFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragmentActiveExerciseEdit);
				activeExerciseEditFragment.setExerciseData(Pair.create(
						workoutData.get(index), new SetData(50, 5)));
			}
		}
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_run_workout;
	}
}
