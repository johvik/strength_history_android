package strength.history.data.provider;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.DataListener;
import strength.history.data.service.ServiceBase;
import strength.history.data.service.ServiceBase.Request;
import strength.history.data.structure.SyncBase;

/**
 * Base class for providers
 * 
 * @param <E>
 */
public abstract class Provider<E extends SyncBase<E>> {
	protected final TreeSet<E> data = new TreeSet<E>();

	/**
	 * Handles a request
	 * 
	 * @param request
	 *            Type of request
	 * @param object
	 *            Object from the message
	 * @param ok
	 *            True if successful
	 */
	public void handleMessage(Request request, Object object, boolean ok) {
		switch (request) {
		case DELETE: {
			@SuppressWarnings("unchecked")
			E e = (E) object;
			if (ok) {
				data.remove(e);
			} else {
				Log.e("Provider", "failed to delete " + e);
			}
			deleteCallback(e, ok);
			break;
		}
		case INSERT: {
			@SuppressWarnings("unchecked")
			E e = (E) object;
			if (ok) {
				data.add(e);
			} else {
				Log.e("Provider", "failed to insert " + e);
			}
			insertCallback(e, ok);
			break;
		}
		case QUERY: {
			@SuppressWarnings("unchecked")
			ArrayList<E> e = (ArrayList<E>) object;
			boolean added = data.addAll(e);
			if (!added) {
				Log.d("Provider", "query nothing changed");
			}
			queryCallback(e, ok);
			break;
		}
		case UPDATE: {
			@SuppressWarnings("unchecked")
			E e = (E) object;
			if (ok) {
				// Search by id
				for (E d : data) {
					if (e.getId() == d.getId()) {
						data.remove(d);
						d.updateFrom(e);
						data.add(d);
						break;
					}
				}
			} else {
				Log.e("Provider", "failed to update " + e);
			}
			updateCallback(e, ok);
			break;
		}
		}
	}

	/**
	 * Retrieves the data
	 * 
	 * @return The data
	 */
	public final Iterable<E> get() {
		return data;
	}

	/**
	 * Deletes item
	 * 
	 * @param e
	 *            Item to delete
	 * @param context
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void delete(E e, Context context, Messenger messenger) {
		runLocalService(e, context, messenger, Request.DELETE);
	}

	/**
	 * Inserts item
	 * 
	 * @param e
	 *            Item to insert
	 * @param context
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void insert(E e, Context context, Messenger messenger) {
		runLocalService(e, context, messenger, Request.INSERT);
	}

	/**
	 * Gets all items
	 * 
	 * @param e
	 *            Not used, pass null
	 * @param context
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void query(E e, Context context, Messenger messenger) {
		runLocalService(e, context, messenger, Request.QUERY);
	}

	/**
	 * Updates item
	 * 
	 * @param e
	 *            Item to update
	 * @param context
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void update(E e, Context context, Messenger messenger) {
		runLocalService(e, context, messenger, Request.UPDATE);
	}

	public abstract void tryAddListener(DataListener dataListener);

	public abstract void tryRemoveListener(DataListener dataListener);

	protected abstract Class<?> getLocalServiceClass();

	protected abstract String getDataFieldName();

	protected abstract void deleteCallback(E e, boolean ok);

	protected abstract void insertCallback(E e, boolean ok);

	protected abstract void queryCallback(ArrayList<E> e, boolean ok);

	protected abstract void updateCallback(E e, boolean ok);

	private void runLocalService(E e, Context context, Messenger messenger,
			Request request) {
		if (context != null) {
			// Local
			Intent intent = new Intent(context, getLocalServiceClass());
			intent.putExtra(ServiceBase.REQUEST, request);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			if (e != null) {
				intent.putExtra(getDataFieldName(), e);
			}
			context.startService(intent);
		}
	}
}
