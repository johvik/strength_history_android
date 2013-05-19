package strength.history.data.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.Intent;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.service.ServiceBase;
import strength.history.data.service.ServiceBase.Request;
import strength.history.data.structure.SyncBase;

/**
 * Base class for providers
 * 
 * @param <E>
 */
public abstract class Provider<E extends SyncBase<E>> {
	protected final HashSet<E> data = new HashSet<E>();
	private boolean loaded = false;

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
			if (ok) {
				loaded = true; // TODO Verify that this works...
				Log.d("Provider", "query done");
			}
			queryCallback(e, ok);
			break;
		}
		case STOP: {
			// Do nothing (see ServiceBase.onStartCommand)
			break;
		}
		case UPDATE: {
			@SuppressWarnings("unchecked")
			E e = (E) object;
			E old = null;
			long id = e.getId();
			// Search by id
			for (E d : data) {
				if (d.getId() == id) {
					old = d;
					break;
				}
			}
			if (ok) {
				if (old != null) {
					old.updateFrom(e);
				}
			} else {
				Log.e("Provider", "failed to update " + e);
			}
			// return the old one if failed
			// but if old is null return e
			updateCallback(ok || old == null ? e : old, ok);
			break;
		}
		}
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
		if (!loaded) {
			// TODO Take advantage of that some may have been loaded?
			runLocalService(e, context, messenger, Request.QUERY);
		} else {
			queryCallback(data, true);
		}
	}

	/**
	 * Stops all ongoing queries
	 * 
	 * @param e
	 *            Not used, pass null
	 * @param context
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void stop(E e, Context context, Messenger messenger) {
		runLocalService(e, context, messenger, Request.STOP);
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

	public abstract void tryAddListener(Object object);

	public abstract void tryRemoveListener(Object object);

	protected abstract Class<?> getLocalServiceClass();

	protected abstract String getDataFieldName();

	protected abstract void deleteCallback(E e, boolean ok);

	protected abstract void insertCallback(E e, boolean ok);

	protected abstract void queryCallback(Collection<E> e, boolean done);

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
