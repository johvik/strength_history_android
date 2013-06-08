package strength.history.ui.workout;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.workout.active.ActiveExerciseEditFragment;

public class RunWorkoutActivity extends CustomTitleFragmentActivity implements
		ExerciseProvider.Events, WorkoutDataProvider.Events.LatestExerciseData {
	public static final String WORKOUT = "rwork";
	public static final String TIME = "rtime";
	private static final String WORKOUT_DATA = "rwdata";
	private static final String SAVED_SET_DATA = "ssdata";
	private static final String INDEX = "rindex";
	private Workout workout = null;
	private long time = -1;
	private WorkoutData workoutData = null;
	private HashMap<Long, SetData> savedSetData = new HashMap<Long, SetData>();
	private int index = 0;
	private ActiveExerciseEditFragment activeExerciseEditFragment;
	private View fragmentActiveExerciseEditView;
	private SortedList<Exercise> exercises = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					return lhs.compareTo(rhs);
				}
			}, true);
	private boolean exercisesLoaded = false;
	private int latestLoaded = 0;
	private boolean showedWarning = false;
	private Toast toastWarning;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workout = getIntent().getParcelableExtra(WORKOUT);
		time = getIntent().getLongExtra(TIME, -1);
		if (savedInstanceState != null) {
			index = savedInstanceState.getInt(INDEX, 0);
			workoutData = savedInstanceState.getParcelable(WORKOUT_DATA);
			savedSetData = (HashMap<Long, SetData>) savedInstanceState
					.getSerializable(SAVED_SET_DATA);
			Log.d("hmm", savedSetData + "");
		}
		if (workout != null && time != -1) {
			toastWarning = Toast.makeText(this, R.string.run_back_warning,
					Toast.LENGTH_SHORT);
			setTitle(workout.getName());
			activeExerciseEditFragment = (ActiveExerciseEditFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragmentActiveExerciseEdit);
			fragmentActiveExerciseEditView = findViewById(R.id.fragmentActiveExerciseEdit);
			Button buttonPrevious = (Button) findViewById(R.id.buttonRunPrevious);
			Button buttonNext = (Button) findViewById(R.id.buttonRunNext);
			buttonPrevious.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					previous();
				}
			});
			buttonNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showedWarning = false;
					toastWarning.cancel();
					update(1);
				}
			});
			setCustomBackButton(new OnClickListener() {
				@Override
				public void onClick(View v) {
					previous();
				}
			});
		} else {
			Toast.makeText(this, R.string.run_workout_error, Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO Change all to resume/pause!
		setCustomProgressBarVisibility(true);
		DataProvider dataProvider = DataListener.add(this);
		Context c = getApplicationContext();
		if (workoutData == null) {
			latestLoaded = 0;
			workoutData = new WorkoutData(time, workout.getId());
			for (Long l : workout) {
				workoutData.add(new ExerciseData(l));
				dataProvider.latestExerciseData(l, c);
			}
		} else {
			latestLoaded = workoutData.size();
		}
		dataProvider.queryExercise(c);
	}

	@Override
	protected void onPause() {
		super.onPause();
		DataListener.remove(this);
		toastWarning.cancel();
		save();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(INDEX, index);
		outState.putParcelable(WORKOUT_DATA, workoutData);
		outState.putSerializable(SAVED_SET_DATA, savedSetData);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_run_workout;
	}

	@Override
	public void onBackPressed() {
		previous();
	}

	private void save() {
		if (index >= 0 && index < workoutData.size()) {
			Pair<ExerciseData, SetData> p = activeExerciseEditFragment
					.getExerciseData();
			savedSetData.put(p.first.getExerciseId(), p.second);
			workoutData.set(index, p.first);
		}
	}

	private void previous() {
		if (index == 0) {
			if (showedWarning) {
				finish(); // Cancel
			} else {
				showedWarning = true;
				toastWarning.show();
			}
		}
		update(-1);
	}

	private void update(int change) {
		int size = workoutData.size();
		if (exercisesLoaded && latestLoaded >= size) {
			setCustomProgressBarVisibility(false);
			if (change != 0) {
				save();
			}
			index += change;
			if (index < 0) {
				index = 0;
			}
			if (index > size) {
				index = size;
			}
			if (index < size) {
				ExerciseData e = workoutData.get(index);
				int pos = exercises.indexOf(new Exercise(e.getExerciseId(), 0,
						"", MuscleGroup.DEFAULT));
				Log.d("", size + " " + pos + " " + exercises.size());
				if (pos != -1) {
					Exercise ex = exercises.get(pos);
					setTitle((index + 1) + "/" + size + " " + ex.getName());
				}
				SetData s = savedSetData.get(e.getExerciseId());
				if (s == null) {
					s = SetData.getDefault();
				}
				activeExerciseEditFragment.setExerciseData(Pair.create(e, s));
				fragmentActiveExerciseEditView.setVisibility(View.VISIBLE);
			} else {
				// TODO Summary
				setTitle(R.string.summary);
				fragmentActiveExerciseEditView.setVisibility(View.GONE);
			}
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
			update(0);
		}
	}

	@Override
	public void latestCallback(ExerciseData e, long exerciseId, boolean ok) {
		savedSetData.put(exerciseId, e == null ? null : e.getBestWeightSet());
		latestLoaded++;
		update(0);
	}
}
