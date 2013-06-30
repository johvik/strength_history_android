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

	/**
	 * Warning deletes everything!
	 * 
	 * @param context
	 */
	public void purge(Context context) {
		mExerciseProvider.purge(context, mMessenger);
		mWeightProvider.purge(context, mMessenger);
		mWorkoutProvider.purge(context, mMessenger);
		mWorkoutDataProvider.purge(context, mMessenger);
	}

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

	public void clear() {
		mExerciseProvider.clear();
		mWeightProvider.clear();
		mWorkoutProvider.clear();
		mWorkoutDataProvider.clear();
	}

	@Override
	public final void handleMessage(Message msg) {
		ServiceBase.Service service = ServiceBase.Service.parse(msg.arg1);
		boolean ok = msg.what == 1;
		Log.d("MessageHandler", "service=" + service + " request=" + msg.arg2
				+ " ok=" + ok);

		switch (service) {
		case EXERCISE:
			mExerciseProvider.handleMessage(msg.arg2, msg.obj, ok);
			break;
		case WEIGHT:
			mWeightProvider.handleMessage(msg.arg2, msg.obj, ok);
			break;
		case WORKOUT:
			mWorkoutProvider.handleMessage(msg.arg2, msg.obj, ok);
			break;
		case WORKOUT_DATA:
			mWorkoutDataProvider.handleMessage(msg.arg2, msg.obj, ok);
			break;
		}
	}

	@Override
	public void createDefaults(Exercise e, Context context) {
		mExerciseProvider.createDefaults(context, mMessenger);
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
	public void queryExercise(Context context) {
		mExerciseProvider.query(context, mMessenger);
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
	public void latestWeight(Context context) {
		mWeightProvider.latest(context, mMessenger);
	}

	@Override
	public void queryWeight(Context context) {
		mWeightProvider.query(context, mMessenger);
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
	public void queryWorkout(Context context) {
		mWorkoutProvider.query(context, mMessenger);
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
	public void latestExerciseData(long exerciseId, Context context) {
		mWorkoutDataProvider
				.latestExerciseData(exerciseId, context, mMessenger);
	}

	@Override
	public void latestWorkoutData(long workoutId, Context context) {
		mWorkoutDataProvider.latestWorkoutData(workoutId, context, mMessenger);
	}

	@Override
	public void queryWorkoutData(Context context) {
		mWorkoutDataProvider.query(context, mMessenger);
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
