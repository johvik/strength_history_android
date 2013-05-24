package strength.history.data.service.local;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import strength.history.data.db.WeightDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Weight;

/**
 * Local weight service
 */
public class LocalWeightService extends
		LocalServiceBase<Weight, WeightDBHelper> {
	/**
	 * enum with possible requests to the Service
	 */
	public enum Request {
		/**
		 * Delete the provided item
		 */
		DELETE,
		/**
		 * Insert the provided item
		 */
		INSERT,
		/**
		 * Gets the latest entry
		 */
		LATEST,
		/**
		 * Recreates the DB, all data is lost
		 */
		PURGE,
		/**
		 * Get all items
		 */
		QUERY,
		/**
		 * Stops all ongoing queries
		 */
		STOP,
		/**
		 * Updates the provided item
		 */
		UPDATE;

		private static final Request[] REQUEST_VALUES = Request.values();

		/**
		 * Converts a number to a enum
		 * 
		 * @param i
		 *            Index in the enum
		 * @return The enum at index i % length
		 */
		public static Request parse(int i) {
			return REQUEST_VALUES[i % REQUEST_VALUES.length];
		}
	}

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
	protected String getIntentName() {
		return WEIGHT;
	}

	@Override
	protected int getArg1() {
		return Service.WEIGHT.ordinal();
	}

	@Override
	protected int getDeleteArg() {
		return Request.DELETE.ordinal();
	}

	@Override
	protected int getInsertArg() {
		return Request.INSERT.ordinal();
	}

	@Override
	protected int getQueryArg() {
		return Request.QUERY.ordinal();
	}

	@Override
	protected int getStopArg() {
		return Request.STOP.ordinal();
	}

	@Override
	protected int getUpdateArg() {
		return Request.UPDATE.ordinal();
	}

	protected final void latest(Messenger messenger) {
		WeightDBHelper db = getDB();
		Message msg = Message.obtain();
		msg.arg1 = getArg1();
		msg.arg2 = Request.LATEST.ordinal();

		Weight w = db.latest();
		msg.what = w != null ? 1 : 0;
		msg.obj = w;

		try {
			messenger.send(msg);
		} catch (RemoteException ex) {
			Log.e("LocalWeightService", "Failed to send message");
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalWeightService", "onHandleIntent");

		int id = intent.getIntExtra(REQUEST, -1);
		if (id != -1) {
			Request request = Request.parse(id);
			Log.d("LocalWeightService", "request=" + request);
			Messenger messenger = intent.getParcelableExtra(MESSENGER);
			if (messenger != null) {
				switch (request) {
				case DELETE:
					delete((Weight) intent.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case INSERT:
					insert((Weight) intent.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case LATEST:
					latest(messenger);
					break;
				case PURGE:
					purge(messenger);
					break;
				case QUERY:
					query(messenger);
					break;
				case STOP:
					// Do nothing (see ServiceBase.onStartCommand)
					break;
				case UPDATE:
					update((Weight) intent.getParcelableExtra(getIntentName()),
							messenger);
					break;
				}
			}
		}
	}
}
