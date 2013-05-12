package strength.history.data.service.local;

import strength.history.data.db.WorkoutDataDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.WorkoutData;

public class LocalWorkoutDataService extends
		LocalServiceBase<WorkoutData, WorkoutDataDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WORKOUT_DATA = "WORKOUT_DATA";

	public LocalWorkoutDataService() {
		super("LocalWorkoutDataService");
	}

	@Override
	protected WorkoutDataDBHelper getDB() {
		return WorkoutDataDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected String getIntentName() {
		return WORKOUT_DATA;
	}

	@Override
	protected int getArg1() {
		return Service.WORKOUT_DATA.ordinal();
	}
}
