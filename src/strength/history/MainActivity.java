package strength.history;

import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.service.ServiceBase.Service;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Main Activity
 */
public class MainActivity extends DataListener {
	// initialized in super.onCreate from callback
	private Iterable<Exercise> exercises;
	private Iterable<Weight> weights;
	private Iterable<Workout> workouts;
	private Iterable<WorkoutData> workoutData;

	private RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private int radioIndex() {
		return radioGroup.indexOfChild(radioGroup.findViewById(radioGroup
				.getCheckedRadioButtonId()));
	}

	/**
	 * @param view
	 */
	public void onQueryClick(View view) {
		Service s = Service.parse(radioIndex());
		Log.d("MainActivity", "onQueryClick " + s);

		switch (s) {
		case EXERCISE:
			data.query((Exercise) null);
			break;
		case WEIGHT:
			data.query((Weight) null);
			break;
		case WORKOUT:
			data.query((Workout) null);
			break;
		case WORKOUT_DATA:
			data.query((WorkoutData) null);
			break;
		}
	}

	/**
	 * @param view
	 */
	public void onInsertClick(View view) {
		Service s = Service.parse(radioIndex());
		Log.d("MainActivity", "onInsertClick " + s);

		switch (s) {
		case EXERCISE:
			data.insert(new Exercise("Test1"));
			break;
		case WEIGHT:
			data.insert(new Weight(new Date().getTime(), 75.5));
			break;
		case WORKOUT:
			Workout w = new Workout("Testing!");
			w.add((long) 10);
			data.insert(w);
			break;
		case WORKOUT_DATA:
			for (Workout wo : workouts) {
				WorkoutData d = new WorkoutData(new Date().getTime(),
						wo.getId());
				for (Exercise e : exercises) {
					ExerciseData ed = new ExerciseData(e.getId());
					ed.add(new SetData(55, 2));
					d.add(ed);
					break;
				}
				data.insert(d);
				break;
			}
			break;
		}
	}

	/**
	 * @param view
	 */
	public void onDeleteClick(View view) {
		Service s = Service.parse(radioIndex());
		Log.d("MainActivity", "onDeleteClick " + s);

		switch (s) {
		case EXERCISE:
			for (Exercise e : exercises) {
				data.delete(e);
			}
			break;
		case WEIGHT:
			for (Weight w : weights) {
				data.delete(w);
			}
			break;
		case WORKOUT:
			for (Workout w : workouts) {
				data.delete(w);
			}
			break;
		case WORKOUT_DATA:
			for (WorkoutData w : workoutData) {
				data.delete(w);
			}
			break;
		}
	}

	/**
	 * @param view
	 */
	public void onUpdateClick(View view) {
		Service s = Service.parse(radioIndex());
		Log.d("MainActivity", "onUpdateClick " + s);

		switch (s) {
		case EXERCISE:
			for (Exercise e : exercises) {
				Exercise tmp = new Exercise(e.getId(), e.getSync(), "Updated");
				e.updateFrom(tmp);
				data.update(e);
				break;
			}
			break;
		case WEIGHT:
			for (Weight w : weights) {
				Weight tmp = new Weight(w.getId(), w.getSync(), w.getTime(),
						w.getWeight() + 0.1);
				w.updateFrom(tmp);
				data.update(w);
				break;
			}
			break;
		case WORKOUT:
			for (Workout w : workouts) {
				Workout tmp = new Workout(w.getId(), w.getSync(), "Updated!!!");
				w.updateFrom(tmp);
				data.update(w);
				break;
			}
			break;
		case WORKOUT_DATA:
			for (WorkoutData w : workoutData) {
				WorkoutData tmp = new WorkoutData(w.getId(), w.getSync(),
						w.getTime(), w.getWorkoutId());
				for (ExerciseData e : w) {
					ExerciseData t2 = new ExerciseData(e.getId(),
							e.getExerciseId());
					for (SetData d : e) {
						SetData t3 = new SetData(d.getId(),
								d.getWeight() + 0.5, d.getRepetitions());
						d.updateFrom(t3);
						t2.add(d);
						break;
					}
					e.updateFrom(t2);
					tmp.add(e);
					break;
				}
				w.updateFrom(tmp);
				data.update(w);
				break;
			}
			break;
		}
	}

	@Override
	public void exerciseCallback(Iterable<Exercise> data) {
		exercises = data;
		Log.d("MainActivity", "exercise data update");
		for (Exercise e : exercises) {
			Log.d("MainActivity", e.toString());
		}
	}

	@Override
	public void weightCallback(Iterable<Weight> data) {
		weights = data;
		Log.d("MainActivity", "weight data update");
		for (Weight w : weights) {
			Log.d("MainActivity", w.toString());
		}
	}

	@Override
	public void workoutCallback(Iterable<Workout> data) {
		workouts = data;
		Log.d("MainActivity", "workout data update");
		for (Workout w : workouts) {
			Log.d("MainActivity", w.toString());
		}
	}

	@Override
	public void workoutDataCallback(Iterable<WorkoutData> data) {
		workoutData = data;
		Log.d("MainActivity", "workoutdata data update");
		for (WorkoutData w : workoutData) {
			Log.d("MainActivity", w.toString());
		}
	}
}
