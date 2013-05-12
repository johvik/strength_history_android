package strength.history.data.provider;

import java.util.ArrayList;

import strength.history.data.service.local.LocalWorkoutService;
import strength.history.data.structure.Workout;

public class WorkoutProvider<T extends WorkoutProvider.Events> extends
		Provider<Workout> {
	public interface Events {
		public void deleteCallback(Workout e, boolean ok);

		public void insertCallback(Workout e, boolean ok);

		public void workoutQueryCallback(ArrayList<Workout> e, boolean ok);

		public void updateCallback(Workout e, boolean ok);
	}

	public interface Provides {
		public void delete(Workout e);

		public void insert(Workout e);

		public void query(Workout e);

		public void update(Workout e);
	}

	private T t;

	public WorkoutProvider(T t) {
		this.t = t;
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
		t.deleteCallback(e, ok);
	}

	@Override
	protected void insertCallback(Workout e, boolean ok) {
		t.insertCallback(e, ok);
	}

	@Override
	protected void queryCallback(ArrayList<Workout> e, boolean ok) {
		t.workoutQueryCallback(e, ok);
	}

	@Override
	protected void updateCallback(Workout e, boolean ok) {
		t.updateCallback(e, ok);
	}
}
