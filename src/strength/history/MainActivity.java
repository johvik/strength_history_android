package strength.history;

import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

import strength.history.data.DataListener;
import strength.history.data.DataProvider;
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Main Activity
 */
public class MainActivity extends Activity implements ExerciseProvider.Events,
		WeightProvider.Events, WorkoutProvider.Events,
		WorkoutDataProvider.Events {
	private DataProvider mDataProvider = null;
	private TreeSet<Exercise> mExercises = new TreeSet<Exercise>();
	private TreeSet<Weight> mWeights = new TreeSet<Weight>();
	private TreeSet<Workout> mWorkouts = new TreeSet<Workout>();
	private TreeSet<WorkoutData> mWorkoutData = new TreeSet<WorkoutData>();

	private RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDataProvider = DataListener.add(this);
		setContentView(R.layout.activity_main);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);

		Intent i = new Intent(this, ExercisesActivity.class);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataListener.remove(this);
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
			mDataProvider.query((Exercise) null, getApplicationContext());
			break;
		case WEIGHT:
			mDataProvider.query((Weight) null, getApplicationContext());
			break;
		case WORKOUT:
			mDataProvider.query((Workout) null, getApplicationContext());
			break;
		case WORKOUT_DATA:
			mDataProvider.query((WorkoutData) null, getApplicationContext());
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
			mDataProvider
					.insert(new Exercise("Test1"), getApplicationContext());
			break;
		case WEIGHT:
			mDataProvider.insert(new Weight(new Date().getTime(), 75.5),
					getApplicationContext());
			break;
		case WORKOUT:
			Workout w = new Workout("Testing!");
			w.add((long) 10);
			mDataProvider.insert(w, getApplicationContext());
			break;
		case WORKOUT_DATA:
			for (Workout wo : mWorkouts) {
				WorkoutData d = new WorkoutData(new Date().getTime(),
						wo.getId());
				for (Exercise e : mExercises) {
					ExerciseData ed = new ExerciseData(e.getId());
					ed.add(new SetData(55, 2));
					d.add(ed);
					break;
				}
				mDataProvider.insert(d, getApplicationContext());
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
			for (Exercise e : mExercises) {
				mDataProvider.delete(e, getApplicationContext());
			}
			break;
		case WEIGHT:
			for (Weight w : mWeights) {
				mDataProvider.delete(w, getApplicationContext());
			}
			break;
		case WORKOUT:
			for (Workout w : mWorkouts) {
				mDataProvider.delete(w, getApplicationContext());
			}
			break;
		case WORKOUT_DATA:
			for (WorkoutData w : mWorkoutData) {
				mDataProvider.delete(w, getApplicationContext());
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
			for (Exercise e : mExercises) {
				Exercise tmp = new Exercise(e.getId(), e.getSync(), "Updated");
				e.updateFrom(tmp);
				mDataProvider.update(e, getApplicationContext());
				break;
			}
			break;
		case WEIGHT:
			for (Weight w : mWeights) {
				Weight tmp = new Weight(w.getId(), w.getSync(), w.getTime(),
						w.getWeight() + 0.1);
				w.updateFrom(tmp);
				mDataProvider.update(w, getApplicationContext());
				break;
			}
			break;
		case WORKOUT:
			for (Workout w : mWorkouts) {
				Workout tmp = new Workout(w.getId(), w.getSync(), "Updated!!!");
				w.updateFrom(tmp);
				mDataProvider.update(w, getApplicationContext());
				break;
			}
			break;
		case WORKOUT_DATA:
			for (WorkoutData w : mWorkoutData) {
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
				mDataProvider.update(w, getApplicationContext());
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
			mDataProvider.stop((Exercise) null, getApplicationContext());
			break;
		case WEIGHT:
			mDataProvider.stop((Weight) null, getApplicationContext());
			break;
		case WORKOUT:
			mDataProvider.stop((Workout) null, getApplicationContext());
			break;
		case WORKOUT_DATA:
			mDataProvider.stop((WorkoutData) null, getApplicationContext());
			break;
		}
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		mExercises.clear();
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		mExercises.clear();
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		Log.d("MainActivity", "exercise data update=" + done);
		Log.d("MainActivity", e.toString());
		mExercises.addAll(e);
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		mExercises.clear();
	}

	@Override
	public void deleteCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		mWorkoutData.clear();
	}

	@Override
	public void insertCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		mWorkoutData.clear();
	}

	@Override
	public void workoutDataQueryCallback(Collection<WorkoutData> e, boolean done) {
		Log.d("MainActivity", "workoutdata data update=" + done);
		Log.d("MainActivity", e.toString());
		mWorkoutData.addAll(e);
	}

	@Override
	public void updateCallback(WorkoutData old, WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		mWorkoutData.clear();
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		mWorkouts.clear();
	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		mWorkouts.clear();
	}

	@Override
	public void workoutQueryCallback(Collection<Workout> e, boolean done) {
		Log.d("MainActivity", "workout data update=" + done);
		Log.d("MainActivity", e.toString());
		mWorkouts.addAll(e);
	}

	@Override
	public void updateCallback(Workout old, Workout e, boolean ok) {
		// TODO Auto-generated method stub
		mWorkouts.clear();
	}

	@Override
	public void deleteCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		mWeights.clear();
	}

	@Override
	public void insertCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		mWeights.clear();
	}

	@Override
	public void weightQueryCallback(Collection<Weight> e, boolean done) {
		Log.d("MainActivity", "weight data update=" + done);
		Log.d("MainActivity", e.toString());
		mWeights.addAll(e);
	}

	@Override
	public void updateCallback(Weight old, Weight e, boolean ok) {
		// TODO Auto-generated method stub
		mWeights.clear();
	}
}
