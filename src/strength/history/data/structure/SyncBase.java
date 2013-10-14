package strength.history.data.structure;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

/**
 * Sync base class for the structures
 * 
 * @param <T>
 */
public abstract class SyncBase<T extends SyncBase<?>> extends Base<T> {
	protected static final String JSON_SYNC = "sync";
	protected static final String JSON_SERVER_ID = "_id";
	private long sync;
	private String serverId;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param sync
	 * @param serverId
	 */
	public SyncBase(long id, long sync, String serverId) {
		super(id);
		this.sync = sync;
		this.serverId = serverId;
	}

	/**
	 * Should be inherited!
	 * 
	 * @param in
	 */
	protected SyncBase(Parcel in) {
		super(in);
		sync = in.readLong();
		serverId = in.readString();
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
		serverId = another.serverId;
		_updateFrom(another);
	}

	@Override
	public final void writeToParcel(Parcel out, int flags) {
		out.writeLong(getId());
		out.writeLong(sync);
		out.writeString(serverId);
		_writeToParcel(out, flags);
	}

	@Override
	public abstract boolean equals(Object o);

	@Override
	public int compareTo(T another) {
		int c = serverId.compareTo(another.serverId);
		if (serverId.length() == 0) {
			// Super compares id
			c = super.compareTo(another);
		}
		return c;
	}

	@Override
	public abstract JSONObject toJSON() throws JSONException;

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

	/**
	 * Gets the id of the object on the server
	 * 
	 * @return The id
	 */
	public final String getServerId() {
		return serverId;
	}
}
