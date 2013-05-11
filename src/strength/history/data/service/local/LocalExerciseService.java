package strength.history.data.service.local;

import strength.history.data.db.ExerciseDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Exercise;

/**
 * Local exercise service
 */
public class LocalExerciseService extends
		LocalServiceBase<Exercise, ExerciseDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String EXERCISE = "EXERCISE";

	/**
	 * Constructor
	 */
	public LocalExerciseService() {
		super("LocalExerciseService");
	}

	@Override
	protected ExerciseDBHelper getDB() {
		return ExerciseDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected String getIntentName() {
		return EXERCISE;
	}

	@Override
	protected int getArg1() {
		return Service.EXERCISE.ordinal();
	}
}
