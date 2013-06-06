package strength.history.data.structure;

import android.os.Parcel;

/**
 * Sync base class for the structures
 * 
 * @param <T>
 */
public abstract class SyncBase<T extends SyncBase<?>> extends Base<T> {
	private long sync;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param sync
	 */
	public SyncBase(long id, long sync) {
		super(id);
		this.sync = sync;
	}

	/**
	 * Should be inherited!
	 * 
	 * @param in
	 */
	protected SyncBase(Parcel in) {
		super(in);
		sync = in.readLong();
	}

	/**
	 * Moves values into the object
	 * 
	 * @param another
	 *            The new values
	 */
	@Override
	public final void updateFrom(T another) {
		setId(another.getId());
		sync = another.sync;
		_updateFrom(another);
	}

	@Override
	public final void writeToParcel(Parcel out, int flags) {
		out.writeLong(getId());
		out.writeLong(sync);
		_writeToParcel(out, flags);
	}

	@Override
	public abstract boolean equals(Object o);

	/**
	 * Gets the sync state of the object
	 * 
	 * @return The sync state
	 */
	public final long getSync() {
		return sync;
	}

	/**
	 * Changes the sync state of the object
	 * 
	 * @param sync
	 *            The new sync state
	 */
	public final void setSync(long sync) {
		this.sync = sync;
	}
}
