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
 * Base class for providers. Remember to override getXXXArg and handleMessage if
 * adding more request types!
 * 
 * @param <E>
 */
public abstract class Provider<E extends SyncBase<E>> {
	protected final HashSet<E> data = new HashSet<E>();
	protected boolean loaded = false;

	@SuppressWarnings("static-method")
	protected int getDeleteArg() {
		return Request.DELETE.ordinal();
	}

	@SuppressWarnings("static-method")
	protected int getInsertArg() {
		return Request.INSERT.ordinal();
	}

	@SuppressWarnings("static-method")
	protected int getPurgeArg() {
		return Request.PURGE.ordinal();
	}

	@SuppressWarnings("static-method")
	protected int getQueryArg() {
		return Request.QUERY.ordinal();
	}

	@SuppressWarnings("static-method")
	protected int getStopArg() {
		return Request.STOP.ordinal();
	}

	@SuppressWarnings("static-method")
	protected int getUpdateArg() {
		return Request.UPDATE.ordinal();
	}

	/**
	 * Handles a request
	 * 
	 * @param requestId
	 *            Type of request
	 * @param object
	 *            Object from the message
	 * @param ok
	 *            True if successful
	 */
	public void handleMessage(int requestId, Object object, boolean ok) {
		Request request = Request.parse(requestId);
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
		case PURGE: {
			// Should never happen
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
				loaded = true;
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
					data.remove(old);
					data.add(e);
				}
			} else {
				Log.e("Provider", "failed to update " + e);
			}
			// return the old one if failed
			// but if old is null return e
			updateCallback(old, e, ok);
			break;
		}
		}
	}

	public final void purge(Context context, Messenger messenger) {
		data.clear();
		loaded = false;
		runLocalService(null, context, messenger, getPurgeArg());
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
		runLocalService(e, context, messenger, getDeleteArg());
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
		runLocalService(e, context, messenger, getInsertArg());
	}

	/**
	 * Gets all items
	 * 
	 * @param context
	 * @param messenger
	 *            Messenger for callback
	 */
	public final void query(Context context, Messenger messenger) {
		if (!loaded) {
			runLocalService(null, context, messenger, getQueryArg());
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
		runLocalService(e, context, messenger, getStopArg());
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
		runLocalService(e, context, messenger, getUpdateArg());
	}

	public abstract void tryAddListener(Object object);

	public abstract void tryRemoveListener(Object object);

	protected abstract Class<?> getLocalServiceClass();

	protected abstract String getDataFieldName();

	protected abstract void deleteCallback(E e, boolean ok);

	protected abstract void insertCallback(E e, boolean ok);

	protected abstract void queryCallback(Collection<E> e, boolean done);

	protected abstract void updateCallback(E old, E e, boolean ok);

	protected final void runLocalService(E e, Context context,
			Messenger messenger, int request) {
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

	protected final void runLocalService(long queryId, Context context,
			Messenger messenger, int request) {
		if (context != null) {
			// Local
			Intent intent = new Intent(context, getLocalServiceClass());
			intent.putExtra(ServiceBase.REQUEST, request);
			intent.putExtra(ServiceBase.MESSENGER, messenger);
			intent.putExtra(getDataFieldName(), queryId);
			context.startService(intent);
		}
	}
}
