package strength.history.data.service;

import strength.history.data.structure.SyncBase;
import android.app.IntentService;
import android.content.Intent;
import android.os.Messenger;

/**
 * Base class for the service framework
 * 
 * @param <E>
 *            Structure type it provides
 */
public abstract class ServiceBase<E extends SyncBase<E>> extends IntentService {

	protected abstract int getArg1();

	protected abstract void delete(E e, Messenger messenger);

	protected abstract void insert(E e, Messenger messenger);

	protected abstract void query(Messenger messenger);

	protected abstract void update(E e, Messenger messenger);

	/**
	 * enum with possible requests to the WeightService
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
		 * Get all items
		 */
		QUERY,
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
	public int onStartCommand(Intent intent, int flags, int startId) {
		Request request = (Request) intent.getSerializableExtra(REQUEST);
		if (request != Request.QUERY) {
			query_interrupt = true;
		} else {
			query_interrupt = false;
		}
		return super.onStartCommand(intent, flags, startId);
	}
}
