package strength.history.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedList<E> implements List<E> {
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

	public SortedList(Comparator<E> comparator, Collection<E> list) {
		this(comparator, list, false);
	}

	public SortedList(Comparator<E> comparator, Collection<E> list,
			boolean unique) {
		this.comparator = comparator;
		this.unique = unique;
		for (E e : list) {
			this.list.add(e);
		}
	}

	@Override
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

	@Override
	public void add(int location, E object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		int old_size = list.size();
		for (E e : arg0) {
			add(e);
		}
		return old_size != list.size();
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object object) {
		return indexOf(object) != -1;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		for (Object e : arg0) {
			if (!contains(e)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public E get(int location) {
		return list.get(location);
	}

	@Override
	public int indexOf(Object object) {
		@SuppressWarnings("unchecked")
		int index = Collections.binarySearch(list, (E) object, comparator);
		if (index >= 0) {
			return index;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		@SuppressWarnings("unchecked")
		E e = (E) object;
		int index = Collections.binarySearch(list, e, comparator);
		if (index >= 0) {
			int i = index;
			for (int size = list.size(); i < size; i++) {
				E ei = list.get(i);
				if (comparator.compare(e, ei) != 0) {
					break; // not equal
				}
				index++;
			}
			return index;
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int location) {
		return list.listIterator(location);
	}

	@Override
	public E remove(int location) {
		return list.remove(location);
	}

	@Override
	public boolean remove(Object object) {
		int index = indexOf(object);
		if (index != -1) {
			list.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		int old_size = list.size();
		for (Object e : arg0) {
			remove(e);
		}
		return old_size != list.size();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E set(int location, E object) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<E> subList(int start, int end) {
		return list.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return list.toArray(array);
	}
}
