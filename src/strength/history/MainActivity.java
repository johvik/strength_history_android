package strength.history;

import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.service.ServiceBase.Service;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;

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
						99.9);
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
}
