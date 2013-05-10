package strength.history;

import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.structure.Weight;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends DataListener {
	private Iterable<Weight> weightData; // initialized in super.onCreate

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

	@SuppressWarnings("static-method")
	public void onQueryWeightClick(View view) {
		Log.d("MainActivity", "onQueryWeightClick");

		data.queryWeight();
	}

	@SuppressWarnings("static-method")
	public void onInsertWeightClick(View view) {
		Log.d("MainActivity", "onInsertWeightClick");

		data.insertWeight(new Weight(new Date().getTime(), 75.5));
	}

	public void onDeleteWeightClick(View view) {
		Log.d("MainActivity", "onDeleteWeightClick");

		for (Weight w : weightData) {
			data.deleteWeight(w);
		}
	}

	public void onUpdateWeightClick(View view) {
		Log.d("MainActivity", "onUpdateWeightClick");

		for (Weight w : weightData) {
			Weight tmp = new Weight(w.getId(), w.getTime(), 99.9, w.getSync());
			w.updateFrom(tmp);
			data.updateWeight(w);
			break;
		}
	}

	@Override
	public void weightDataUpdate(Iterable<Weight> weightData) {
		this.weightData = weightData;
		Log.d("MainActivity", "data update");
		for (Weight w : weightData) {
			Log.d("MainActivity", w.toString());
		}
	}
}
