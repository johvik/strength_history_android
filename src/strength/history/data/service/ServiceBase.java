package strength.history.data.service;

import android.app.IntentService;

public abstract class ServiceBase extends IntentService {
	public static final String MESSENGER = "MESSENGER";
	public static final String REQUEST = "REQUEST";

	protected static final int QUERY_LIMIT = 50;
	protected static final int DB_TRIES = 2;

	public enum Service {
		WEIGHT_DATA;

		private static final Service[] SERVICE_VALUES = Service.values();

		public static Service parse(int i) {
			return SERVICE_VALUES[i];
		}
	}

	public ServiceBase(String name) {
		super(name);
	}
}
