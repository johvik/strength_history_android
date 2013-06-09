package strength.history.data.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

import android.content.Context;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.provider.WorkoutDataProvider.Events.LatestExerciseData;
import strength.history.data.provider.WorkoutDataProvider.Events.LatestWorkoutData;
import strength.history.data.service.local.LocalWorkoutDataService;
import strength.history.data.service.local.LocalWorkoutDataService.Request;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.WorkoutData;

public class WorkoutDataProvider extends Provider<WorkoutData> {
	public interface Events {
		public void deleteCallback(WorkoutData e, boolean ok);

		public void insertCallback(WorkoutData e, boolean ok);

		public void updateCallback(WorkoutData old, WorkoutData e, boolean ok);

		public void workoutDataQueryCallback(Collection<WorkoutData> e,
				boolean done);

		public interface LatestExerciseData {
			public void latestCallback(ExerciseData e, long exerciseId,
					boolean ok);
		}

		public interface LatestWorkoutData {
			public void latestCallback(WorkoutData e, long workoutId, boolean ok);
		}
	}

	public interface Provides {
		public void delete(WorkoutData e, Context context);

		public void insert(WorkoutData e, Context context);

		public void latestExerciseData(long exerciseId, Context context);

		public void latestWorkoutData(long workoutId, Context context);

		public void queryWorkoutData(Context context);

		public void stop(WorkoutData e, Context context);

		public void update(WorkoutData e, Context context);
	}

	private LinkedHashSet<Events> eventListeners = new LinkedHashSet<Events>();
	private LinkedHashSet<LatestExerciseData> latestExerciseDataListeners = new LinkedHashSet<LatestExerciseData>();
	private LinkedHashSet<LatestWorkoutData> latestWorkoutDataListeners = new LinkedHashSet<LatestWorkoutData>();

	private HashMap<Long, ExerciseData> latestExerciseDataCache = new HashMap<Long, ExerciseData>();
	private HashMap<Long, WorkoutData> latestWorkoutDataCache = new HashMap<Long, WorkoutData>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Events) {
			eventListeners.add((Events) object);
		}
		if (object instanceof LatestExerciseData) {
			latestExerciseDataListeners.add((LatestExerciseData) object);
		}
		if (object instanceof LatestWorkoutData) {
			latestWorkoutDataListeners.add((LatestWorkoutData) object);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Events) {
			eventListeners.remove(object);
		}
		if (object instanceof LatestExerciseData) {
			latestExerciseDataListeners.remove(object);
		}
		if (object instanceof LatestWorkoutData) {
			latestWorkoutDataListeners.remove(object);
		}
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWorkoutDataService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWorkoutDataService.WORKOUT_DATA;
	}

	@Override
	protected void deleteCallback(WorkoutData e, boolean ok) {
		for (Events t : eventListeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(WorkoutData e, boolean ok) {
		for (Events t : eventListeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<WorkoutData> e, boolean done) {
		for (Events t : eventListeners) {
			t.workoutDataQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(WorkoutData old, WorkoutData e, boolean ok) {
		for (Events t : eventListeners) {
			t.updateCallback(old, e, ok);
		}
	}

	private void latestCallback(ExerciseData e, long exerciseId, boolean ok) {
		for (LatestExerciseData t : latestExerciseDataListeners) {
			t.latestCallback(e, exerciseId, ok);
		}
	}

	private void latestCallback(WorkoutData e, long workoutId, boolean ok) {
		for (LatestWorkoutData t : latestWorkoutDataListeners) {
			t.latestCallback(e, workoutId, ok);
		}
	}

	public final void latestExerciseData(long exerciseId, Context context,
			Messenger messenger) {
		ExerciseData e = latestExerciseDataCache.get(exerciseId);
		if (e == null) {
			runLocalService(exerciseId, context, messenger,
					Request.LATEST_EXERCISE_DATA.ordinal());
		} else {
			latestCallback(e, e.getExerciseId(), true);
		}
	}

	public final void latestWorkoutData(long workoutId, Context context,
			Messenger messenger) {
		WorkoutData e = latestWorkoutDataCache.get(workoutId);
		if (e == null) {
			runLocalService(workoutId, context, messenger,
					Request.LATEST_WORKOUT_DATA.ordinal());
		} else {
			latestCallback(e, e.getWorkoutId(), true);
		}
	}

	@Override
	protected void onBeforePurge() {
		super.onBeforePurge();
		latestExerciseDataCache.clear();
		latestWorkoutDataCache.clear();
	}

	@Override
	protected void onBeforeDelete(WorkoutData e) {
		if (e != null) {
			latestExerciseDataCache.clear();
			long workoutId = e.getWorkoutId();
			WorkoutData cache = latestWorkoutDataCache.get(workoutId);
			if (cache != null) {
				if (cache.getId() == e.getId()) {
					latestWorkoutDataCache.remove(workoutId);
				}
			}
		}
	}

	@Override
	protected void onBeforeInsert(WorkoutData e) {
		if (e != null) {
			latestExerciseDataCache.clear();
			long workoutId = e.getWorkoutId();
			WorkoutData cache = latestWorkoutDataCache.get(workoutId);
			if (cache != null) {
				latestWorkoutDataCache.remove(workoutId);
			}
		}
	}

	@Override
	protected void onBeforeUpdate(WorkoutData e) {
		if (e != null) {
			latestExerciseDataCache.clear();
			long workoutId = e.getWorkoutId();
			WorkoutData cache = latestWorkoutDataCache.get(workoutId);
			if (cache != null) {
				latestWorkoutDataCache.remove(workoutId);
			}
		}
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
			WorkoutData e = (WorkoutData) object;
			if (ok) {
				data.remove(e);
				latestExerciseDataCache.clear();
				long workoutId = e.getWorkoutId();
				WorkoutData cache = latestWorkoutDataCache.get(workoutId);
				if (cache != null) {
					if (cache.getId() == e.getId()) {
						latestWorkoutDataCache.remove(workoutId);
					}
				}
			} else {
				Log.e("WorkoutDataProvider", "failed to delete " + e);
			}
			deleteCallback(e, ok);
			break;
		}
		case INSERT: {
			WorkoutData e = (WorkoutData) object;
			if (ok) {
				data.add(e);
				latestExerciseDataCache.clear();
				long workoutId = e.getWorkoutId();
				WorkoutData cache = latestWorkoutDataCache.get(workoutId);
				if (cache != null) {
					if (cache.getTime() <= e.getTime()) {
						latestWorkoutDataCache.put(workoutId, e);
					}
				} else {
					latestWorkoutDataCache.put(workoutId, e);
				}
			} else {
				Log.e("WorkoutDataProvider", "failed to insert " + e);
			}
			insertCallback(e, ok);
			break;
		}
		case LATEST_EXERCISE_DATA: {
			if (!ok) {
				long exerciseId = (Long) object;
				Log.e("WorkoutDataProvider",
						"failed to get latest exercisedata " + exerciseId);
				latestCallback((ExerciseData) null, exerciseId, ok);
			} else {
				ExerciseData e = (ExerciseData) object;
				latestExerciseDataCache.put(e.getExerciseId(), e);
				latestCallback(e, e.getExerciseId(), ok);
			}
			break;
		}
		case LATEST_WORKOUT_DATA: {
			if (!ok) {
				long workoutId = (Long) object;
				Log.e("WorkoutDataProvider",
						"failed to get latest workoutdata " + workoutId);
				latestCallback((WorkoutData) null, workoutId, ok);
			} else {
				WorkoutData e = (WorkoutData) object;
				latestWorkoutDataCache.put(e.getWorkoutId(), e);
				latestCallback(e, e.getWorkoutId(), ok);
			}
			break;
		}
		case PURGE: {
			// Should never happen
			break;
		}
		case QUERY: {
			@SuppressWarnings("unchecked")
			ArrayList<WorkoutData> e = (ArrayList<WorkoutData>) object;
			boolean added = data.addAll(e);
			if (!added) {
				Log.d("WorkoutDataProvider", "query nothing changed");
			}
			if (ok) {
				loaded = true;
				Log.d("WorkoutDataProvider", "query done");
			}
			queryCallback(e, ok);
			break;
		}
		case STOP: {
			// Do nothing (see ServiceBase.onStartCommand)
			break;
		}
		case UPDATE: {
			WorkoutData e = (WorkoutData) object;
			WorkoutData old = null;
			long id = e.getId();
			// Search by id
			for (WorkoutData d : data) {
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
				latestExerciseDataCache.clear();
				long workoutId = e.getWorkoutId();
				WorkoutData cache = latestWorkoutDataCache.get(workoutId);
				if (cache != null) {
					if (cache.getId() == e.getId()) {
						if (cache.getTime() > e.getTime()) {
							latestWorkoutDataCache.remove(cache);
						} else {
							latestWorkoutDataCache.put(workoutId, e);
						}
					} else if (cache.getTime() <= e.getTime()) {
						latestWorkoutDataCache.put(workoutId, e);
					}
				} else {
					latestWorkoutDataCache.put(workoutId, e);
				}
			} else {
				Log.e("WorkoutDataProvider", "failed to update " + e);
			}
			// return the old one if failed
			// but if old is null return e
			updateCallback(old, e, ok);
			break;
		}
		}
	}
}
