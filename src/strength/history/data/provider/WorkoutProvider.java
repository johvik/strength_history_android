package strength.history.data.provider;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalWorkoutService;
import strength.history.data.structure.Workout;

public class WorkoutProvider extends Provider<Workout> {
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
		public void delete(Workout e);

		/**
		 * Inserts item
		 * 
		 * @param e
		 *            Item to insert
		 */
		public void insert(Workout e);

		/**
		 * Gets all items
		 * 
		 * @param e
		 *            Unused, pass null
		 */
		public void query(Workout e);

		/**
		 * Updates item
		 * 
		 * @param e
		 *            Item to update
		 */
		public void update(Workout e);
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
	protected void callback(DataListener dataListener) {
		dataListener.workoutCallback(data);
	}
}
