package strength.history.data.service;

import strength.history.data.structure.Weight;
import android.content.Intent;
import android.os.Messenger;

public abstract class WeightDataServiceBase extends ServiceBase {
	public static final String WEIGHT = "WEIGHT";

	public interface WeightDataProvider {
		public void deleteWeight(Weight weight);

		public void insertWeight(Weight weight);

		public void queryWeight();

		public void updateWeight(Weight weight);
	}

	public enum Request {
		DELETE, INSERT, QUERY, UPDATE;

		private static final Request[] REQUEST_VALUES = Request.values();

		public static Request parse(int i) {
			return REQUEST_VALUES[i];
		}
	}

	protected boolean query_interrupt = false;

	public WeightDataServiceBase(String name) {
		super(name);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Request request = (Request) intent.getSerializableExtra(REQUEST);
		if (request != Request.QUERY) {
			query_interrupt = true;
		} else {
			query_interrupt = false;
		}
		return super.onStartCommand(intent, flags, startId);
	}

	protected abstract void delete(Weight weight, Messenger messenger);

	protected abstract void insert(Weight weight, Messenger messenger);

	protected abstract void query(Messenger messenger);

	protected abstract void update(Weight weight, Messenger messenger);
}
