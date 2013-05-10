package strength.history.data.structure;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Weight implements Base<Weight> {
	private long id;
	private long time;
	private double weight;
	private int sync;
	private Weight backup = null;

	public Weight(long time, double weight) {
		this(-1, time, weight, Sync.NEW);
	}

	public Weight(long id, long time, double weight, int sync) {
		this.id = id;
		this.time = time;
		this.weight = weight;
		this.sync = sync;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public long getTime() {
		return time;
	}

	public double getWeight() {
		return weight;
	}

	public int getSync() {
		return sync;
	}

	@Override
	public void updateFrom(Weight another) {
		id = another.id;
		time = another.time;
		weight = another.weight;
		sync = another.sync;
	}

	@Override
	public Weight copy() {
		Weight copy = new Weight(id, time, weight, sync);
		return copy;
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

	private Weight(Parcel in) {
		id = in.readLong();
		time = in.readLong();
		weight = in.readDouble();
		sync = in.readInt();
	}

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
	public String toString() {
		return id + " " + new Date(time).toLocaleString() + " " + weight
				+ " kg " + sync;
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
	};
}
