package strength.history.data.provider;

import java.util.ArrayList;

import strength.history.data.service.local.LocalExerciseService;
import strength.history.data.structure.Exercise;

/**
 * Provides data mappings for the exercise service
 * 
 * @param <T>
 */
public class ExerciseProvider<T extends ExerciseProvider.Events> extends
		Provider<Exercise> {
	public interface Events {
		public void deleteCallback(Exercise e, boolean ok);

		public void insertCallback(Exercise e, boolean ok);

		public void exerciseQueryCallback(ArrayList<Exercise> e, boolean ok);

		public void updateCallback(Exercise e, boolean ok);
	}

	public interface Provides {
		public void delete(Exercise e);

		public void insert(Exercise e);

		public void query(Exercise e);

		public void update(Exercise e);
	}

	private T t;

	public ExerciseProvider(T t) {
		this.t = t;
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
		t.deleteCallback(e, ok);
	}

	@Override
	protected void insertCallback(Exercise e, boolean ok) {
		t.insertCallback(e, ok);
	}

	@Override
	protected void queryCallback(ArrayList<Exercise> e, boolean ok) {
		t.exerciseQueryCallback(e, ok);
	}

	@Override
	protected void updateCallback(Exercise e, boolean ok) {
		t.updateCallback(e, ok);
	}
}
