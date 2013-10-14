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

public class Workout extends SyncBase<Workout> implements List<Long> {
	private static final String JSON_NAME = "name";
	private static final String JSON_EXERCISES_ID = "exercises";
	private String name;
	private ArrayList<Long> exercise_ids = new ArrayList<Long>();

	// TODO How to handle exercise_ids vs server ids??

	public Workout(String name) {
		this(-1, new Date().getTime(), "", name);
	}

	public Workout(long id, long sync, String serverId, String name) {
		super(id, sync, serverId);
		this.name = name;
	}

	protected Workout(Parcel in) {
		super(in);
		name = in.readString();
		in.readList(exercise_ids, Long.class.getClassLoader());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof Workout)) {
			return false;
		} else {
			return compareTo((Workout) o) == 0;
		}
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(JSON_ID, getId());
		object.put(JSON_SYNC, getSync());
		object.put(JSON_SERVER_ID, getServerId());
		object.put(JSON_NAME, name);
		JSONArray array = new JSONArray();
		for (Long e : exercise_ids) {
			array.put(e);
		}
		object.put(JSON_EXERCISES_ID, array);
		return object;
	}

	public static final Workout fromJSON(JSONObject object)
			throws JSONException {
		long id;
		try {
			id = object.getLong(JSON_ID);
		} catch (JSONException e) {
			id = -1;
		}
		long sync = object.getLong(JSON_SYNC);
		String serverId = object.getString(JSON_SERVER_ID);
		String name = object.getString(JSON_NAME);
		Workout w = new Workout(id, sync, serverId, name);
		JSONArray array = object.getJSONArray(JSON_EXERCISES_ID);
		for (int i = 0, j = array.length(); i < j; i++) {
			w.add(array.getLong(i));
		}
		return w;
	}

	@Override
	protected Workout _copy() {
		Workout copy = new Workout(getId(), getSync(), getServerId(), name);
		for (Long l : exercise_ids) {
			copy.add(l.longValue());
		}
		return copy;
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

	public void setName(String name) {
		this.name = name;
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
