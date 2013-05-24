package strength.history.data.provider;

import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.provider.ExerciseProvider.Events.Edit;
import strength.history.data.provider.ExerciseProvider.Events.Query;
import strength.history.data.service.local.LocalExerciseService;
import strength.history.data.structure.Exercise;

/**
 * Provides data mappings for the exercise service
 */
public class ExerciseProvider extends Provider<Exercise> {
	public interface Events {
		public interface Edit {
			public void deleteCallback(Exercise e, boolean ok);

			public void insertCallback(Exercise e, boolean ok);

			public void updateCallback(Exercise old, Exercise e, boolean ok);
		}

		public interface Query {
			public void exerciseQueryCallback(Collection<Exercise> e,
					boolean done);
		}
	}

	public interface Provides {
		public void delete(Exercise e, Context context);

		public void insert(Exercise e, Context context);

		public void queryExercise(Context context);

		public void stop(Exercise e, Context context);

		public void update(Exercise e, Context context);
	}

	private LinkedHashSet<Edit> editListeners = new LinkedHashSet<Edit>();
	private LinkedHashSet<Query> queryListeners = new LinkedHashSet<Query>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Edit) {
			editListeners.add((Edit) object);
		}
		if (object instanceof Query) {
			Query e = (Query) object;
			e.exerciseQueryCallback(data, false); // Initial values
			queryListeners.add(e);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Edit) {
			editListeners.remove(object);
		}
		if (object instanceof Query) {
			queryListeners.remove(object);
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
		for (Edit t : editListeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Exercise e, boolean ok) {
		for (Edit t : editListeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(Collection<Exercise> e, boolean done) {
		for (Query t : queryListeners) {
			t.exerciseQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Exercise old, Exercise e, boolean ok) {
		for (Edit t : editListeners) {
			t.updateCallback(old, e, ok);
		}
	}
}
