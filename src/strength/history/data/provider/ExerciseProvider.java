package strength.history.data.provider;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalExerciseService;
import strength.history.data.structure.Exercise;

/**
 * Provides data mappings for the exercise service
 */
public class ExerciseProvider extends Provider<Exercise> {
	public interface Events {
		public void deleteCallback(Exercise e, boolean ok);

		public void insertCallback(Exercise e, boolean ok);

		public void exerciseQueryCallback(ArrayList<Exercise> e, boolean done);

		public void updateCallback(Exercise e, boolean ok);
	}

	public interface Provides {
		public void delete(Exercise e, Context context);

		public void insert(Exercise e, Context context);

		public void query(Exercise e, Context context);

		public void stop(Exercise e, Context context);

		public void update(Exercise e, Context context);
	}

	private LinkedHashSet<Events> listeners = new LinkedHashSet<Events>();

	@Override
	public void tryAddListener(DataListener dataListener) {
		if (dataListener instanceof Events) {
			listeners.add((Events) dataListener);
		}
	}

	@Override
	public void tryRemoveListener(DataListener dataListener) {
		if (dataListener instanceof Events) {
			listeners.add((Events) dataListener);
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
		for (Events t : listeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Exercise e, boolean ok) {
		for (Events t : listeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(ArrayList<Exercise> e, boolean done) {
		for (Events t : listeners) {
			t.exerciseQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Exercise e, boolean ok) {
		for (Events t : listeners) {
			t.updateCallback(e, ok);
		}
	}
}
