package strength.history.data.structure;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Exercise class
 */
public class Exercise extends SyncBase<Exercise> {
	public static double DEFAULT_INCREASE = 2.5;

	private static final String JSON_NAME = "name";
	private static final String JSON_STANDARD_INCREASE = "standardIncrease";
	private String name;
	private double standardIncrease;

	/**
	 * Constructs a new exercise
	 * 
	 * @param name
	 * @param standardIncrease
	 */
	public Exercise(String name, double standardIncrease) {
		this(-1, new Date().getTime(), name, standardIncrease);
	}

	/**
	 * Constructs a new exercise
	 * 
	 * @param id
	 * @param sync
	 * @param name
	 * @param standardIncrease
	 */
	public Exercise(long id, long sync, String name, double standardIncrease) {
		super(id, sync);
		this.name = name;
		this.standardIncrease = standardIncrease;
	}

	protected Exercise(Parcel in) {
		super(in);
		name = in.readString();
		standardIncrease = in.readDouble();
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
	public JSONObject toJSON() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(JSON_ID, getId());
		object.put(JSON_SYNC, getSync());
		object.put(JSON_NAME, name);
		object.put(JSON_STANDARD_INCREASE, standardIncrease);
		return object;
	}

	public static final Exercise fromJSON(JSONObject object)
			throws JSONException {
		long id = object.getLong(JSON_ID);
		long sync = object.getLong(JSON_SYNC);
		String name = object.getString(JSON_NAME);
		double standardIncrease = object.getDouble(JSON_STANDARD_INCREASE);
		return new Exercise(id, sync, name, standardIncrease);
	}

	@Override
	protected Exercise _copy() {
		return new Exercise(getId(), getSync(), name, standardIncrease);
	}

	@Override
	protected void _updateFrom(Exercise another) {
		name = another.name;
		standardIncrease = another.standardIncrease;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeString(name);
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

	public double getStandardIncrease() {
		return standardIncrease;
	}

	public void setStandardIncrease(double standardIncrease) {
		this.standardIncrease = standardIncrease;
	}
}
