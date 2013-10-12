package strength.history.data.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ExerciseData extends Base<ExerciseData> implements List<SetData> {
	private static final String JSON_EXERCISE_ID = "exercise";
	private static final String JSON_SETS = "sets";
	private long exercise_id;
	private ArrayList<SetData> sets = new ArrayList<SetData>();

	public ExerciseData(long exercise_id) {
		this(-1, exercise_id);
	}

	public ExerciseData(long id, long exercise_id) {
		super(id);
		this.exercise_id = exercise_id;
	}

	protected ExerciseData(Parcel in) {
		super(in);
		exercise_id = in.readLong();
		in.readTypedList(sets, SetData.CREATOR);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (!(o instanceof ExerciseData)) {
			return false;
		} else {
			ExerciseData d = (ExerciseData) o;
			return getId() == d.getId();
		}
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject object = new JSONObject();
		object.put(JSON_EXERCISE_ID, exercise_id);
		JSONArray array = new JSONArray();
		for (SetData s : sets) {
			array.put(s.toJSON());
		}
		object.put(JSON_SETS, array);
		return object;
	}

	public static final ExerciseData fromJSON(JSONObject object)
			throws JSONException {
		long exercise_id = object.getLong(JSON_EXERCISE_ID);
		ExerciseData e = new ExerciseData(exercise_id);
		JSONArray array = object.getJSONArray(JSON_SETS);
		for (int i = 0, j = array.length(); i < j; i++) {
			e.add(SetData.fromJSON(array.getJSONObject(i)));
		}
		return e;
	}

	@Override
	protected ExerciseData _copy() {
		ExerciseData copy = new ExerciseData(getId(), exercise_id);
		for (SetData s : sets) {
			copy.add(s._copy());
		}
		return copy;
	}

	@Override
	protected void _updateFrom(ExerciseData another) {
		exercise_id = another.exercise_id;
		sets = another.sets;
	}

	@Override
	protected void _writeToParcel(Parcel out, int flags) {
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

	public long getExerciseId() {
		return exercise_id;
	}

	public SetData getBestWeightSet() {
		int size = sets.size();
		if (size == 0) {
			return null;
		}
		SetData res = sets.get(0);
		double best = res.getWeight();
		for (int i = 1; i < size; i++) {
			SetData si = sets.get(i);
			double w = si.getWeight();
			if (w > best) {
				res = si;
				best = w;
			} else if (w == best) {
				if (si.getRepetitions() > res.getRepetitions()) {
					res = si;
				}
			}
		}
		return res;
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
