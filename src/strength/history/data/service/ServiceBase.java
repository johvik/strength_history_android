package strength.history.data.service;

import android.app.IntentService;

public abstract class ServiceBase extends IntentService {
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

	public enum Service {
		/**
		 * Service that provides weight data
		 */
		WEIGHT_DATA;

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

	public ServiceBase(String name) {
		super(name);
	}
}
