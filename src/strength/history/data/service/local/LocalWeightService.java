package strength.history.data.service.local;

import java.util.ArrayList;

import strength.history.data.db.WeightDBHelper;
import strength.history.data.service.WeightServiceBase;
import strength.history.data.structure.Weight;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class LocalWeightService extends WeightServiceBase {
	public LocalWeightService() {
		super("LocalWeightService");
	}

	@Override
	public void onDestroy() {
		Log.d("LocalWeightService", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalWeightService", "onHandleIntent");

		Request request = (Request) intent.getSerializableExtra(REQUEST);
		Messenger messenger = intent.getParcelableExtra(MESSENGER);
		if (request != null && messenger != null) {
			switch (request) {
			case DELETE:
				delete((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			case INSERT:
				insert((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			case QUERY:
				query(messenger);
				break;
			case UPDATE:
				update((Weight) intent.getParcelableExtra(WEIGHT), messenger);
				break;
			}
		}
	}

	@Override
	protected void delete(Weight weight, Messenger messenger) {
		if (weight != null) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.WEIGHT_DATA.ordinal();
			msg.arg2 = Request.DELETE.ordinal();

			boolean deleted = db.delete(weight);
			for (int i = 0; i < DB_TRIES && !deleted; i++) {
				deleted = db.delete(weight);
			}
			msg.what = deleted ? 1 : 0;
			msg.obj = weight;

			try {
				messenger.send(msg);
			} catch (RemoteException e) {
				Log.e("LocalWeightService", "Failed to send message");
			}
		}
	}

	@Override
	protected void insert(Weight weight, Messenger messenger) {
		if (weight != null) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.WEIGHT_DATA.ordinal();
			msg.arg2 = Request.INSERT.ordinal();

			boolean inserted = db.insert(weight);
			for (int i = 0; i < DB_TRIES && !inserted; i++) {
				inserted = db.insert(weight);
			}
			msg.what = inserted ? 1 : 0;
			msg.obj = weight;

			try {
				messenger.send(msg);
			} catch (RemoteException e) {
				Log.e("LocalWeightService", "Failed to send message");
			}
		}
	}

	@Override
	protected void query(Messenger messenger) {
		// Divide into smaller queries
		for (int offset = 0; !query_interrupt; offset += QUERY_LIMIT) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.WEIGHT_DATA.ordinal();
			msg.arg2 = Request.QUERY.ordinal();

			ArrayList<Weight> res = db.query(offset, QUERY_LIMIT);
			msg.obj = res;

			try {
				messenger.send(msg);
			} catch (RemoteException e) {
				Log.e("LocalWeightService", "Failed to send message");
			}

			if (res.size() < QUERY_LIMIT) {
				break;
			}
		}
	}

	@Override
	protected void update(Weight weight, Messenger messenger) {
		if (weight != null) {
			WeightDBHelper db = WeightDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.WEIGHT_DATA.ordinal();
			msg.arg2 = Request.UPDATE.ordinal();

			boolean updated = db.update(weight);
			for (int i = 0; i < DB_TRIES && !updated; i++) {
				updated = db.update(weight);
			}
			msg.what = updated ? 1 : 0;
			msg.obj = weight;

			try {
				messenger.send(msg);
			} catch (RemoteException e) {
				Log.e("LocalWeightService", "Failed to send message");
			}
		}
	}
}
