package strength.history.data.service.local;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import strength.history.data.db.WorkoutDataDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.WorkoutData;

public class LocalWorkoutDataService extends
		LocalServiceBase<WorkoutData, WorkoutDataDBHelper> {
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
		 * Gets the latest non empty exercise data entry
		 */
		LATEST_EXERCISE_DATA,
		/**
		 * Gets the latest workout data entry
		 */
		LATEST_WORKOUT_DATA,
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
	public static final String WORKOUT_DATA = "WORKOUT_DATA";

	public LocalWorkoutDataService() {
		super("LocalWorkoutDataService");
	}

	@Override
	protected WorkoutDataDBHelper getDB() {
		return WorkoutDataDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected String getIntentName() {
		return WORKOUT_DATA;
	}

	@Override
	protected int getArg1() {
		return Service.WORKOUT_DATA.ordinal();
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

	protected void latestExerciseData(long exerciseId, Messenger messenger) {
		WorkoutDataDBHelper db = getDB();
		Message msg = Message.obtain();
		msg.arg1 = getArg1();
		msg.arg2 = Request.LATEST_EXERCISE_DATA.ordinal();

		ExerciseData e = db.latestExerciseData(exerciseId);
		msg.what = e != null ? 1 : 0;
		msg.obj = e;

		try {
			messenger.send(msg);
		} catch (RemoteException ex) {
			Log.e("LocalWorkoutDataService", "Failed to send message");
		}
	}

	protected void latestWorkoutData(long workoutId, Messenger messenger) {
		WorkoutDataDBHelper db = getDB();
		Message msg = Message.obtain();
		msg.arg1 = getArg1();
		msg.arg2 = Request.LATEST_WORKOUT_DATA.ordinal();

		WorkoutData w = db.latestWorkoutData(workoutId);
		msg.what = w != null ? 1 : 0;
		msg.obj = w;

		try {
			messenger.send(msg);
		} catch (RemoteException ex) {
			Log.e("LocalWorkoutDataService", "Failed to send message");
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalWorkoutDataService", "onHandleIntent");

		int id = intent.getIntExtra(REQUEST, -1);
		if (id != -1) {
			Request request = Request.parse(id);
			Log.d("LocalWorkoutDataService", "request=" + request);
			Messenger messenger = intent.getParcelableExtra(MESSENGER);
			if (messenger != null) {
				switch (request) {
				case DELETE:
					delete((WorkoutData) intent
							.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case INSERT:
					insert((WorkoutData) intent
							.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case LATEST_EXERCISE_DATA:
					latestExerciseData(
							intent.getLongExtra(getIntentName(), -1), messenger);
					break;
				case LATEST_WORKOUT_DATA:
					latestWorkoutData(intent.getLongExtra(getIntentName(), -1),
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
					update((WorkoutData) intent
							.getParcelableExtra(getIntentName()),
							messenger);
					break;
				}
			}
		}
	}
}
