package strength.history.data.service;

import java.util.ArrayList;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import strength.history.data.db.DBHelperBase;
import strength.history.data.structure.Base;

/**
 * Local base class for the service framework
 * 
 * @param <E>
 *            Structure type it provides
 * @param <D>
 *            DBHelper class
 */
public abstract class LocalServiceBase<E extends Base<E>, D extends DBHelperBase<E>>
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

	@Override
	protected final void delete(E e, Messenger messenger) {
		if (e != null) {
			D db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
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
				Log.e("LocalServiceBase", "Failed to send message");
			}
		}
	}

	@Override
	protected final void query(Messenger messenger) {
		// Divide into smaller queries
		for (int offset = 0; !query_interrupt; offset += QUERY_LIMIT) {
			D db = getDB();
			Message msg = Message.obtain();
			msg.arg1 = getArg1();
			msg.arg2 = Request.QUERY.ordinal();

			ArrayList<E> res = db.query(offset, QUERY_LIMIT);
			msg.what = 1;
			msg.obj = res;

			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e("LocalServiceBase", "Failed to send message");
			}

			if (res.size() < QUERY_LIMIT) {
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
				Log.e("LocalServiceBase", "Failed to send message");
			}
		}

	}
}
