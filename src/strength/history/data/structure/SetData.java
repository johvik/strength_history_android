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
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof SetData)) {
			return false;
		} else {
			SetData d = (SetData) o;
			return getId() == d.getId();
		}
	}

	@Override
	protected SetData _copy() {
		return new SetData(getId(), weight, repetitions);
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
