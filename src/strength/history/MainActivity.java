package strength.history;

import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

/**
 * Main Activity
 */
public class MainActivity extends DataListener {
	private Iterable<Exercise> exercises; // initialized in super.onCreate
	private Iterable<Weight> weights; // initialized in super.onCreate
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
		int i = radioIndex();
		Log.d("MainActivity", "onQueryClick" + i);

		if (i == 0) {
			data.query((Exercise) null);
		} else {
			data.query((Weight) null);
		}
	}

	/**
	 * @param view
	 */
	public void onInsertClick(View view) {
		int i = radioIndex();
		Log.d("MainActivity", "onInsertClick" + i);

		if (i == 0) {
			data.insert(new Exercise("Test1"));
		} else {
			data.insert(new Weight(new Date().getTime(), 75.5));
		}
	}

	/**
	 * @param view
	 */
	public void onDeleteClick(View view) {
		int i = radioIndex();
		Log.d("MainActivity", "onDeleteClick" + i);

		if (i == 0) {
			for (Exercise e : exercises) {
				data.delete(e);
			}
		} else {
			for (Weight w : weights) {
				data.delete(w);
			}
		}
	}

	/**
	 * @param view
	 */
	public void onUpdateClick(View view) {
		int i = radioIndex();
		Log.d("MainActivity", "onUpdateClick" + i);

		if (i == 0) {
			for (Exercise e : exercises) {
				Exercise tmp = new Exercise(e.getId(), e.getSync(), "Updated");
				e.updateFrom(tmp);
				data.update(e);
				break;
			}
		} else {
			for (Weight w : weights) {
				Weight tmp = new Weight(w.getId(), w.getSync(), w.getTime(),
						99.9);
				w.updateFrom(tmp);
				data.update(w);
				break;
			}
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
