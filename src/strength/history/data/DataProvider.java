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
	private Messenger mMessenger = new Messenger(this);

	private ExerciseProvider mExerciseProvider = new ExerciseProvider();
	private WeightProvider mWeightProvider = new WeightProvider();
	private WorkoutProvider mWorkoutProvider = new WorkoutProvider();
	private WorkoutDataProvider mWorkoutDataProvider = new WorkoutDataProvider();

	public void addListeners(Object object) {
		mExerciseProvider.tryAddListener(object);
		mWeightProvider.tryAddListener(object);
		mWorkoutProvider.tryAddListener(object);
		mWorkoutDataProvider.tryAddListener(object);
	}

	public void removeListeners(Object object) {
		mExerciseProvider.tryRemoveListener(object);
		mWeightProvider.tryRemoveListener(object);
		mWorkoutProvider.tryRemoveListener(object);
		mWorkoutDataProvider.tryRemoveListener(object);
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
			mExerciseProvider.handleMessage(request, msg.obj, ok);
			break;
		case WEIGHT:
			mWeightProvider.handleMessage(request, msg.obj, ok);
			break;
		case WORKOUT:
			mWorkoutProvider.handleMessage(request, msg.obj, ok);
			break;
		case WORKOUT_DATA:
			mWorkoutDataProvider.handleMessage(request, msg.obj, ok);
			break;
		}
	}

	@Override
	public void delete(Exercise e, Context context) {
		mExerciseProvider.delete(e, context, mMessenger);
	}

	@Override
	public void insert(Exercise e, Context context) {
		mExerciseProvider.insert(e, context, mMessenger);
	}

	@Override
	public void query(Exercise e, Context context) {
		mExerciseProvider.query(e, context, mMessenger);
	}

	@Override
	public void stop(Exercise e, Context context) {
		mExerciseProvider.stop(e, context, mMessenger);
	}

	@Override
	public void update(Exercise e, Context context) {
		mExerciseProvider.update(e, context, mMessenger);
	}

	@Override
	public void delete(Weight e, Context context) {
		mWeightProvider.delete(e, context, mMessenger);
	}

	@Override
	public void insert(Weight e, Context context) {
		mWeightProvider.insert(e, context, mMessenger);
	}

	@Override
	public void query(Weight e, Context context) {
		mWeightProvider.query(e, context, mMessenger);
	}

	@Override
	public void stop(Weight e, Context context) {
		mWeightProvider.stop(e, context, mMessenger);
	}

	@Override
	public void update(Weight e, Context context) {
		mWeightProvider.update(e, context, mMessenger);
	}

	@Override
	public void delete(Workout e, Context context) {
		mWorkoutProvider.delete(e, context, mMessenger);
	}

	@Override
	public void insert(Workout e, Context context) {
		mWorkoutProvider.insert(e, context, mMessenger);
	}

	@Override
	public void query(Workout e, Context context) {
		mWorkoutProvider.query(e, context, mMessenger);
	}

	@Override
	public void stop(Workout e, Context context) {
		mWorkoutProvider.stop(e, context, mMessenger);
	}

	@Override
	public void update(Workout e, Context context) {
		mWorkoutProvider.update(e, context, mMessenger);
	}

	@Override
	public void delete(WorkoutData e, Context context) {
		mWorkoutDataProvider.delete(e, context, mMessenger);
	}

	@Override
	public void insert(WorkoutData e, Context context) {
		mWorkoutDataProvider.insert(e, context, mMessenger);
	}

	@Override
	public void query(WorkoutData e, Context context) {
		mWorkoutDataProvider.query(e, context, mMessenger);
	}

	@Override
	public void stop(WorkoutData e, Context context) {
		mWorkoutDataProvider.stop(e, context, mMessenger);
	}

	@Override
	public void update(WorkoutData e, Context context) {
		mWorkoutDataProvider.update(e, context, mMessenger);
	}
}
