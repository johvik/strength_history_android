package strength.history.data.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;
import android.os.Messenger;
import android.util.Log;

import strength.history.data.provider.WorkoutDataProvider.Events.Edit;
import strength.history.data.provider.WorkoutDataProvider.Events.LatestExerciseData;
import strength.history.data.provider.WorkoutDataProvider.Events.LatestWorkoutData;
import strength.history.data.provider.WorkoutDataProvider.Events.Query;
import strength.history.data.service.local.LocalWorkoutDataService;
import strength.history.data.service.local.LocalWorkoutDataService.Request;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.WorkoutData;

public class WorkoutDataProvider extends Provider<WorkoutData> {
	public interface Events {
		public interface Edit {
			public void deleteCallback(WorkoutData e, boolean ok);

			public void insertCallback(WorkoutData e, boolean ok);

			public void updateCallback(WorkoutData old, WorkoutData e,
					boolean ok);
		}

		public interface LatestExerciseData {
			public void latestCallback(ExerciseData e, boolean ok);
		}

		public interface LatestWorkoutData {
			public void latestCallback(WorkoutData e, boolean ok);
		}

		public interface Query {
			public void workoutDataQueryCallback(Collection<WorkoutData> e,
					boolean done);
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

	private LinkedHashSet<Edit> editListeners = new LinkedHashSet<Edit>();
	private LinkedHashSet<LatestExerciseData> latestExerciseDataListeners = new LinkedHashSet<LatestExerciseData>();
	private LinkedHashSet<LatestWorkoutData> latestWorkoutDataListeners = new LinkedHashSet<LatestWorkoutData>();
	private LinkedHashSet<Query> queryListeners = new LinkedHashSet<Query>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Edit) {
			editListeners.add((Edit) object);
		}
		if (object instanceof LatestExerciseData) {
			latestExerciseDataListeners.add((LatestExerciseData) object);
		}
		if (object instanceof LatestWorkoutData) {
			latestWorkoutDataListeners.add((LatestWorkoutData) object);
		}
		if (object instanceof Query) {
			Query e = (Query) object;
			e.workoutDataQueryCallback(data, false); // Initial values
			queryListeners.add(e);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Edit) {
			editListeners.remove(object);
		}
		if (object instanceof LatestExerciseData) {
			latestExerciseDataListeners.remove(object);
		}
		if (object instanceof LatestWorkoutData) {
			latestWorkoutDataListeners.remove(object);
		}
		if (object instanceof Query) {
			queryListeners.remove(object);
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
		for (Edit t : editListeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(WorkoutData e, boolean ok) {
		for (Edit t : editListeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<WorkoutData> e, boolean done) {
		for (Query t : queryListeners) {
			t.workoutDataQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(WorkoutData old, WorkoutData e, boolean ok) {
		for (Edit t : editListeners) {
			t.updateCallback(old, e, ok);
		}
	}

	private void latestCallback(ExerciseData e, boolean ok) {
		for (LatestExerciseData t : latestExerciseDataListeners) {
			t.latestCallback(e, ok);
		}
	}

	private void latestCallback(WorkoutData e, boolean ok) {
		for (LatestWorkoutData t : latestWorkoutDataListeners) {
			t.latestCallback(e, ok);
		}
	}

	public final void latestExerciseData(long exerciseId, Context context,
			Messenger messenger) {
		// TODO Cache?
		runLocalService(exerciseId, context, messenger,
				Request.LATEST_EXERCISE_DATA.ordinal());
	}

	public final void latestWorkoutData(long workoutId, Context context,
			Messenger messenger) {
		// TODO Cache?
		runLocalService(workoutId, context, messenger,
				Request.LATEST_WORKOUT_DATA.ordinal());
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
			} else {
				Log.e("WorkoutDataProvider", "failed to insert " + e);
			}
			insertCallback(e, ok);
			break;
		}
		case LATEST_EXERCISE_DATA: {
			ExerciseData e = (ExerciseData) object;
			if (!ok) {
				Log.e("WorkoutDataProvider",
						"failed to get latest exercisedata " + e);
			}
			latestCallback(e, ok);
			break;
		}
		case LATEST_WORKOUT_DATA: {
			WorkoutData e = (WorkoutData) object;
			if (!ok) {
				Log.e("WorkoutDataProvider",
						"failed to get latest workoutdata " + e);
			}
			latestCallback(e, ok);
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
