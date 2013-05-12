package strength.history.data.provider;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalExerciseService;
import strength.history.data.structure.Exercise;

/**
 * Provides data mappings for the exercise service
 */
public class ExerciseProvider extends Provider<Exercise> {
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
		public void delete(Exercise e);

		/**
		 * Inserts item
		 * 
		 * @param e
		 *            Item to insert
		 */
		public void insert(Exercise e);

		/**
		 * Gets all items
		 * 
		 * @param e
		 *            Unused, pass null
		 */
		public void query(Exercise e);

		/**
		 * Updates item
		 * 
		 * @param e
		 *            Item to update
		 */
		public void update(Exercise e);
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
	protected void callback(DataListener dataListener) {
		dataListener.exerciseCallback(data);
	}
}
