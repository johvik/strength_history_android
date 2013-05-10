package strength.history.data.structure;

import android.os.Parcelable;

public interface Base<T> extends Comparable<T>, Parcelable {
	public void updateFrom(T t);

	public T copy();

	public void backup();

	public void commit();

	public void revert();

	public long getId();

	public void setId(long id);

	public int getSync();

	public void setSync(int sync);
}
