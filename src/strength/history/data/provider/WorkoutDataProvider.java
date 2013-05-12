package strength.history.data.provider;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalWorkoutDataService;
import strength.history.data.structure.WorkoutData;

public class WorkoutDataProvider extends Provider<WorkoutData> {
	/**
	 * Interface of methods that this service provides
	 */
	public interface Provides {
		/**
		 * Deletes item
		 * 
		 * @param e
		 *            Item to delete
		 */
		public void delete(WorkoutData e);

		/**
		 * Inserts item
		 * 
		 * @param e
		 *            Item to insert
		 */
		public void insert(WorkoutData e);

		/**
		 * Gets all items
		 * 
		 * @param e
		 *            Unused, pass null
		 */
		public void query(WorkoutData e);

		/**
		 * Updates item
		 * 
		 * @param e
		 *            Item to update
		 */
		public void update(WorkoutData e);
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
	protected void callback(DataListener dataListener) {
		dataListener.workoutDataCallback(data);
	}

}
