package strength.history.data.provider;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalWeightService;
import strength.history.data.structure.Weight;

/**
 * Provides data mappings for the weight service
 */
public class WeightProvider extends Provider<Weight> {
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
		public void delete(Weight e);

		/**
		 * Inserts item
		 * 
		 * @param e
		 *            Item to insert
		 */
		public void insert(Weight e);

		/**
		 * Gets all items
		 * 
		 * @param e
		 *            Unused, pass null
		 */
		public void query(Weight e);

		/**
		 * Updates item
		 * 
		 * @param e
		 *            Item to update
		 */
		public void update(Weight e);
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWeightService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWeightService.WEIGHT;
	}

	@Override
	protected void callback(DataListener dataListener) {
		dataListener.weightCallback(data);
	}
}
