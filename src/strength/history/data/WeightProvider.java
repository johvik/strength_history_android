package strength.history.data;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Intent;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.service.ServiceBase;
import strength.history.data.service.ServiceBase.Request;
import strength.history.data.service.WeightServiceBase;
import strength.history.data.service.local.LocalWeightService;
import strength.history.data.structure.Weight;

/**
 * Provides data mappings for the weight service
 */
public class WeightProvider {
	/**
	 * Interface of methods that this service provides
	 */
	public interface Provides {
		/**
		 * Deletes item
		 * 
		 * @param e
		 *            Item to delete
		 */
		public void delete(Weight e);

		/**
		 * Inserts item
		 * 
		 * @param e
		 *            Item to insert
		 */
		public void insert(Weight e);

		/**
		 * Gets all items
		 * 
		 * @param e
		 *            Unused, pass null
		 */
		public void query(Weight e);

		/**
		 * Updates item
		 * 
		 * @param e
		 *            Item to update
		 */
		public void update(Weight e);
	}

	/**
	 * Events used for callback
	 */
	public interface Events {
		/**
		 * Weights was changed
		 * 
		 * @param weights
		 *            The new data
		 */
		public void callback(Iterable<Weight> weights);
	}

	private TreeSet<Weight> weights = new TreeSet<Weight>();

	/**
	 * Retrieves the items
	 * 
	 * @return The items
	 */
	public Iterable<Weight> get() {
		return weights;
	}

	/**
	 * Handles a request
	 * 
	 * @param request
	 *            Type of request
	 * @param object
	 *            Object from the message
	 * @param ok
	 *            True if successful
	 * @param dataListener
	 *            To send callback
	 */
	public void handleCallback(Request request, Object object, boolean ok,
			DataListener dataListener) {
		switch (request) {
		case DELETE:
			handleDelete((Weight) object, ok, dataListener);
			break;
		case INSERT:
			handleInsert((Weight) object, ok, dataListener);
			break;
		case QUERY:
			@SuppressWarnings("unchecked")
			ArrayList<Weight> tmp = (ArrayList<Weight>) object;
			handleQuery(tmp, ok, dataListener);
			break;
		case UPDATE:
			handleUpdate((Weight) object, ok, dataListener);
			break;
		}
	}

	/**
	 * Deletes item
	 * 
	 * @param e
	 *            Item to delete
	 * @param dataListener
	 *            Object of the listener, may be null
	 * @param messenger
	 *            Messenger for callback
	 */
	public static void delete(Weight e, DataListener dataListener,
			Messenger messenger) {
		if (dataListener != null) {
			// Local
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					WeightServiceBase.Request.DELETE);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			intent.putExtra(WeightServiceBase.WEIGHT, e);
			dataListener.startService(intent);
		}
	}

	/**
	 * Inserts item
	 * 
	 * @param e
	 *            Item to insert
	 * @param dataListener
	 *            Object of the listener, may be null
	 * @param messenger
	 *            Messenger for callback
	 */
	public static void insert(Weight e, DataListener dataListener,
			Messenger messenger) {
		if (dataListener != null) {
			// Local
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					WeightServiceBase.Request.INSERT);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			intent.putExtra(WeightServiceBase.WEIGHT, e);
			dataListener.startService(intent);
		}
	}

	/**
	 * Gets all items
	 * 
	 * @param e
	 *            Unused, pass null
	 * @param dataListener
	 *            Object of the listener, may be null
	 * @param messenger
	 *            Messenger for callback
	 */
	public static void query(Weight e, DataListener dataListener,
			Messenger messenger) {
		if (dataListener != null) {
			// Local
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					WeightServiceBase.Request.QUERY);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			dataListener.startService(intent);
		}
	}

	/**
	 * Updates item
	 * 
	 * @param e
	 *            Item to update
	 * @param dataListener
	 *            Object of the listener, may be null
	 * @param messenger
	 *            Messenger for callback
	 */
	public static void update(Weight e, DataListener dataListener,
			Messenger messenger) {
		if (dataListener != null) {
			// Local
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					WeightServiceBase.Request.UPDATE);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			intent.putExtra(WeightServiceBase.WEIGHT, e);
			dataListener.startService(intent);
		}
	}

	private void handleDelete(Weight e, boolean ok, DataListener dataListener) {
		if (!ok) {
			Log.e("DataProvider", "failed to delete " + e);
		} else {
			weights.remove(e);
			if (dataListener != null) {
				dataListener.callback(weights);
			}
		}
	}

	private void handleInsert(Weight e, boolean ok, DataListener dataListener) {
		if (!ok) {
			Log.e("DataProvider", "failed to insert " + e);
		} else {
			weights.add(e);
			if (dataListener != null) {
				dataListener.callback(weights);
			}
		}
	}

	private void handleQuery(ArrayList<Weight> e, boolean ok,
			DataListener dataListener) {
		boolean added = weights.addAll(e);
		if (added && dataListener != null) {
			dataListener.callback(weights);
		} else if (!added) {
			Log.d("DataProvider", "query nothing changed");
		}
	}

	private void handleUpdate(Weight e, boolean ok, DataListener dataListener) {
		if (!ok) {
			Log.e("DataProvider", "failed to update " + e);
		} else {
			// Search by id
			for (Weight w : weights) {
				if (e.getId() == w.getId()) {
					weights.remove(w);
					w.updateFrom(e);
					weights.add(w);
					break;
				}
			}
			if (dataListener != null) {
				dataListener.callback(weights);
			}
		}
	}
}
