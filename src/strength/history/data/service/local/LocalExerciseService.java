package strength.history.data.service.local;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import strength.history.data.db.ExerciseDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Exercise;

/**
 * Local exercise service
 */
public class LocalExerciseService extends
		LocalServiceBase<Exercise, ExerciseDBHelper> {
	/**
	 * enum with possible requests to the Service
	 */
	public enum Request {
		/**
		 * Creates default values
		 */
		CREATE_DEFAULTS,
		/**
		 * Delete the provided item
		 */
		DELETE,
		/**
		 * Insert the provided item
		 */
		INSERT,
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
	public static final String EXERCISE = "EXERCISE";

	/**
	 * Constructor
	 */
	public LocalExerciseService() {
		super("LocalExerciseService");
	}

	@Override
	protected ExerciseDBHelper getDB() {
		return ExerciseDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected String getIntentName() {
		return EXERCISE;
	}

	@Override
	protected int getArg1() {
		return Service.EXERCISE.ordinal();
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

	protected void createDefaults(Messenger messenger) {
		ExerciseDBHelper db = getDB();
		Message msg = Message.obtain();
		msg.arg1 = getArg1();
		msg.arg2 = Request.CREATE_DEFAULTS.ordinal();

		ArrayList<Exercise> res = db.createDefaults();
		msg.what = 1;
		msg.obj = res;

		try {
			messenger.send(msg);
		} catch (RemoteException ex) {
			Log.e("LocalExerciseService", "Failed to send message");
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalExerciseService", "onHandleIntent");

		int id = intent.getIntExtra(REQUEST, -1);
		if (id != -1) {
			Request request = Request.parse(id);
			Log.d("LocalWorkoutDataService", "request=" + request);
			Messenger messenger = intent.getParcelableExtra(MESSENGER);
			if (messenger != null) {
				switch (request) {
				case CREATE_DEFAULTS:
					createDefaults(messenger);
					break;
				case DELETE:
					delete((Exercise) intent
							.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case INSERT:
					insert((Exercise) intent
							.getParcelableExtra(getIntentName()),
							messenger);
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
					update((Exercise) intent
							.getParcelableExtra(getIntentName()),
							messenger);
					break;
				}
			}
		}
	}
}
