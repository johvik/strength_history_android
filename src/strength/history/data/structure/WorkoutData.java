package strength.history.data.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkoutData extends SyncBase<WorkoutData> implements
		List<ExerciseData> {
	private static final String JSON_TIME = "time";
	private static final String JSON_WORKOUT_ID = "workout";
	private static final String JSON_EXERCISE_DATA = "data";
	private long time;
	private long workout_id;
	private ArrayList<ExerciseData> exercises = new ArrayList<ExerciseData>();

	public WorkoutData(long time, long workout_id) {
		this(-1, new Date().getTime(), time, workout_id);
	}

	public WorkoutData(long id, long sync, long time, long workout_id) {
		super(id, sync);
		this.time = time;
		this.workout_id = workout_id;
	}

	protected WorkoutData(Parcel in) {
		super(in);
		time = in.readLong();
		workout_id = in.readLong();
		in.readTypedList(exercises, ExerciseData.CREATOR);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof WorkoutData)) {
			return false;
		} else {
			WorkoutData d = (WorkoutData) o;
			return getId() == d.getId() && time == d.time;
		}
	}

	@Override
	public int hashCode() {
		long id = getId();
		int result = (int) (id ^ (id >>> 32));
		return result * 37 + (int) (time ^ (time >>> 32));
	}

	@Override
	public int compareTo(WorkoutData another) {
		// Don't care about array
		int c = Long.valueOf(another.time).compareTo(time); // descending time
		if (c == 0) {
			return Long.valueOf(getId()).compareTo(another.getId());
		}
		return c;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(JSON_ID, getId());
		object.put(JSON_SYNC, getSync());
		object.put(JSON_TIME, time);
		object.put(JSON_WORKOUT_ID, workout_id);
		JSONArray array = new JSONArray();
		for (ExerciseData e : exercises) {
			array.put(e.toJSON());
		}
		object.put(JSON_EXERCISE_DATA, array);
		return object;
	}

	public static final WorkoutData fromJSON(JSONObject object)
			throws JSONException {
		long id = object.getLong(JSON_ID);
		long sync = object.getLong(JSON_SYNC);
		long time = object.getLong(JSON_TIME);
		long workout_id = object.getLong(JSON_WORKOUT_ID);
		WorkoutData w = new WorkoutData(id, sync, time, workout_id);
		JSONArray array = object.getJSONArray(JSON_EXERCISE_DATA);
		for (int i = 0, j = array.length(); i < j; i++) {
			w.add(ExerciseData.fromJSON(array.getJSONObject(i)));
		}
		return w;
	}

	@Override
	protected WorkoutData _copy() {
		WorkoutData copy = new WorkoutData(getId(), getSync(), time, workout_id);
		for (ExerciseData e : exercises) {
			copy.add(e._copy());
		}
		return copy;
	}

	@Override
	protected void _updateFrom(WorkoutData another) {
		time = another.time;
		workout_id = another.workout_id;
		exercises = another.exercises;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeLong(time);
		out.writeLong(workout_id);
		out.writeTypedList(exercises);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<WorkoutData> CREATOR = new Parcelable.Creator<WorkoutData>() {
		@Override
		public WorkoutData createFromParcel(Parcel in) {
			return new WorkoutData(in);
		}

		@Override
		public WorkoutData[] newArray(int size) {
			return new WorkoutData[size];
		}
	};

	public long getTime() {
		return time;
	}

	public long getWorkoutId() {
		return workout_id;
	}

	/*
	 * *************************************************************
	 * 
	 * The rest is just methods implemented for the list interface *
	 * 
	 * *************************************************************
	 */

	@Override
	public boolean add(ExerciseData object) {
		return exercises.add(object);
	}

	@Override
	public void add(int location, ExerciseData object) {
		exercises.add(location, object);
	}

	@Override
	public boolean addAll(Collection<? extends ExerciseData> arg0) {
		return exercises.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends ExerciseData> arg1) {
		return exercises.addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		exercises.clear();
	}

	@Override
	public boolean contains(Object object) {
		return exercises.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return exercises.containsAll(arg0);
	}

	@Override
	public ExerciseData get(int location) {
		return exercises.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return exercises.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return exercises.isEmpty();
	}

	@Override
	public Iterator<ExerciseData> iterator() {
		return exercises.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return exercises.lastIndexOf(object);
	}

	@Override
	public ListIterator<ExerciseData> listIterator() {
		return exercises.listIterator();
	}

	@Override
	public ListIterator<ExerciseData> listIterator(int location) {
		return exercises.listIterator(location);
	}

	@Override
	public ExerciseData remove(int location) {
		return exercises.remove(location);
	}

	@Override
	public boolean remove(Object object) {
		return exercises.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return exercises.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return exercises.retainAll(arg0);
	}

	@Override
	public ExerciseData set(int location, ExerciseData object) {
		return exercises.set(location, object);
	}

	@Override
	public int size() {
		return exercises.size();
	}

	@Override
	public List<ExerciseData> subList(int start, int end) {
		return exercises.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return exercises.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return exercises.toArray(array);
	}
}
