package strength.history.data.service.local;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
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

	@Override
	protected void previous(Weight e, Messenger messenger) {
		WeightDBHelper db = getDB();
		Message msg = Message.obtain();
		msg.arg1 = getArg1();
		msg.arg2 = Request.PREVIOUS.ordinal();

		Weight w = db.previous();
		msg.what = w != null ? 1 : 0;
		msg.obj = w;

		try {
			messenger.send(msg);
		} catch (RemoteException ex) {
			Log.e("LocalWeightService", "Failed to send message");
		}
	}
}
