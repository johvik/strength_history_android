package strength.history.data.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.provider.WeightProvider.Events.Latest;
import strength.history.data.service.local.LocalWeightService;
import strength.history.data.service.local.LocalWeightService.Request;
import strength.history.data.structure.Weight;

/**
 * Provides data mappings for the weight service
 */
public class WeightProvider extends Provider<Weight> {
	public interface Events {
		public void deleteCallback(Weight e, boolean ok);

		public void insertCallback(Weight e, boolean ok);

		public void updateCallback(Weight old, Weight e, boolean ok);

		public void weightQueryCallback(Collection<Weight> e, boolean done);

		public interface Latest {
			public void latestCallback(Weight e, boolean ok);
		}
	}

	public interface Provides {
		public void delete(Weight e, Context context);

		public void insert(Weight e, Context context);

		public void latestWeight(Context context);

		public void queryWeight(Context context);

		public void stop(Weight e, Context context);

		public void update(Weight e, Context context);
	}

	private LinkedHashSet<Events> eventListeners = new LinkedHashSet<Events>();
	private LinkedHashSet<Latest> latestListeners = new LinkedHashSet<Latest>();

	private Weight latestCache = null;

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Events) {
			eventListeners.add((Events) object);
		}
		if (object instanceof Latest) {
			latestListeners.add((Latest) object);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Events) {
			eventListeners.remove(object);
		}
		if (object instanceof Latest) {
			latestListeners.remove(object);
		}
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWeightService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWeightService.WEIGHT;
	}

	@Override
	protected void deleteCallback(Weight e, boolean ok) {
		for (Events t : eventListeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Weight e, boolean ok) {
		for (Events t : eventListeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<Weight> e, boolean done) {
		for (Events t : eventListeners) {
			t.weightQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Weight old, Weight e, boolean ok) {
		for (Events t : eventListeners) {
			t.updateCallback(old, e, ok);
		}
	}

	private void latestCallback(Weight e, boolean ok) {
		for (Latest t : latestListeners) {
			t.latestCallback(e, ok);
		}
	}

	public final void latest(Context context, Messenger messenger) {
		if (latestCache == null) {
			runLocalService(null, context, messenger, Request.LATEST.ordinal());
		} else {
			latestCallback(latestCache, true);
		}
	}

	@Override
	protected void onPurge() {
		super.onPurge();
		latestCache = null;
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
	protected int getPurgeArg() {
		return Request.PURGE.ordinal();
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
	@Override
	public void handleMessage(int requestId, Object object, boolean ok) {
		Request request = Request.parse(requestId);
		switch (request) {
		case DELETE: {
			Weight e = (Weight) object;
			if (ok) {
				data.remove(e);
				if (latestCache != null) {
					if (latestCache.getId() == e.getId()) {
						latestCache = null;
					}
				}
			} else {
				Log.e("WeightProvider", "failed to delete " + e);
			}
			deleteCallback(e, ok);
			break;
		}
		case INSERT: {
			Weight e = (Weight) object;
			if (ok) {
				data.add(e);
				if (latestCache != null) {
					if (latestCache.getTime() <= e.getTime()) {
						latestCache = e;
					}
				} else {
					latestCache = e;
				}
			} else {
				Log.e("WeightProvider", "failed to insert " + e);
			}
			insertCallback(e, ok);
			break;
		}
		case LATEST: {
			Weight e = (Weight) object;
			if (!ok) {
				Log.e("WeightProvider", "failed to get latest " + e);
			}
			latestCache = e;
			latestCallback(e, ok);
			break;
		}
		case PURGE: {
			// Should never happen
			break;
		}
		case QUERY: {
			@SuppressWarnings("unchecked")
			ArrayList<Weight> e = (ArrayList<Weight>) object;
			boolean added = data.addAll(e);
			if (!added) {
				Log.d("WeightProvider", "query nothing changed");
			}
			if (ok) {
				loaded = true;
				Log.d("WeightProvider", "query done");
			}
			queryCallback(e, ok);
			break;
		}
		case STOP: {
			// Do nothing (see ServiceBase.onStartCommand)
			break;
		}
		case UPDATE: {
			Weight e = (Weight) object;
			Weight old = null;
			long id = e.getId();
			// Search by id
			for (Weight d : data) {
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
				if (latestCache != null) {
					if (latestCache.getId() == e.getId()) {
						if (latestCache.getTime() > e.getTime()) {
							latestCache = null;
						} else {
							latestCache = e;
						}
					} else if (latestCache.getTime() <= e.getTime()) {
						latestCache = e;
					}
				} else {
					latestCache = e;
				}
			} else {
				Log.e("WeightProvider", "failed to update " + e);
			}
			// return the old one if failed
			// but if old is null return e
			updateCallback(old, e, ok);
			break;
		}
		}
	}
}
