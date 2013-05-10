package strength.history.data.service;

import strength.history.data.structure.Exercise;

/**
 * Base class for exercise service
 */
public abstract class ExerciseServiceBase extends ServiceBase<Exercise> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String EXERCISE = "EXERCISE";

	/**
	 * Constructs a new exercise base service
	 * 
	 * @param name
	 *            Name of the service
	 */
	public ExerciseServiceBase(String name) {
		super(name);
	}

}
