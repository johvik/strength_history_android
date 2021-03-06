package strength.history.data.service;

import strength.history.data.structure.SyncBase;
import android.app.IntentService;
import android.content.Intent;
import android.os.Messenger;

/**
 * Base class for the service framework. Remember to override getXXXArg and
 * onHandleIntent if adding more request types!
 * 
 * @param <E>
 *            Structure type it provides
 */
public abstract class ServiceBase<E extends SyncBase<E>> extends IntentService {

	protected abstract int getArg1();

	protected int getDeleteArg() {
		return Request.DELETE.ordinal();
	}

	protected int getInsertArg() {
		return Request.INSERT.ordinal();
	}

	protected int getQueryArg() {
		return Request.QUERY.ordinal();
	}

	protected int getStopArg() {
		return Request.STOP.ordinal();
	}

	protected int getUpdateArg() {
		return Request.UPDATE.ordinal();
	}

	protected abstract void delete(E e, Messenger messenger);

	protected abstract void insert(E e, Messenger messenger);

	protected abstract void purge(Messenger messenger);

	protected abstract void query(Messenger messenger);

	protected abstract void update(E e, Messenger messenger);

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
	 * Name of the messenger passed with the intent
	 */
	public static final String MESSENGER = "MESSENGER";
	/**
	 * Name of the request passed with the intent
	 */
	public static final String REQUEST = "REQUEST";

	/**
	 * Limit for queries
	 */
	protected static final int QUERY_LIMIT = 50;
	/**
	 * Number of tries to the DB
	 */
	protected static final int DB_TRIES = 2;
	/**
	 * When set to true query stops in the next iteration
	 */
	protected boolean query_interrupt = false;

	/**
	 * enum with all services
	 */
	public enum Service {
		/**
		 * Service that provides exercise
		 */
		EXERCISE,
		/**
		 * Service that provides weight
		 */
		WEIGHT,
		/**
		 * Service that provides workout
		 */
		WORKOUT,
		/**
		 * Service that provides workout data
		 */
		WORKOUT_DATA;

		private static final Service[] SERVICE_VALUES = Service.values();

		/**
		 * Converts a number to a enum
		 * 
		 * @param i
		 *            Index in the enum
		 * @return The enum at index i % length
		 */
		public static Service parse(int i) {
			return SERVICE_VALUES[i % SERVICE_VALUES.length];
		}
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public ServiceBase(String name) {
		super(name);
	}

	@Override
	public final int onStartCommand(Intent intent, int flags, int startId) {
		int id = intent.getIntExtra(REQUEST, -1);
		if (id != -1) {
			if (id == getQueryArg()) {
				query_interrupt = false;
			} else if (id == getStopArg()) {
				query_interrupt = true;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
}
