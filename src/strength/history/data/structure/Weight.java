package strength.history.data.structure;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Weight class
 */
public class Weight extends SyncBase<Weight> {
	private long time;
	private double weight;

	/**
	 * Constructs a new weight object
	 * 
	 * @param time
	 * @param weight
	 */
	public Weight(long time, double weight) {
		this(-1, new Date().getTime(), time, weight);
	}

	/**
	 * Constructs a new weight object
	 * 
	 * @param id
	 * @param sync
	 * @param time
	 * @param weight
	 */
	public Weight(long id, long sync, long time, double weight) {
		super(id, sync);
		this.time = time;
		this.weight = weight;
	}

	protected Weight(Parcel in) {
		super(in);
		time = in.readLong();
		weight = in.readDouble();
	}

	@Override
	public String toString() {
		return "Weight=" + getId() + ":" + getSync() + " "
				+ new Date(time).toLocaleString() + " " + weight + " kg";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof Weight)) {
			return false;
		} else {
			Weight w = (Weight) o;
			return getId() == w.getId() && time == w.time;
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
			return Long.valueOf(getId()).compareTo(another.getId());
		}
		return c;
	}

	@Override
	protected Weight _copy() {
		return new Weight(getId(), getSync(), time, weight);
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
}
