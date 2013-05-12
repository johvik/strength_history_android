package strength.history.data.provider;

import java.util.ArrayList;

import strength.history.data.service.local.LocalWorkoutDataService;
import strength.history.data.structure.WorkoutData;

public class WorkoutDataProvider<T extends WorkoutDataProvider.Events> extends
		Provider<WorkoutData> {
	public interface Events {
		public void deleteCallback(WorkoutData e, boolean ok);

		public void insertCallback(WorkoutData e, boolean ok);

		public void workoutDataQueryCallback(ArrayList<WorkoutData> e,
				boolean ok);

		public void updateCallback(WorkoutData e, boolean ok);
	}

	public interface Provides {
		public void delete(WorkoutData e);

		public void insert(WorkoutData e);

		public void query(WorkoutData e);

		public void update(WorkoutData e);
	}

	private T t;

	public WorkoutDataProvider(T t) {
		this.t = t;
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
		t.deleteCallback(e, ok);
	}

	@Override
	protected void insertCallback(WorkoutData e, boolean ok) {
		t.insertCallback(e, ok);
	}

	@Override
	protected void queryCallback(ArrayList<WorkoutData> e, boolean ok) {
		t.workoutDataQueryCallback(e, ok);
	}

	@Override
	protected void updateCallback(WorkoutData e, boolean ok) {
		t.updateCallback(e, ok);
	}
}
