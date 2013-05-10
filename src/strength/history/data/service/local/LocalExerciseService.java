package strength.history.data.service.local;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import strength.history.data.db.ExerciseDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Exercise;

/**
 * Local exercise service
 */
public class LocalExerciseService extends
		LocalServiceBase<Exercise, ExerciseDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String EXERCISE = "EXERCISE";

	/**
	 * Constructor
	 */
	public LocalExerciseService() {
		super("LocalExerciseService");
	}

	@Override
	protected ExerciseDBHelper getDB() {
		return ExerciseDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected int getArg1() {
		return Service.EXERCISE.ordinal();
	}

	@Override
	protected void delete(Exercise e, Messenger messenger) {
		if (e != null) {
			ExerciseDBHelper db = ExerciseDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.EXERCISE.ordinal();
			msg.arg2 = Request.DELETE.ordinal();

			boolean deleted = db.delete(e);
			for (int i = 0; i < DB_TRIES && !deleted; i++) {
				deleted = db.delete(e);
			}
			msg.what = deleted ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalExerciseService", "Failed to send message");
			}
		}
	}

	@Override
	protected void insert(Exercise e, Messenger messenger) {
		if (e != null) {
			ExerciseDBHelper db = ExerciseDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.EXERCISE.ordinal();
			msg.arg2 = Request.INSERT.ordinal();

			boolean inserted = db.insert(e);
			for (int i = 0; i < DB_TRIES && !inserted; i++) {
				inserted = db.insert(e);
			}
			msg.what = inserted ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalExerciseService", "Failed to send message");
			}
		}
	}

	@Override
	protected void query(Messenger messenger) {
		// Divide into smaller queries
		for (int offset = 0; !query_interrupt; offset += QUERY_LIMIT) {
			ExerciseDBHelper db = ExerciseDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.EXERCISE.ordinal();
			msg.arg2 = Request.QUERY.ordinal();

			ArrayList<Exercise> res = db.query(offset, QUERY_LIMIT);
			msg.what = 1;
			msg.obj = res;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalExerciseService", "Failed to send message");
			}

			if (res.size() < QUERY_LIMIT) {
				break;
			}
		}
	}

	@Override
	protected void update(Exercise e, Messenger messenger) {
		if (e != null) {
			ExerciseDBHelper db = ExerciseDBHelper
					.getInstance(getApplicationContext());
			Message msg = Message.obtain();
			msg.arg1 = Service.EXERCISE.ordinal();
			msg.arg2 = Request.UPDATE.ordinal();

			boolean updated = db.update(e);
			for (int i = 0; i < DB_TRIES && !updated; i++) {
				updated = db.update(e);
			}
			msg.what = updated ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalExerciseService", "Failed to send message");
			}
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalExerciseService", "onHandleIntent");

		Request request = (Request) intent.getSerializableExtra(REQUEST);
		Messenger messenger = intent.getParcelableExtra(MESSENGER);
		if (request != null && messenger != null) {
			switch (request) {
			case DELETE:
				delete((Exercise) intent.getParcelableExtra(EXERCISE),
						messenger);
				break;
			case INSERT:
				insert((Exercise) intent.getParcelableExtra(EXERCISE),
						messenger);
				break;
			case QUERY:
				query(messenger);
				break;
			case UPDATE:
				update((Exercise) intent.getParcelableExtra(EXERCISE),
						messenger);
				break;
			}
		}
	}
}
