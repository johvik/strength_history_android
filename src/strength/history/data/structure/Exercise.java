package strength.history.data.structure;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Exercise class
 */
public class Exercise extends Base<Exercise> {
	private String name;

	/**
	 * Constructs a new exercise
	 * 
	 * @param name
	 */
	public Exercise(String name) {
		this(-1, Sync.NEW, name);
	}

	/**
	 * Constructs a new exercise
	 * 
	 * @param id
	 * @param sync
	 * @param name
	 */
	public Exercise(long id, int sync, String name) {
		super(id, sync);
		this.name = name;
	}

	protected Exercise(Parcel in) {
		super(in);
		name = in.readString();
	}

	@Override
	public Exercise copy() {
		return new Exercise(getId(), getSync(), name);
	}

	@Override
	public String toString() {
		return "Exercise=" + getId() + " " + name + " " + getSync();
	}

	@Override
	public void updateFrom(Exercise another) {
		super.updateFrom(another);
		name = another.name;
	}

	@Override
	public int compareTo(Exercise another) {
		int c = name.compareTo(another.name);
		if (c == 0) {
			c = super.compareTo(another);
		}
		return c;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		super.writeToParcel(out, flags);
		out.writeString(name);
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
	 * Gets the name of the exercise
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

}
