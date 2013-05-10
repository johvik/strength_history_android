package strength.history.data.service;

import strength.history.data.structure.Weight;
import android.content.Intent;
import android.os.Messenger;

public abstract class WeightServiceBase extends ServiceBase {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WEIGHT = "WEIGHT";

	public interface WeightProvider {
		/**
		 * Deletes weight
		 * 
		 * @param weight
		 *            Item to delete
		 */
		public void deleteWeight(Weight weight);

		/**
		 * Inserts weight
		 * 
		 * @param weight
		 *            Item to insert
		 */
		public void insertWeight(Weight weight);

		/**
		 * Gets all items
		 */
		public void queryWeight();

		/**
		 * Updates weight
		 * 
		 * @param weight
		 *            Item to update
		 */
		public void updateWeight(Weight weight);
	}

	/**
	 * enum with possible requests to the WeightDataService
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
	 * When set to true query stops in the next iteration
	 */
	protected boolean query_interrupt = false;

	public WeightServiceBase(String name) {
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

	/**
	 * Deletes weight in the service
	 * 
	 * @param weight
	 *            Item to delete
	 * @param messenger
	 *            Callback
	 */
	protected abstract void delete(Weight weight, Messenger messenger);

	/**
	 * Inserts weight in the service
	 * 
	 * @param weight
	 *            Item to insert
	 * @param messenger
	 *            Callback
	 */
	protected abstract void insert(Weight weight, Messenger messenger);

	/**
	 * Gets all items in the service
	 * 
	 * @param messenger
	 *            Callback
	 */
	protected abstract void query(Messenger messenger);

	/**
	 * Updates weight in the service
	 * 
	 * @param weight
	 *            Item to update
	 * @param messenger
	 *            Callback
	 */
	protected abstract void update(Weight weight, Messenger messenger);
}
