package strength.history;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import strength.history.data.DataListener;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.service.ServiceBase.Service;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Main Activity
 */
public class MainActivity extends DataListener implements
		ExerciseProvider.Events, WeightProvider.Events, WorkoutProvider.Events,
		WorkoutDataProvider.Events {
	private TreeSet<Exercise> exercises = new TreeSet<Exercise>();
	private TreeSet<Weight> weights = new TreeSet<Weight>();
	private TreeSet<Workout> workouts = new TreeSet<Workout>();
	private TreeSet<WorkoutData> workoutData = new TreeSet<WorkoutData>();

	private RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

		Intent i = new Intent(this, ExercisesActivity.class);
		startActivity(i);
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
			data.query((Exercise) null, getApplicationContext());
			break;
		case WEIGHT:
			data.query((Weight) null, getApplicationContext());
			break;
		case WORKOUT:
			data.query((Workout) null, getApplicationContext());
			break;
		case WORKOUT_DATA:
			data.query((WorkoutData) null, getApplicationContext());
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
			data.insert(new Exercise("Test1"), getApplicationContext());
			break;
		case WEIGHT:
			data.insert(new Weight(new Date().getTime(), 75.5),
					getApplicationContext());
			break;
		case WORKOUT:
			Workout w = new Workout("Testing!");
			w.add((long) 10);
			data.insert(w, getApplicationContext());
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
				data.insert(d, getApplicationContext());
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
				data.delete(e, getApplicationContext());
			}
			break;
		case WEIGHT:
			for (Weight w : weights) {
				data.delete(w, getApplicationContext());
			}
			break;
		case WORKOUT:
			for (Workout w : workouts) {
				data.delete(w, getApplicationContext());
			}
			break;
		case WORKOUT_DATA:
			for (WorkoutData w : workoutData) {
				data.delete(w, getApplicationContext());
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
				data.update(e, getApplicationContext());
				break;
			}
			break;
		case WEIGHT:
			for (Weight w : weights) {
				Weight tmp = new Weight(w.getId(), w.getSync(), w.getTime(),
						w.getWeight() + 0.1);
				w.updateFrom(tmp);
				data.update(w, getApplicationContext());
				break;
			}
			break;
		case WORKOUT:
			for (Workout w : workouts) {
				Workout tmp = new Workout(w.getId(), w.getSync(), "Updated!!!");
				w.updateFrom(tmp);
				data.update(w, getApplicationContext());
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
				data.update(w, getApplicationContext());
				break;
			}
			break;
		}
	}

	public void onStopClick(View view) {
		Service s = Service.parse(radioIndex());
		Log.d("MainActivity", "onStopClick " + s);

		switch (s) {
		case EXERCISE:
			data.stop((Exercise) null, getApplicationContext());
			break;
		case WEIGHT:
			data.stop((Weight) null, getApplicationContext());
			break;
		case WORKOUT:
			data.stop((Workout) null, getApplicationContext());
			break;
		case WORKOUT_DATA:
			data.stop((WorkoutData) null, getApplicationContext());
			break;
		}
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		exercises.clear();
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		exercises.clear();
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		Log.d("MainActivity", "exercise data update=" + done);
		Log.d("MainActivity", e.toString());
		exercises.addAll(e);
	}

	@Override
	public void updateCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		exercises.clear();
	}

	@Override
	public void deleteCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		workoutData.clear();
	}

	@Override
	public void insertCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		workoutData.clear();
	}

	@Override
	public void workoutDataQueryCallback(Collection<WorkoutData> e, boolean done) {
		Log.d("MainActivity", "workoutdata data update=" + done);
		Log.d("MainActivity", e.toString());
		workoutData.addAll(e);
	}

	@Override
	public void updateCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		workoutData.clear();
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		workouts.clear();
	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		workouts.clear();
	}

	@Override
	public void workoutQueryCallback(Collection<Workout> e, boolean done) {
		Log.d("MainActivity", "workout data update=" + done);
		Log.d("MainActivity", e.toString());
		workouts.addAll(e);
	}

	@Override
	public void updateCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		workouts.clear();
	}

	@Override
	public void deleteCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		weights.clear();
	}

	@Override
	public void insertCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		weights.clear();
	}

	@Override
	public void weightQueryCallback(Collection<Weight> e, boolean done) {
		Log.d("MainActivity", "weight data update=" + done);
		Log.d("MainActivity", e.toString());
		weights.addAll(e);
	}

	@Override
	public void updateCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		weights.clear();
	}
}
