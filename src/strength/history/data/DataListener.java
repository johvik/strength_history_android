package strength.history.data;

import android.app.Activity;
import android.os.Bundle;

public abstract class DataListener extends Activity implements
		DataProvider.Events {
	/**
	 * Data provider object
	 */
	protected static final DataProvider data = new DataProvider();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DataProvider.setListener(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		DataProvider.setListener(null);
		super.onDestroy();
	}
}
