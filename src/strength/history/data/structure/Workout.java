package strength.history.data.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class Workout extends Base<Workout> implements List<Exercise> {
	private String name;
	private ArrayList<Exercise> exercises = new ArrayList<Exercise>();

	public Workout(long id, int sync, String name) {
		super(id, sync);
		this.name = name;
	}

	protected Workout(Parcel in) {
		super(in);
		name = in.readString();
		in.readTypedList(exercises, Exercise.CREATOR);
	}

	@Override
	public String toString() {
		return "Workout=" + getId() + ":" + getSync() + " " + name + " "
				+ exercises;
	}

	@Override
	protected Workout _copy() {
		Workout copy = new Workout(getId(), getSync(), name);
		for (Exercise e : exercises) {
			copy.add(e._copy());
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
		exercises = another.exercises;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
		out.writeString(name);
		out.writeTypedList(exercises);
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
	public boolean add(Exercise object) {
		return exercises.add(object);
	}

	@Override
	public void add(int location, Exercise object) {
		exercises.add(location, object);
	}

	@Override
	public boolean addAll(Collection<? extends Exercise> arg0) {
		return exercises.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends Exercise> arg1) {
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
	public Exercise get(int location) {
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
	public Iterator<Exercise> iterator() {
		return exercises.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return exercises.lastIndexOf(object);
	}

	@Override
	public ListIterator<Exercise> listIterator() {
		return exercises.listIterator();
	}

	@Override
	public ListIterator<Exercise> listIterator(int location) {
		return exercises.listIterator(location);
	}

	@Override
	public Exercise remove(int location) {
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
	public Exercise set(int location, Exercise object) {
		return exercises.set(location, object);
	}

	@Override
	public int size() {
		return exercises.size();
	}

	@Override
	public List<Exercise> subList(int start, int end) {
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
