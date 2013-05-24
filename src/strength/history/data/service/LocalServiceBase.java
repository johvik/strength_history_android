package strength.history.data.service;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import strength.history.data.db.DBHelperBase;
import strength.history.data.structure.SyncBase;

/**
 * Local base class for the service framework
 * 
 * @param <E>
 *            Structure type it provides
 * @param <D>
 *            DBHelper class
 */
public abstract class LocalServiceBase<E extends SyncBase<E>, D extends DBHelperBase<E>>
		extends ServiceBase<E> {
	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public LocalServiceBase(String name) {
		super(name);
	}

	protected abstract D getDB();

	protected abstract String getIntentName();

	@Override
	protected final void delete(E e, Messenger messenger) {
		if (e != null) {
			D db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = getDeleteArg();

			boolean deleted = db.delete(e);
			for (int i = 0; i < DB_TRIES && !deleted; i++) {
				deleted = db.delete(e);
			}
			msg.what = deleted ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalServiceBase", "Failed to send message");
			}
		}
	}

	@Override
	protected final void insert(E e, Messenger messenger) {
		if (e != null) {
			D db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = getInsertArg();

			boolean inserted = db.insert(e);
			for (int i = 0; i < DB_TRIES && !inserted; i++) {
				inserted = db.insert(e);
			}
			msg.what = inserted ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalServiceBase", "Failed to send message");
			}
		}
	}

	@Override
	protected final void purge(Messenger messenger) {
		D db = getDB();
		db.purge();
	}

	@Override
	protected final void query(Messenger messenger) {
		// Divide into smaller queries
		for (int offset = 0; !query_interrupt; offset += QUERY_LIMIT) {
			D db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = getQueryArg();

			ArrayList<E> res = db.query(offset, QUERY_LIMIT);
			boolean doBreak = res.size() < QUERY_LIMIT;
			msg.what = doBreak ? 1 : 0;
			msg.obj = res;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalServiceBase", "Failed to send message");
			}

			if (doBreak) {
				break;
			}
		}
	}

	@Override
	protected final void update(E e, Messenger messenger) {
		if (e != null) {
			D db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = getUpdateArg();

			boolean updated = db.update(e);
			for (int i = 0; i < DB_TRIES && !updated; i++) {
				updated = db.update(e);
			}
			msg.what = updated ? 1 : 0;
			msg.obj = e;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalServiceBase", "Failed to send message");
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalServiceBase", "onHandleIntent");

		int id = intent.getIntExtra(REQUEST, -1);
		if (id != -1) {
			Request request = Request.parse(id);
			Log.d("LocalServiceBase", "request=" + request);
			Messenger messenger = intent.getParcelableExtra(MESSENGER);
			if (messenger != null) {
				switch (request) {
				case DELETE:
					delete((E) intent.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case INSERT:
					insert((E) intent.getParcelableExtra(getIntentName()),
							messenger);
					break;
				case PURGE:
					purge(messenger);
					break;
				case QUERY:
					query(messenger);
					break;
				case STOP:
					// Do nothing (see ServiceBase.onStartCommand)
					break;
				case UPDATE:
					update((E) intent.getParcelableExtra(getIntentName()),
							messenger);
					break;
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		Log.d(this.getClass().getSimpleName(),
				"onDestroy " + Service.parse(getArg1()));
		super.onDestroy();
	}
}
