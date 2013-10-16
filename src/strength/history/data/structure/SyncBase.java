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
	public enum State {
		/**
		 * Data is unchanged since last sync
		 */
		OLD,
		/**
		 * The data has been added since last sync
		 */
		NEW,
		/**
		 * The data has been updated since last sync
		 */
		UPDATED,
		/**
		 * The data has been removed since last sync
		 */
		DELETED;
		private static final State[] STATE_VALUES = State.values();

		public static State parse(int i) {
			return STATE_VALUES[i % STATE_VALUES.length];
		}
	};

	protected static final String JSON_SYNC = "sync";
	protected static final String JSON_SERVER_ID = "_id";
	private long sync;
	private String serverId;
	private State state;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param sync
	 * @param serverId
	 * @param state
	 */
	public SyncBase(long id, long sync, String serverId, State state) {
		super(id);
		this.sync = sync;
		this.serverId = serverId;
		this.state = state;
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
		state = State.parse(in.readInt());
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
		state = another.state;
		_updateFrom(another);
	}

	@Override
	public final void writeToParcel(Parcel out, int flags) {
		out.writeLong(getId());
		out.writeLong(sync);
		out.writeString(serverId);
		out.writeInt(state.ordinal());
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

	/**
	 * Gets the state of the local object
	 * 
	 * @return The state
	 */
	public final State getState() {
		return state;
	}
}
