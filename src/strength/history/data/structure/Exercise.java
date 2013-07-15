package strength.history.data.structure;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Exercise class
 */
public class Exercise extends SyncBase<Exercise> {
	public static double DEFAULT_INCREASE = 2.5;

	public enum MuscleGroup {
		// important order
		// a change will change the type of all existing exercises
		ARMS, CHEST, BACK, SHOULDERS, ABS, LEGS;

		public static final MuscleGroup DEFAULT = ARMS;
		public static final MuscleGroup[] SORTED_VALUES = MuscleGroup.values();
		private static final MuscleGroup[] VALUES = MuscleGroup.values();
		static {
			Arrays.sort(SORTED_VALUES, new Comparator<MuscleGroup>() {
				@Override
				public int compare(MuscleGroup lhs, MuscleGroup rhs) {
					return lhs.toString().compareTo(rhs.toString());
				}
			});
		}

		/**
		 * Converts a number to a enum
		 * 
		 * @param i
		 *            Index in the enum
		 * @return The enum at index i % length
		 */
		public static MuscleGroup parse(int i) {
			return VALUES[i % VALUES.length];
		}

		@Override
		public String toString() {
			switch (this) {
			case ABS:
				return "Abs";
			case ARMS:
				return "Arms";
			case BACK:
				return "Back";
			case CHEST:
				return "Chest";
			case LEGS:
				return "Legs";
			case SHOULDERS:
				return "Shoulders";
			default:
				return super.toString();
			}
		}
	}

	private String name;
	private MuscleGroup muscleGroup;
	private double standardIncrease;

	/**
	 * Constructs a new exercise
	 * 
	 * @param name
	 * @param muscleGroup
	 * @param standardIncrease
	 */
	public Exercise(String name, MuscleGroup muscleGroup,
			double standardIncrease) {
		this(-1, new Date().getTime(), name, muscleGroup, standardIncrease);
	}

	/**
	 * Constructs a new exercise
	 * 
	 * @param id
	 * @param sync
	 * @param name
	 * @param muscleGroup
	 * @param standardIncrease
	 */
	public Exercise(long id, long sync, String name, MuscleGroup muscleGroup,
			double standardIncrease) {
		super(id, sync);
		this.name = name;
		this.muscleGroup = muscleGroup;
		this.standardIncrease = standardIncrease;
	}

	protected Exercise(Parcel in) {
		super(in);
		name = in.readString();
		muscleGroup = MuscleGroup.parse(in.readInt());
		standardIncrease = in.readDouble();
	}

	@Override
	public String toString() {
		return "Exercise=" + getId() + ":" + getSync() + " " + name + " "
				+ muscleGroup + " " + standardIncrease;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof Exercise)) {
			return false;
		} else {
			Exercise e = (Exercise) o;
			return getId() == e.getId();
		}
	}

	@Override
	protected Exercise _copy() {
		return new Exercise(getId(), getSync(), name, muscleGroup,
				standardIncrease);
	}

	@Override
	protected void _updateFrom(Exercise another) {
		name = another.name;
		muscleGroup = another.muscleGroup;
		standardIncrease = another.standardIncrease;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeInt(muscleGroup.ordinal());
		out.writeDouble(standardIncrease);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
		@Override
		public Exercise createFromParcel(Parcel in) {
			return new Exercise(in);
		}

		@Override
		public Exercise[] newArray(int size) {
			return new Exercise[size];
		}
	};

	/**
	 * Gets the name
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MuscleGroup getMuscleGroup() {
		return muscleGroup;
	}

	public void setMuscleGroup(MuscleGroup muscleGroup) {
		this.muscleGroup = muscleGroup;
	}

	public double getStandardIncrease() {
		return standardIncrease;
	}

	public void setStandardIncrease(double standardIncrease) {
		this.standardIncrease = standardIncrease;
	}
}
