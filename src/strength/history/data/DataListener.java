package strength.history.data;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Base Activity that acts as a listener
 */
public abstract class DataListener extends Activity {
	/**
	 * Data provider object
	 */
	protected static final DataProvider data = new DataProvider();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		data.addListeners(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		Log.d("DataListener", "onDestroy");
		data.removeListeners(this);
		super.onDestroy();
	}
}
