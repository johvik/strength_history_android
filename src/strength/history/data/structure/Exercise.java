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
	public String toString() {
		return "Exercise=" + getId() + ":" + getSync() + " " + name;
	}

	@Override
	protected Exercise _copy() {
		return new Exercise(getId(), getSync(), name);
	}

	@Override
	protected int _compareTo(Exercise another) {
		return name.compareTo(another.name);
	}

	@Override
	protected void _updateFrom(Exercise another) {
		name = another.name;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
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
	 * Gets the name
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

}
