package strength.history.data.provider;

import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.service.local.LocalExerciseService;
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
	}

	public interface Provides {
		public void delete(Exercise e, Context context);

		public void insert(Exercise e, Context context);

		public void queryExercise(Context context);

		public void stop(Exercise e, Context context);

		public void update(Exercise e, Context context);
	}

	private LinkedHashSet<Events> eventListeners = new LinkedHashSet<Events>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Events) {
			Events e = (Events) object;
			e.exerciseQueryCallback(data, false); // Initial values
			eventListeners.add((Events) object);
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
}
