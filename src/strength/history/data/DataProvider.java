package strength.history.data;

import strength.history.data.structure.Weight;

import android.os.Messenger;

/**
 * Combines several data providers into one
 */
public class DataProvider implements WeightProvider.Provides {
	/**
	 * Interface that contains callback events
	 */
	public interface Events extends WeightProvider.Events {
	}

	private Messenger messenger = new Messenger(new MessageHandler(this));
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
			dataListener.callback(weightProvider.get());
		}
	}

	/**
	 * @return The data listener
	 */
	public DataListener getDataListener() {
		return dataListener;
	}

	/**
	 * @return The weight provider
	 */
	public WeightProvider getWeightProvider() {
		return weightProvider;
	}

	@Override
	public void delete(Weight e) {
		WeightProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Weight e) {
		WeightProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Weight e) {
		WeightProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Weight e) {
		WeightProvider.update(e, dataListener, messenger);
	}
}
