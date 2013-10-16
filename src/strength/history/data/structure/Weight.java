package strength.history.data.structure;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Weight class
 */
public class Weight extends SyncBase<Weight> {
	public static final double DEFAULT = 75.0;
	private static final String JSON_TIME = "time";
	private static final String JSON_WEIGHT = "weight";
	private long time;
	private double weight;

	/**
	 * Constructs a new weight object
	 * 
	 * @param time
	 * @param weight
	 */
	public Weight(long time, double weight) {
		this(-1, new Date().getTime(), "", time, weight);
	}

	/**
	 * Constructs a new weight object
	 * 
	 * @param id
	 * @param sync
	 * @param serverId
	 * @param time
	 * @param weight
	 */
	public Weight(long id, long sync, String serverId, long time, double weight) {
		super(id, sync, serverId);
		this.time = time;
		this.weight = weight;
	}

	protected Weight(Parcel in) {
		super(in);
		time = in.readLong();
		weight = in.readDouble();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof Weight)) {
			return false;
		} else {
			return compareTo((Weight) o) == 0;
		}
	}

	@Override
	public int hashCode() {
		long id = getId();
		int result = (int) (id ^ (id >>> 32));
		return result * 37 + (int) (time ^ (time >>> 32));
	}

	@Override
	public int compareTo(Weight another) {
		int c = Long.valueOf(another.time).compareTo(time); // descending time
		if (c == 0) {
			c = super.compareTo(another);
		}
		return c;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(JSON_SYNC, getSync());
		object.put(JSON_SERVER_ID, getServerId());
		object.put(JSON_TIME, time);
		object.put(JSON_WEIGHT, weight);
		return object;
	}

	public static final Weight fromJSON(JSONObject object) throws JSONException {
		long sync = object.getLong(JSON_SYNC);
		String serverId = object.getString(JSON_SERVER_ID);
		long time = object.getLong(JSON_TIME);
		double weight = object.getDouble(JSON_WEIGHT);
		return new Weight(-1, sync, serverId, time, weight);
	}

	@Override
	protected Weight _copy() {
		return new Weight(getId(), getSync(), getServerId(), time, weight);
	}

	@Override
	protected void _updateFrom(Weight another) {
		time = another.time;
		weight = another.weight;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeLong(time);
		out.writeDouble(weight);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<Weight> CREATOR = new Parcelable.Creator<Weight>() {
		@Override
		public Weight createFromParcel(Parcel in) {
			return new Weight(in);
		}

		@Override
		public Weight[] newArray(int size) {
			return new Weight[size];
		}
	};

	/**
	 * Gets the time of the weight
	 * 
	 * @return The time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Gets the weight
	 * 
	 * @return The weight
	 */
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
