package strength.history.data.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class Workout extends Base<Workout> implements List<Long> {
	private String name;
	private ArrayList<Long> exercise_ids = new ArrayList<Long>();

	public Workout(String name) {
		this(-1, Sync.NEW, name);
	}

	public Workout(long id, int sync, String name) {
		super(id, sync);
		this.name = name;
	}

	protected Workout(Parcel in) {
		super(in);
		name = in.readString();
		in.readList(exercise_ids, Long.class.getClassLoader());
	}

	@Override
	public String toString() {
		return "Workout=" + getId() + ":" + getSync() + " " + name + " "
				+ exercise_ids;
	}

	@Override
	protected Workout _copy() {
		Workout copy = new Workout(getId(), getSync(), name);
		for (Long l : exercise_ids) {
			copy.add(l.longValue());
		}
		return copy;
	}

	@Override
	protected int _compareTo(Workout another) {
		// Don't care about the array...
		return name.compareTo(another.name);
	}

	@Override
	protected void _updateFrom(Workout another) {
		name = another.name;
		exercise_ids = another.exercise_ids;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeList(exercise_ids);
	}

	/**
	 * 
	 */
	public static final Parcelable.Creator<Workout> CREATOR = new Parcelable.Creator<Workout>() {
		@Override
		public Workout createFromParcel(Parcel in) {
			return new Workout(in);
		}

		@Override
		public Workout[] newArray(int size) {
			return new Workout[size];
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

	/*
	 * *************************************************************
	 * 
	 * The rest is just methods implemented for the list interface *
	 * 
	 * *************************************************************
	 */

	@Override
	public boolean add(Long object) {
		return exercise_ids.add(object);
	}

	@Override
	public void add(int location, Long object) {
		exercise_ids.add(location, object);
	}

	@Override
	public boolean addAll(Collection<? extends Long> arg0) {
		return exercise_ids.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends Long> arg1) {
		return exercise_ids.addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		exercise_ids.clear();
	}

	@Override
	public boolean contains(Object object) {
		return exercise_ids.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return exercise_ids.containsAll(arg0);
	}

	@Override
	public Long get(int location) {
		return exercise_ids.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return exercise_ids.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return exercise_ids.isEmpty();
	}

	@Override
	public Iterator<Long> iterator() {
		return exercise_ids.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return exercise_ids.lastIndexOf(object);
	}

	@Override
	public ListIterator<Long> listIterator() {
		return exercise_ids.listIterator();
	}

	@Override
	public ListIterator<Long> listIterator(int location) {
		return exercise_ids.listIterator(location);
	}

	@Override
	public Long remove(int location) {
		return exercise_ids.remove(location);
	}

	@Override
	public boolean remove(Object object) {
		return exercise_ids.remove(object);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return exercise_ids.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return exercise_ids.retainAll(arg0);
	}

	@Override
	public Long set(int location, Long object) {
		return exercise_ids.set(location, object);
	}

	@Override
	public int size() {
		return exercise_ids.size();
	}

	@Override
	public List<Long> subList(int start, int end) {
		return exercise_ids.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return exercise_ids.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return exercise_ids.toArray(array);
	}
}
