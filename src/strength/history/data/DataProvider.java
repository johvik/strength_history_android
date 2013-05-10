package strength.history.data;

import java.util.ArrayList;
import java.util.TreeSet;

import strength.history.data.service.ServiceBase;
import strength.history.data.service.WeightServiceBase.WeightProvider;
import strength.history.data.service.local.LocalWeightService;
import strength.history.data.structure.Weight;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class DataProvider implements WeightProvider {

	/**
	 * List of events listeners can support
	 */
	public interface Events {
		/**
		 * WeightData was changed
		 * 
		 * @param weightData
		 *            The new data
		 */
		public void weightDataUpdate(Iterable<Weight> weightData);
	}

	private static Messenger mMessenger = new Messenger(new MessageHandler());
	private static TreeSet<Weight> weightData = new TreeSet<Weight>();
	private static DataListener dataListener = null;

	public static void setListener(DataListener dataListener) {
		DataProvider.dataListener = dataListener;
		if (dataListener != null) {
			DataProvider.dataListener.weightDataUpdate(weightData);
		}
	}

	@Override
	public void deleteWeight(Weight weight) {
		if (dataListener != null) {
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					LocalWeightService.Request.DELETE);
			intent.putExtra(ServiceBase.MESSENGER, mMessenger);
			intent.putExtra(LocalWeightService.WEIGHT, weight);
			dataListener.startService(intent);
		}
	}

	@Override
	public void insertWeight(Weight weight) {
		if (dataListener != null) {
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					LocalWeightService.Request.INSERT);
			intent.putExtra(ServiceBase.MESSENGER, mMessenger);
			intent.putExtra(LocalWeightService.WEIGHT, weight);
			dataListener.startService(intent);
		}
	}

	@Override
	public void queryWeight() {
		if (dataListener != null) {
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					LocalWeightService.Request.QUERY);
			intent.putExtra(ServiceBase.MESSENGER, mMessenger);
			dataListener.startService(intent);
		}
	}

	@Override
	public void updateWeight(Weight weight) {
		if (dataListener != null) {
			Intent intent = new Intent(dataListener, LocalWeightService.class);
			intent.putExtra(ServiceBase.REQUEST,
					LocalWeightService.Request.UPDATE);
			intent.putExtra(ServiceBase.MESSENGER, mMessenger);
			intent.putExtra(LocalWeightService.WEIGHT, weight);
			dataListener.startService(intent);
		}
	}

	private static class MessageHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			ServiceBase.Service service = ServiceBase.Service.parse(msg.arg1);
			Log.d("DataProvider", "service=" + service);
			switch (service) {
			case WEIGHT_DATA:
				LocalWeightService.Request request = LocalWeightService.Request
						.parse(msg.arg2);
				Log.d("DataProvider", "request=" + request);
				switch (request) {
				case DELETE: {
					Weight w = (Weight) msg.obj;
					if (msg.what == 0) { // Failed
						Log.e("DataProvider", "failed to delete weight " + w);
					} else {
						weightData.remove(w);
						if (dataListener != null) {
							dataListener.weightDataUpdate(weightData);
						}
					}
					break;
				}
				case QUERY: {
					@SuppressWarnings("unchecked")
					ArrayList<Weight> res = (ArrayList<Weight>) msg.obj;
					boolean added = weightData.addAll(res);
					if (added && dataListener != null) {
						dataListener.weightDataUpdate(weightData);
					} else if (!added) {
						Log.d("DataProvider", "query nothing changed");
					}
					break;
				}
				case INSERT: {
					Weight w = (Weight) msg.obj;
					if (msg.what == 0) { // Failed
						Log.e("DataProvider", "failed to insert weight " + w);
					} else {
						weightData.add(w);
						if (dataListener != null) {
							dataListener.weightDataUpdate(weightData);
						}
					}
					break;
				}
				case UPDATE: {
					Weight w = (Weight) msg.obj;
					if (msg.what == 0) { // Failed
						Log.e("DataProvider", "failed to update weight " + w);
					} else {
						// Search by id
						for (Weight wi : weightData) {
							if (w.getId() == wi.getId()) {
								weightData.remove(wi);
								wi.updateFrom(w);
								weightData.add(wi);
								break;
							}
						}
						if (dataListener != null) {
							dataListener.weightDataUpdate(weightData);
						}
					}
					break;
				}
				}
				break;
			}
		};
	}
}
