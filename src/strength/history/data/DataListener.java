package strength.history.data;

/**
 * Base Activity that acts as a listener
 */
public final class DataListener {
	private static final DataProvider dataProvider = new DataProvider();

	private DataListener() {
	}

	/**
	 * Should be called in onCreate
	 * 
	 * @param object
	 * @return The data provider object
	 */
	public static DataProvider add(Object object) {
		dataProvider.addListeners(object);
		return dataProvider;
	}

	/**
	 * Should be called in onDestroy
	 * 
	 * @param object
	 */
	public static void remove(Object object) {
		dataProvider.removeListeners(object);
	}
}
