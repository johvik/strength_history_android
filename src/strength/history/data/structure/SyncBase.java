package strength.history.data.structure;

import android.os.Parcel;

/**
 * Sync base class for the structures
 * 
 * @param <T>
 */
public abstract class SyncBase<T extends SyncBase<?>> extends Base<T> {
	private int sync;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param sync
	 */
	public SyncBase(long id, int sync) {
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
		sync = in.readInt();
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
		out.writeInt(sync);
		_writeToParcel(out, flags);
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
