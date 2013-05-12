package strength.history.data;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.service.ServiceBase;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Combines several data providers into one
 */
public class DataProvider extends Handler implements ExerciseProvider.Provides,
		WeightProvider.Provides, WorkoutProvider.Provides,
		WorkoutDataProvider.Provides {
	private Messenger messenger = new Messenger(this);

	private ExerciseProvider exerciseProvider = new ExerciseProvider();
	private WeightProvider weightProvider = new WeightProvider();
	private WorkoutProvider workoutProvider = new WorkoutProvider();
	private WorkoutDataProvider workoutDataProvider = new WorkoutDataProvider();

	public void addListeners(DataListener dataListener) {
		exerciseProvider.tryAddListener(dataListener);
		weightProvider.tryAddListener(dataListener);
		workoutProvider.tryAddListener(dataListener);
		workoutDataProvider.tryAddListener(dataListener);
	}

	public void removeListeners(DataListener dataListener) {
		exerciseProvider.tryRemoveListener(dataListener);
		weightProvider.tryRemoveListener(dataListener);
		workoutProvider.tryRemoveListener(dataListener);
		workoutDataProvider.tryRemoveListener(dataListener);
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
			exerciseProvider.handleMessage(request, msg.obj, ok);
			break;
		case WEIGHT:
			weightProvider.handleMessage(request, msg.obj, ok);
			break;
		case WORKOUT:
			workoutProvider.handleMessage(request, msg.obj, ok);
			break;
		case WORKOUT_DATA:
			workoutDataProvider.handleMessage(request, msg.obj, ok);
			break;
		}
	}

	@Override
	public void delete(Exercise e, Context context) {
		exerciseProvider.delete(e, context, messenger);
	}

	@Override
	public void insert(Exercise e, Context context) {
		exerciseProvider.insert(e, context, messenger);
	}

	@Override
	public void query(Exercise e, Context context) {
		exerciseProvider.query(e, context, messenger);
	}

	@Override
	public void update(Exercise e, Context context) {
		exerciseProvider.update(e, context, messenger);
	}

	@Override
	public void delete(Weight e, Context context) {
		weightProvider.delete(e, context, messenger);
	}

	@Override
	public void insert(Weight e, Context context) {
		weightProvider.insert(e, context, messenger);
	}

	@Override
	public void query(Weight e, Context context) {
		weightProvider.query(e, context, messenger);
	}

	@Override
	public void update(Weight e, Context context) {
		weightProvider.update(e, context, messenger);
	}

	@Override
	public void delete(Workout e, Context context) {
		workoutProvider.delete(e, context, messenger);
	}

	@Override
	public void insert(Workout e, Context context) {
		workoutProvider.insert(e, context, messenger);
	}

	@Override
	public void query(Workout e, Context context) {
		workoutProvider.query(e, context, messenger);
	}

	@Override
	public void update(Workout e, Context context) {
		workoutProvider.update(e, context, messenger);
	}

	@Override
	public void delete(WorkoutData e, Context context) {
		workoutDataProvider.delete(e, context, messenger);
	}

	@Override
	public void insert(WorkoutData e, Context context) {
		workoutDataProvider.insert(e, context, messenger);
	}

	@Override
	public void query(WorkoutData e, Context context) {
		workoutDataProvider.query(e, context, messenger);
	}

	@Override
	public void update(WorkoutData e, Context context) {
		workoutDataProvider.update(e, context, messenger);
	}
}
