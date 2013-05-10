package strength.history.data.provider;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Intent;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.DataListener;
import strength.history.data.service.ServiceBase;
import strength.history.data.service.ServiceBase.Request;
import strength.history.data.structure.Base;

/**
 * Base class for providers
 * 
 * @param <E>
 */
public abstract class Provider<E extends Base<E>> {
	protected final TreeSet<E> data = new TreeSet<E>();

	/**
	 * Retrieves the data
	 * 
	 * @return The data
	 */
	public final Iterable<E> get() {
		return data;
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
	@SuppressWarnings("unchecked")
	public final void handleCallback(Request request, Object object,
			boolean ok, DataListener dataListener) {
		switch (request) {
		case DELETE:
			handleDelete((E) object, ok, dataListener);
			break;
		case INSERT:
			handleInsert((E) object, ok, dataListener);
			break;
		case QUERY:
			handleQuery((ArrayList<E>) object, ok, dataListener);
			break;
		case UPDATE:
			handleUpdate((E) object, ok, dataListener);
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
	public final void delete(E e, DataListener dataListener, Messenger messenger) {
		runLocalService(e, dataListener, messenger, Request.DELETE);
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
	public final void insert(E e, DataListener dataListener, Messenger messenger) {
		runLocalService(e, dataListener, messenger, Request.INSERT);
	}

	/**
	 * Gets all items
	 * 
	 * @param e
	 *            Not used, pass null
	 * @param dataListener
	 *            Object of the listener, may be null
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void query(E e, DataListener dataListener, Messenger messenger) {
		runLocalService(e, dataListener, messenger, Request.QUERY);
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
	public final void update(E e, DataListener dataListener, Messenger messenger) {
		runLocalService(e, dataListener, messenger, Request.UPDATE);
	}

	protected abstract Class<?> getLocalServiceClass();

	protected abstract String getDataFieldName();

	protected abstract void callback(DataListener dataListener);

	private void handleDelete(E e, boolean ok, DataListener dataListener) {
		if (!ok) {
			Log.e("Provider", "failed to delete " + e);
		} else {
			data.remove(e);
			if (dataListener != null) {
				callback(dataListener);
			}
		}
	}

	private void handleInsert(E e, boolean ok, DataListener dataListener) {
		if (!ok) {
			Log.e("Provider", "failed to insert " + e);
		} else {
			data.add(e);
			if (dataListener != null) {
				callback(dataListener);
			}
		}
	}

	private void handleQuery(ArrayList<E> e, boolean ok,
			DataListener dataListener) {
		boolean added = data.addAll(e);
		if (added && dataListener != null) {
			callback(dataListener);
		} else if (!added) {
			Log.d("Provider", "query nothing changed");
		}
	}

	private void handleUpdate(E e, boolean ok, DataListener dataListener) {
		if (!ok) {
			Log.e("Provider", "failed to update " + e);
		} else {
			// Search by id
			for (E d : data) {
				if (e.getId() == d.getId()) {
					data.remove(d);
					d.updateFrom(e);
					data.add(d);
					break;
				}
			}
			if (dataListener != null) {
				callback(dataListener);
			}
		}

	}

	private void runLocalService(E e, DataListener dataListener,
			Messenger messenger, Request request) {
		if (dataListener != null) {
			// Local
			Intent intent = new Intent(dataListener, getLocalServiceClass());
			intent.putExtra(ServiceBase.REQUEST, request);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			if (e != null) {
				intent.putExtra(getDataFieldName(), e);
			}
			dataListener.startService(intent);
		}
	}

}
