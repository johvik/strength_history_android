package strength.history.data.structure;

import android.os.Parcelable;

public interface Base<T> extends Comparable<T>, Parcelable {
	/**
	 * Moves values into the object
	 * 
	 * @param t
	 *            The new values
	 */
	public void updateFrom(T t);

	/**
	 * Creates a new object with the same properties
	 * 
	 * @return The new object
	 */
	public T copy();

	/**
	 * Stores a local copy of the object for later reverts
	 */
	public void backup();

	/**
	 * Deletes the backup
	 */
	public void commit();

	/**
	 * Restores the data from the backup
	 */
	public void revert();

	/**
	 * Gets the id of the object
	 * 
	 * @return The id
	 */
	public long getId();

	/**
	 * Changes the id of the object
	 * 
	 * @param id
	 *            The new id
	 */
	public void setId(long id);

	/**
	 * Gets the sync state of the object
	 * 
	 * @return The sync state
	 */
	public int getSync();

	/**
	 * Changes the sync state of the object
	 * 
	 * @param sync
	 *            The new sync state
	 */
	public void setSync(int sync);
}
