package strength.history.data.provider;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalWorkoutDataService;
import strength.history.data.structure.WorkoutData;

public class WorkoutDataProvider extends Provider<WorkoutData> {
	public interface Events {
		public void deleteCallback(WorkoutData e, boolean ok);

		public void insertCallback(WorkoutData e, boolean ok);

		public void workoutDataQueryCallback(ArrayList<WorkoutData> e,
				boolean done);

		public void updateCallback(WorkoutData e, boolean ok);
	}

	public interface Provides {
		public void delete(WorkoutData e, Context context);

		public void insert(WorkoutData e, Context context);

		public void query(WorkoutData e, Context context);

		public void stop(WorkoutData e, Context context);

		public void update(WorkoutData e, Context context);
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
		return LocalWorkoutDataService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWorkoutDataService.WORKOUT_DATA;
	}

	@Override
	protected void deleteCallback(WorkoutData e, boolean ok) {
		for (Events t : listeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(WorkoutData e, boolean ok) {
		for (Events t : listeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(ArrayList<WorkoutData> e, boolean done) {
		for (Events t : listeners) {
			t.workoutDataQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(WorkoutData e, boolean ok) {
		for (Events t : listeners) {
			t.updateCallback(e, ok);
		}
	}
}