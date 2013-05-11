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

	protected Base(Parcel in) {
		id = in.readLong();
		sync = in.readInt();
	}

	/**
	 * Creates a new object with the same properties
	 * 
	 * @return The new object
	 */
	public abstract T copy();

	@Override
	public abstract String toString();

	/**
	 * Moves values into the object
	 * 
	 * @param another
	 *            The new values
	 */
	public void updateFrom(T another) {
		id = another.id;
		sync = another.sync;
	}

	@Override
	public int compareTo(T another) {
		int c = Long.valueOf(id).compareTo(another.id);
		if (c == 0) {
			c = Integer.valueOf(sync).compareTo(another.sync);
		}
		return c;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeInt(sync);
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
			backup = copy();
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
