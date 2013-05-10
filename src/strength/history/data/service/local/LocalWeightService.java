package strength.history.data.service.local;

import strength.history.data.db.WeightDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Weight;
import android.content.Intent;
import android.os.Messenger;
import android.util.Log;

/**
 * Local weight service
 */
public class LocalWeightService extends
		LocalServiceBase<Weight, WeightDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WEIGHT = "WEIGHT";

	/**
	 * Constructor
	 */
	public LocalWeightService() {
		super("LocalWeightService");
	}

	@Override
	protected WeightDBHelper getDB() {
		return WeightDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected int getArg1() {
		return Service.WEIGHT.ordinal();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalWeightService", "onHandleIntent");

		Request request = (Request) intent.getSerializableExtra(REQUEST);
		Messenger messenger = intent.getParcelableExtra(MESSENGER);
		if (request != null && messenger != null) {
			switch (request) {
			case DELETE:
				delete((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			case INSERT:
				insert((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			case QUERY:
				query(messenger);
				break;
			case UPDATE:
				update((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			}
		}
	}
}
