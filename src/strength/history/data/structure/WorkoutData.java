package strength.history.data.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkoutData extends SyncBase<WorkoutData> implements
		List<ExerciseData> {
	private long time;
	private long workout_id;
	private ArrayList<ExerciseData> exercises = new ArrayList<ExerciseData>();

	public WorkoutData(long time, long workout_id) {
		this(-1, Sync.NEW, time, workout_id);
	}

	public WorkoutData(long id, int sync, long time, long workout_id) {
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
	public String toString() {
		return "WorkoutData=" + getId() + ":" + getSync() + " "
				+ new Date(time).toLocaleString() + " " + workout_id + " "
				+ exercises;
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

	@Override
	public int compareTo(WorkoutData another) {
		// Don't care about array
		int c = Long.valueOf(another.time).compareTo(time); // descending time
		if (c == 0) {
			c = Long.valueOf(workout_id).compareTo(another.workout_id);
			if (c == 0) {
				c = Long.valueOf(getId()).compareTo(another.getId());
				if (c == 0) {
					c = Integer.valueOf(getSync()).compareTo(another.getSync());
				}
			}
		}
		return c;
	}

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
