package strength.history.data.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseData extends Base<ExerciseData> implements List<SetData> {
	private long workout_data_id;
	private long exercise_id;
	private ArrayList<SetData> sets = new ArrayList<SetData>();

	public ExerciseData(long id, long workout_data_id, long exercise_id) {
		super(id);
		this.workout_data_id = workout_data_id;
		this.exercise_id = exercise_id;
	}

	protected ExerciseData(Parcel in) {
		super(in);
		workout_data_id = in.readLong();
		exercise_id = in.readLong();
		in.readTypedList(sets, SetData.CREATOR);
	}

	@Override
	public String toString() {
		return "ExerciseData=" + getId() + " " + workout_data_id + " "
				+ exercise_id + " " + sets;
	}

	@Override
	protected ExerciseData _copy() {
		ExerciseData copy = new ExerciseData(getId(), workout_data_id,
				exercise_id);
		for (SetData s : sets) {
			copy.add(s._copy());
		}
		return copy;
	}

	@Override
	protected void _updateFrom(ExerciseData another) {
		workout_data_id = another.workout_data_id;
		exercise_id = another.exercise_id;
		sets = another.sets;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeLong(workout_data_id);
		out.writeLong(exercise_id);
		out.writeTypedList(sets);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<ExerciseData> CREATOR = new Parcelable.Creator<ExerciseData>() {
		@Override
		public ExerciseData createFromParcel(Parcel in) {
			return new ExerciseData(in);
		}

		@Override
		public ExerciseData[] newArray(int size) {
			return new ExerciseData[size];
		}
	};

	@Override
	public int compareTo(ExerciseData another) {
		// Don't care about array
		int c = Long.valueOf(getId()).compareTo(another.getId());
		if (c == 0) {
			c = Long.valueOf(workout_data_id)
					.compareTo(another.workout_data_id);
			if (c == 0) {
				c = Long.valueOf(exercise_id).compareTo(another.exercise_id);
			}
		}
		return c;
	}

	public long getWorkoutDataId() {
		return workout_data_id;
	}

	public long getExerciseId() {
		return exercise_id;
	}

	/*
	 * *************************************************************
	 * 
	 * The rest is just methods implemented for the list interface *
	 * 
	 * *************************************************************
	 */

	@Override
	public boolean add(SetData object) {
		return sets.add(object);
	}

	@Override
	public void add(int location, SetData object) {
		sets.add(location, object);
	}

	@Override
	public boolean addAll(Collection<? extends SetData> arg0) {
		return sets.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends SetData> arg1) {
		return sets.addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		sets.clear();
	}

	@Override
	public boolean contains(Object object) {
		return sets.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return sets.containsAll(arg0);
	}

	@Override
	public SetData get(int location) {
		return sets.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return sets.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return sets.isEmpty();
	}

	@Override
	public Iterator<SetData> iterator() {
		return sets.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return sets.lastIndexOf(object);
	}

	@Override
	public ListIterator<SetData> listIterator() {
		return sets.listIterator();
	}

	@Override
	public ListIterator<SetData> listIterator(int location) {
		return sets.listIterator(location);
	}

	@Override
	public SetData remove(int location) {
		return sets.remove(location);
	}

	@Override
	public boolean remove(Object object) {
		return sets.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return sets.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return sets.retainAll(arg0);
	}

	@Override
	public SetData set(int location, SetData object) {
		return sets.set(location, object);
	}

	@Override
	public int size() {
		return sets.size();
	}

	@Override
	public List<SetData> subList(int start, int end) {
		return sets.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return sets.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return sets.toArray(array);
	}
}
