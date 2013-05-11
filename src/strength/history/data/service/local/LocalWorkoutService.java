package strength.history.data.service.local;

import strength.history.data.db.WorkoutDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Workout;

public class LocalWorkoutService extends
		LocalServiceBase<Workout, WorkoutDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WORKOUT = "WORKOUT";

	public LocalWorkoutService() {
		super("LocalWorkoutService");
	}

	@Override
	protected WorkoutDBHelper getDB() {
		return WorkoutDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected String getIntentName() {
		return WORKOUT;
	}

	@Override
	protected int getArg1() {
		return Service.WORKOUT.ordinal();
	}
}
