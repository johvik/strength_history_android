package strength.history.data.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.provider.ExerciseProvider.Events.Defaults;
import strength.history.data.service.local.LocalExerciseService;
import strength.history.data.service.local.LocalExerciseService.Request;
import strength.history.data.structure.Exercise;

/**
 * Provides data mappings for the exercise service
 */
public class ExerciseProvider extends Provider<Exercise> {
	public interface Events {
		public void deleteCallback(Exercise e, boolean ok);

		public void insertCallback(Exercise e, boolean ok);

		public void updateCallback(Exercise old, Exercise e, boolean ok);

		public void exerciseQueryCallback(Collection<Exercise> e, boolean done);

		public interface Defaults {
			public void exerciseCreateDefaultsCallback(Collection<Exercise> e);
		}
	}

	public interface Provides {
		public void createDefaults(Exercise e, Context context);

		public void delete(Exercise e, Context context);

		public void insert(Exercise e, Context context);

		public void queryExercise(Context context);

		public void stop(Exercise e, Context context);

		public void update(Exercise e, Context context);
	}

	private LinkedHashSet<Events> eventListeners = new LinkedHashSet<Events>();
	private LinkedHashSet<Defaults> defaultsListeners = new LinkedHashSet<Defaults>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Events) {
			eventListeners.add((Events) object);
		}
		if (object instanceof Defaults) {
			defaultsListeners.add((Defaults) object);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Events) {
			eventListeners.remove(object);
		}
		if (object instanceof Defaults) {
			defaultsListeners.remove(object);
		}
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalExerciseService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalExerciseService.EXERCISE;
	}

	@Override
	protected void deleteCallback(Exercise e, boolean ok) {
		for (Events t : eventListeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Exercise e, boolean ok) {
		for (Events t : eventListeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<Exercise> e, boolean done) {
		for (Events t : eventListeners) {
			t.exerciseQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Exercise old, Exercise e, boolean ok) {
		for (Events t : eventListeners) {
			t.updateCallback(old, e, ok);
		}
	}

	protected void createDefaultsCallback(Collection<Exercise> e) {
		for (Defaults t : defaultsListeners) {
			t.exerciseCreateDefaultsCallback(e);
		}
		for (Events t : eventListeners) {
			t.exerciseQueryCallback(e, false);
		}
		loaded = false;
	}

	public final void createDefaults(Context context, Messenger messenger) {
		runLocalService(null, context, messenger,
				Request.CREATE_DEFAULTS.ordinal());
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
		case CREATE_DEFAULTS: {
			@SuppressWarnings("unchecked")
			ArrayList<Exercise> e = (ArrayList<Exercise>) object;
			createDefaultsCallback(e);
			break;
		}
		case DELETE: {
			Exercise e = (Exercise) object;
			if (ok) {
				data.remove(e);
			} else {
				Log.e("ExerciseProvider", "failed to delete " + e);
			}
			deleteCallback(e, ok);
			break;
		}
		case INSERT: {
			Exercise e = (Exercise) object;
			if (ok) {
				data.add(e);
			} else {
				Log.e("ExerciseProvider", "failed to insert " + e);
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
			ArrayList<Exercise> e = (ArrayList<Exercise>) object;
			boolean added = data.addAll(e);
			if (!added) {
				Log.d("ExerciseProvider", "query nothing changed");
			}
			if (ok) {
				loaded = true;
				Log.d("ExerciseProvider", "query done");
			}
			queryCallback(e, ok);
			break;
		}
		case STOP: {
			// Do nothing (see ServiceBase.onStartCommand)
			break;
		}
		case UPDATE: {
			Exercise e = (Exercise) object;
			Exercise old = null;
			long id = e.getId();
			// Search by id
			for (Exercise d : data) {
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
				Log.e("ExerciseProvider", "failed to update " + e);
			}
			// return the old one if failed
			// but if old is null return e
			updateCallback(old, e, ok);
			break;
		}
		}
	}
}
