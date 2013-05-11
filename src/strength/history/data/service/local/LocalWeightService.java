package strength.history.data.service.local;

import strength.history.data.db.WeightDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Weight;

/**
 * Local weight service
 */
public class LocalWeightService extends
		LocalServiceBase<Weight, WeightDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String WEIGHT = "WEIGHT";

	/**
	 * Constructor
	 */
	public LocalWeightService() {
		super("LocalWeightService");
	}

	@Override
	protected WeightDBHelper getDB() {
		return WeightDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected String getIntentName() {
		return WEIGHT;
	}

	@Override
	protected int getArg1() {
		return Service.WEIGHT.ordinal();
	}
}
