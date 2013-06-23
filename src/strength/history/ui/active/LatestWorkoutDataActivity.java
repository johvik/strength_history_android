package strength.history.ui.active;

import java.util.Collection;
import java.util.Comparator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.ui.custom.CustomTitleFragmentActivity;

public class LatestWorkoutDataActivity extends CustomTitleFragmentActivity
		implements ExerciseProvider.Events,
		WorkoutDataProvider.Events.LatestExerciseData,
		WorkoutDataSummaryAdapter.Master {
	public static final String WORKOUT = "wo";
	private WorkoutData workoutData = null;
	private Workout workout = null;
	private WorkoutDataSummaryAdapter workoutDataSummaryAdapter;
	private SortedList<Exercise> exercises = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					return lhs.compareTo(rhs);
				}
			}, true);
	private boolean exercisesLoaded = false;
	private int latestExercisesTotal = 0;
	private int latestExercisesLoaded = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView listViewLatestWorkoutData = (ListView) findViewById(R.id.listViewLatestWorkoutData);
		listViewLatestWorkoutData
				.setEmptyView(findViewById(R.id.textViewEmptyList));
		Intent i = getIntent();
		workout = i.getParcelableExtra(WORKOUT);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitle(getString(R.string.latest_workoutdata_title, workout.getName()));
		workoutDataSummaryAdapter = new WorkoutDataSummaryAdapter(this, this);
		listViewLatestWorkoutData.setAdapter(workoutDataSummaryAdapter);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_latest_workoutdata_list;
	}

	@Override
	protected void onResume() {
		super.onResume();
		setCustomProgressBarVisibility(true);
		DataProvider dataProvider = DataListener.add(this);
		Context c = getApplicationContext();
		workoutData = new WorkoutData(0, workout.getId());
		exercisesLoaded = false;
		latestExercisesLoaded = 0;
		latestExercisesTotal = workout.size();
		for (Long l : workout) {
			workoutData.add(new ExerciseData(l));
			dataProvider.latestExerciseData(l, c);
		}
		dataProvider.queryExercise(c);
	}

	@Override
	protected void onPause() {
		super.onPause();
		DataListener.remove(this);
	}

	private void update() {
		if (exercisesLoaded && latestExercisesLoaded >= latestExercisesTotal) {
			setCustomProgressBarVisibility(false);
			workoutDataSummaryAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		if (ok) {
			exercises.remove(e);
		}
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		if (ok) {
			exercises.add(e);
		}
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		if (ok) {
			exercises.remove(old);
			exercises.add(e);
		}
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		exercises.addAll(e);
		if (done) {
			exercisesLoaded = true;
			update();
		}
	}

	@Override
	public void latestCallback(ExerciseData e, long exerciseId, boolean ok) {
		latestExercisesLoaded++;
		if (ok) {
			for (int i = 0, j = workoutData.size(); i < j; i++) {
				if (workoutData.get(i).getExerciseId() == exerciseId) {
					workoutData.set(i, e);
				}
			}
		}
		update();
	}

	@Override
	public ExerciseData getItem(int position) {
		return workoutData == null ? null : workoutData.get(position);
	}

	@Override
	public int getSize() {
		return workoutData == null ? 0 : workoutData.size();
	}

	@Override
	public Pair<ExerciseData, Exercise> getPairItem(int position) {
		if (workoutData == null) {
			return Pair.create(null, null);
		}
		ExerciseData d = workoutData.get(position);
		int pos = exercises.indexOf(new Exercise(d.getExerciseId(), 0, "",
				MuscleGroup.DEFAULT));
		Exercise e = null;
		if (pos != -1) {
			e = exercises.get(pos);
		}
		return Pair.create(d, e);
	}
}
