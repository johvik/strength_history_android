package strength.history.data.structure;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Weight class
 */
public class Weight implements Base<Weight> {
	private long id;
	private long time;
	private double weight;
	private int sync;
	private Weight backup = null;

	/**
	 * Constructs a new weight object
	 * 
	 * @param time
	 * @param weight
	 */
	public Weight(long time, double weight) {
		this(-1, time, weight, Sync.NEW);
	}

	/**
	 * Constructs a new weight object
	 * 
	 * @param id
	 * @param time
	 * @param weight
	 * @param sync
	 */
	public Weight(long id, long time, double weight, int sync) {
		this.id = id;
		this.time = time;
		this.weight = weight;
		this.sync = sync;
	}

	private Weight(Parcel in) {
		id = in.readLong();
		time = in.readLong();
		weight = in.readDouble();
		sync = in.readInt();
	}

	@Override
	public int compareTo(Weight another) {
		int c = Long.valueOf(another.time).compareTo(time); // descending time
		if (c == 0) {
			c = Long.valueOf(id).compareTo(another.id);
			if (c == 0) {
				c = Double.valueOf(weight).compareTo(another.weight);
				if (c == 0) {
					c = Integer.valueOf(sync).compareTo(another.sync);
				}
			}
		}
		return c;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeLong(time);
		out.writeDouble(weight);
		out.writeInt(sync);
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

	@Override
	public void updateFrom(Weight another) {
		id = another.id;
		time = another.time;
		weight = another.weight;
		sync = another.sync;
	}

	@Override
	public Weight copy() {
		return new Weight(id, time, weight, sync);
	}

	@Override
	public void backup() {
		if (backup == null) {
			backup = copy();
		}
	}

	@Override
	public void commit() {
		backup = null;
	}

	@Override
	public void revert() {
		if (backup != null) {
			updateFrom(backup);
			backup = null;
		}
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int getSync() {
		return sync;
	}

	@Override
	public void setSync(int sync) {
		this.sync = sync;
	}

	@Override
	public String toString() {
		return "Weight=" + id + " " + new Date(time).toLocaleString() + " "
				+ weight + " kg " + sync;
	}

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
