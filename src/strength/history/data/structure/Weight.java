package strength.history.data.structure;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Weight class
 */
public class Weight extends Base<Weight> {
	private long time;
	private double weight;

	/**
	 * Constructs a new weight object
	 * 
	 * @param time
	 * @param weight
	 */
	public Weight(long time, double weight) {
		this(-1, Sync.NEW, time, weight);
	}

	/**
	 * Constructs a new weight object
	 * 
	 * @param id
	 * @param sync
	 * @param time
	 * @param weight
	 */
	public Weight(long id, int sync, long time, double weight) {
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
	public Weight copy() {
		return new Weight(getId(), getSync(), time, weight);
	}

	@Override
	public String toString() {
		return "Weight=" + getId() + " " + new Date(time).toLocaleString()
				+ " " + weight + " kg " + getSync();
	}

	@Override
	public void updateFrom(Weight another) {
		super.updateFrom(another);
		time = another.time;
		weight = another.weight;
	}

	@Override
	public int compareTo(Weight another) {
		int c = Long.valueOf(another.time).compareTo(time); // descending time
		if (c == 0) {
			c = Double.valueOf(weight).compareTo(another.weight);
			if (c == 0) {
				c = super.compareTo(another);
			}
		}
		return c;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
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
