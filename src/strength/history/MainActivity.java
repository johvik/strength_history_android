package strength.history;

import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

/**
 * Main Activity
 */
public class MainActivity extends DataListener {
	private Iterable<Exercise> exercises; // initialized in super.onCreate
	private Iterable<Weight> weights; // initialized in super.onCreate

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * @param view
	 */
	@SuppressWarnings("static-method")
	public void onQueryWeightClick(View view) {
		Log.d("MainActivity", "onQueryWeightClick");

		data.query((Weight) null);
	}

	/**
	 * @param view
	 */
	@SuppressWarnings("static-method")
	public void onInsertWeightClick(View view) {
		Log.d("MainActivity", "onInsertWeightClick");

		data.insert(new Weight(new Date().getTime(), 75.5));
	}

	/**
	 * @param view
	 */
	public void onDeleteWeightClick(View view) {
		Log.d("MainActivity", "onDeleteWeightClick");

		for (Weight w : weights) {
			data.delete(w);
		}
	}

	/**
	 * @param view
	 */
	public void onUpdateWeightClick(View view) {
		Log.d("MainActivity", "onUpdateWeightClick");

		for (Weight w : weights) {
			Weight tmp = new Weight(w.getId(), w.getTime(), 99.9, w.getSync());
			w.updateFrom(tmp);
			data.update(w);
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
}
