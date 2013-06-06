package strength.history.data.provider;

import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.service.local.LocalWorkoutService;
import strength.history.data.structure.Workout;

public class WorkoutProvider extends Provider<Workout> {
	public interface Events {
		public void deleteCallback(Workout e, boolean ok);

		public void insertCallback(Workout e, boolean ok);

		public void updateCallback(Workout old, Workout e, boolean ok);

		public void workoutQueryCallback(Collection<Workout> e, boolean done);
	}

	public interface Provides {
		public void delete(Workout e, Context context);

		public void insert(Workout e, Context context);

		public void queryWorkout(Context context);

		public void stop(Workout e, Context context);

		public void update(Workout e, Context context);
	}

	private LinkedHashSet<Events> eventListeners = new LinkedHashSet<Events>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Events) {
			Events e = (Events) object;
			e.workoutQueryCallback(data, false); // Initial values
			eventListeners.add(e);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Events) {
			eventListeners.remove(object);
		}
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWorkoutService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWorkoutService.WORKOUT;
	}

	@Override
	protected void deleteCallback(Workout e, boolean ok) {
		for (Events t : eventListeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Workout e, boolean ok) {
		for (Events t : eventListeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<Workout> e, boolean done) {
		for (Events t : eventListeners) {
			t.workoutQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Workout old, Workout e, boolean ok) {
		for (Events t : eventListeners) {
			t.updateCallback(old, e, ok);
		}
	}
}
