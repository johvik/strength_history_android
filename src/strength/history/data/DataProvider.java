package strength.history.data;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.service.ServiceBase;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Combines several data providers into one
 */
public class DataProvider extends Handler implements ExerciseProvider.Provides,
		WeightProvider.Provides, WorkoutProvider.Provides {
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

		/**
		 * Called when data was changed
		 * 
		 * @param data
		 *            The new data
		 */
		public void workoutCallback(Iterable<Workout> data);
	}

	private Messenger messenger = new Messenger(this);
	private ExerciseProvider exerciseProvider = new ExerciseProvider();
	private WeightProvider weightProvider = new WeightProvider();
	private WorkoutProvider workoutProvider = new WorkoutProvider();
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
			dataListener.workoutCallback(workoutProvider.get());
		}
	}

	@Override
	public final void handleMessage(Message msg) {
		ServiceBase.Service service = ServiceBase.Service.parse(msg.arg1);
		ServiceBase.Request request = ServiceBase.Request.parse(msg.arg2);
		boolean ok = msg.what == 1;
		Log.d("MessageHandler", "service=" + service + " request=" + request
				+ " ok=" + ok);

		switch (service) {
		case EXERCISE:
			exerciseProvider.handleCallback(request, msg.obj, ok, dataListener);
			break;
		case WEIGHT:
			weightProvider.handleCallback(request, msg.obj, ok, dataListener);
			break;
		case WORKOUT:
			workoutProvider.handleCallback(request, msg.obj, ok, dataListener);
			break;
		}
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

	@Override
	public void delete(Workout e) {
		workoutProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Workout e) {
		workoutProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Workout e) {
		workoutProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Workout e) {
		workoutProvider.update(e, dataListener, messenger);
	}
}
