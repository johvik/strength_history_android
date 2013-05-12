package strength.history.data.structure;

import android.os.Parcel;
import android.os.Parcelable;

public class SetData extends Base<SetData> {
	private double weight;
	private int repetitions;

	public SetData(double weight, int repetitions) {
		this(-1, weight, repetitions);
	}

	public SetData(long id, double weight, int repetitions) {
		super(id);
		this.weight = weight;
		this.repetitions = repetitions;
	}

	protected SetData(Parcel in) {
		super(in);
		weight = in.readDouble();
		repetitions = in.readInt();
	}

	@Override
	public String toString() {
		return "SetData=" + getId() + " " + weight + "kg x" + repetitions;
	}

	@Override
	protected SetData _copy() {
		return new SetData(getId(), weight, repetitions);
	}

	@Override
	public int compareTo(SetData another) {
		int c = Long.valueOf(getId()).compareTo(another.getId());
		if (c == 0) {
			c = Double.valueOf(weight).compareTo(another.weight);
			if (c == 0) {
				c = Integer.valueOf(repetitions).compareTo(another.repetitions);
			}
		}
		return c;
	}

	@Override
	protected void _updateFrom(SetData another) {
		weight = another.weight;
		repetitions = another.repetitions;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeDouble(weight);
		out.writeInt(repetitions);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<SetData> CREATOR = new Parcelable.Creator<SetData>() {
		@Override
		public SetData createFromParcel(Parcel in) {
			return new SetData(in);
		}

		@Override
		public SetData[] newArray(int size) {
			return new SetData[size];
		}
	};

	public double getWeight() {
		return weight;
	}

	public int getRepetitions() {
		return repetitions;
	}
}
