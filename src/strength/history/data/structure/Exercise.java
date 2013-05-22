package strength.history.data.structure;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Exercise class
 */
public class Exercise extends SyncBase<Exercise> {
	public enum MuscleGroup {
		// important order, must be the same as the spinner in UI
		ARMS, CHEST, BACK, SHOULDERS, ABS, LEGS;

		private static final MuscleGroup[] VALUES = MuscleGroup.values();

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
	}

	private String name;
	private MuscleGroup muscleGroup;

	/**
	 * Constructs a new exercise
	 * 
	 * @param name
	 * @param muscleGroup
	 */
	public Exercise(String name, MuscleGroup muscleGroup) {
		this(-1, Sync.NEW, name, muscleGroup);
	}

	/**
	 * Constructs a new exercise
	 * 
	 * @param id
	 * @param sync
	 * @param name
	 * @param muscleGroup
	 */
	public Exercise(long id, int sync, String name, MuscleGroup muscleGroup) {
		super(id, sync);
		this.name = name;
		this.muscleGroup = muscleGroup;
	}

	protected Exercise(Parcel in) {
		super(in);
		name = in.readString();
		muscleGroup = MuscleGroup.parse(in.readInt());
	}

	@Override
	public String toString() {
		return "Exercise=" + getId() + ":" + getSync() + " " + name + " "
				+ muscleGroup;
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
		return new Exercise(getId(), getSync(), name, muscleGroup);
	}

	@Override
	protected void _updateFrom(Exercise another) {
		name = another.name;
		muscleGroup = another.muscleGroup;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeInt(muscleGroup.ordinal());
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
}
