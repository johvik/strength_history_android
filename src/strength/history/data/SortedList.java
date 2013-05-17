package strength.history.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class SortedList<E> implements Iterable<E> {
	private final ArrayList<E> list = new ArrayList<E>();
	private final Comparator<E> comparator;
	private final boolean unique;

	public SortedList(Comparator<E> comparator) {
		this(comparator, false);
	}

	public SortedList(Comparator<E> comparator, boolean unique) {
		this.comparator = comparator;
		this.unique = unique;
	}

	public boolean add(E e) {
		int index = Collections.binarySearch(list, e, comparator);
		if (index < 0) {
			index = -(index + 1);
			list.add(index, e);
			return true;
		} else if (!unique) {
			list.add(index, e);
			return true;
		} else {
			return false;
		}
	}

	public void clear() {
		list.clear();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	public boolean remove(E e) {
		int index = Collections.binarySearch(list, e, comparator);
		if (index >= 0) {
			list.remove(index);
			return true;
		}
		return false;
	}

	public int size() {
		return list.size();
	}
}
