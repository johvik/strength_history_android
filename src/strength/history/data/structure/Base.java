package strength.history.data.structure;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Base class for the structures
 * 
 * @param <T>
 */
@SuppressLint("ParcelCreator")
public abstract class Base<T extends Base<?>> implements Comparable<T>,
		Parcelable {
	private long id;
	private T backup = null;

	public Base(long id) {
		this.id = id;
	}

	protected Base(Parcel in) {
		id = in.readLong();
	}

	@Override
	public abstract String toString();

	protected abstract T _copy();

	protected abstract void _updateFrom(T another);

	protected abstract void _writeToParcel(Parcel out, int flags);

	/**
	 * Moves values into the object
	 * 
	 * @param another
	 *            The new values
	 */
	public void updateFrom(T another) {
		id = another.id;
		_updateFrom(another);
	}

	@Override
	public abstract boolean equals(Object o);

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public int compareTo(T another) {
		return Long.valueOf(id).compareTo(another.id);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		_writeToParcel(out, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public final T copy() {
		return _copy();
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
}
