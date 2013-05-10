package strength.history.data.service;

import strength.history.data.structure.Weight;

/**
 * Base class for weight services
 */
public abstract class WeightServiceBase extends ServiceBase<Weight> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WEIGHT = "WEIGHT";

	/**
	 * Constructs a new WeightService
	 * 
	 * @param name
	 *            Name of the service
	 */
	public WeightServiceBase(String name) {
		super(name);
	}
}
