package strength.history.data.service.local;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
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

	@Override
	protected void previous(WorkoutData e, Messenger messenger) {
		if (e != null) {
			WorkoutDataDBHelper db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = Request.PREVIOUS.ordinal();

			WorkoutData w = db.previous(e);
			msg.what = w != null ? 1 : 0;
			msg.obj = w;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalWorkoutDataService", "Failed to send message");
			}
		}
	}
}
