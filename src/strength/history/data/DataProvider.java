package strength.history.data;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;

import android.os.Messenger;

/**
 * Combines several data providers into one
 */
public class DataProvider implements ExerciseProvider.Provides,
		WeightProvider.Provides {
	/**
	 * Interface that contains callback events
	 */
	public interface Events {
		/**
		 * Called when data was changed
		 * 
		 * @param data
		 *            The new data
		 */
		public void exerciseCallback(Iterable<Exercise> data);

		/**
		 * Called when data was changed
		 * 
		 * @param data
		 *            The new data
		 */
		public void weightCallback(Iterable<Weight> data);
	}

	private Messenger messenger = new Messenger(new MessageHandler(this));
	private ExerciseProvider exerciseProvider = new ExerciseProvider();
	private WeightProvider weightProvider = new WeightProvider();
	private DataListener dataListener = null;

	/**
	 * Changes the listener for callback
	 * 
	 * @param dataListener
	 *            The new listener, may be null
	 */
	public void setListener(DataListener dataListener) {
		this.dataListener = dataListener;
		if (dataListener != null) {
			dataListener.exerciseCallback(exerciseProvider.get());
			dataListener.weightCallback(weightProvider.get());
		}
	}

	/**
	 * @return The data listener
	 */
	public DataListener getDataListener() {
		return dataListener;
	}

	/**
	 * @return The provider
	 */
	public ExerciseProvider getExerciseProvider() {
		return exerciseProvider;
	}

	/**
	 * @return The provider
	 */
	public WeightProvider getWeightProvider() {
		return weightProvider;
	}

	@Override
	public void delete(Exercise e) {
		exerciseProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Exercise e) {
		exerciseProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Exercise e) {
		exerciseProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Exercise e) {
		exerciseProvider.update(e, dataListener, messenger);
	}

	@Override
	public void delete(Weight e) {
		weightProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Weight e) {
		weightProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Weight e) {
		weightProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Weight e) {
		weightProvider.update(e, dataListener, messenger);
	}
}
