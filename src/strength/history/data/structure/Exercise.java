package strength.history.data.structure;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Exercise class
 */
public class Exercise implements Base<Exercise> {
	private long id;
	private String name;
	private int sync;
	private Exercise backup = null;

	/**
	 * Constructs a new exercise
	 * 
	 * @param name
	 */
	public Exercise(String name) {
		this(-1, name, Sync.NEW);
	}

	/**
	 * Constructs a new exercise
	 * 
	 * @param id
	 * @param name
	 * @param sync
	 */
	public Exercise(long id, String name, int sync) {
		this.id = id;
		this.name = name;
		this.sync = sync;
	}

	private Exercise(Parcel in) {
		id = in.readLong();
		name = in.readString();
		sync = in.readInt();
	}

	@Override
	public int compareTo(Exercise another) {
		int c = name.compareTo(another.name);
		if (c == 0) {
			c = Long.valueOf(id).compareTo(another.id);
			if (c == 0) {
				c = Integer.valueOf(sync).compareTo(another.sync);
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
		out.writeString(name);
		out.writeInt(sync);
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

	@Override
	public void updateFrom(Exercise another) {
		id = another.id;
		name = another.name;
		sync = another.sync;
	}

	@Override
	public Exercise copy() {
		return new Exercise(id, name, sync);
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
		return "Exercise=" + id + " " + name + " " + sync;
	}

	/**
	 * Gets the name of the exercise
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

}
