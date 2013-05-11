package strength.history.data.structure;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Base class for the structures
 * 
 * @param <T>
 */
public abstract class Base<T extends Base<?>> implements Comparable<T>,
		Parcelable {
	private long id;
	private int sync;
	private T backup = null;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param sync
	 */
	public Base(long id, int sync) {
		this.id = id;
		this.sync = sync;
	}

	/**
	 * Should be inherited!
	 * 
	 * @param in
	 */
	protected Base(Parcel in) {
		id = in.readLong();
		sync = in.readInt();
	}

	@Override
	public abstract String toString();

	protected abstract T _copy();

	protected abstract int _compareTo(T another);

	protected abstract void _updateFrom(T another);

	protected abstract void _writeToParcel(Parcel out, int flags);

	/**
	 * Moves values into the object
	 * 
	 * @param another
	 *            The new values
	 */
	public final void updateFrom(T another) {
		id = another.id;
		sync = another.sync;
		_updateFrom(another);
	}

	@Override
	public final int compareTo(T another) {
		int c = _compareTo(another);
		if (c == 0) {
			c = Long.valueOf(id).compareTo(another.id);
			if (c == 0) {
				c = Integer.valueOf(sync).compareTo(another.sync);
			}
		}
		return c;
	}

	@Override
	public final void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeInt(sync);
		_writeToParcel(out, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Stores a local copy of the object for later reverts
	 */
	public final void backup() {
		if (backup == null) {
			backup = _copy();
		}
	}

	/**
	 * Deletes the backup
	 */
	public final void commit() {
		backup = null;
	}

	/**
	 * Restores the data from the backup
	 */
	public final void revert() {
		if (backup != null) {
			updateFrom(backup);
			backup = null;
		}
	}

	/**
	 * Gets the id of the object
	 * 
	 * @return The id
	 */
	public final long getId() {
		return id;
	}

	/**
	 * Changes the id of the object
	 * 
	 * @param id
	 *            The new id
	 */
	public final void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the sync state of the object
	 * 
	 * @return The sync state
	 */
	public final int getSync() {
		return sync;
	}

	/**
	 * Changes the sync state of the object
	 * 
	 * @param sync
	 *            The new sync state
	 */
	public final void setSync(int sync) {
		this.sync = sync;
	}
}
