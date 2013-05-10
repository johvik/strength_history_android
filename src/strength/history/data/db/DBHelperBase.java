package strength.history.data.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DBHelperBase<E> extends SQLiteOpenHelper {

	public DBHelperBase(Context context, String name, int version) {
		super(context, name, null, version);
	}

	/**
	 * Converts it to content values
	 * 
	 * @param e
	 *            Object to convert
	 * @return The content values
	 */
	protected abstract ContentValues toContentValues(E e);

	/**
	 * Deletes it from the DB
	 * 
	 * @param e
	 *            Object to delete
	 * @return True if objects was deleted
	 */
	public abstract boolean delete(E e);

	/**
	 * Inserts it into the DB
	 * 
	 * @param e
	 *            Object to insert
	 * @return True if object was inserted
	 */
	public abstract boolean insert(E e);

	/**
	 * Makes a query to the DB
	 * 
	 * @param offset
	 *            Limit offset
	 * @param limit
	 *            Limit
	 * @return The list of resulting items
	 */
	public abstract ArrayList<E> query(int offset, int limit);

	/**
	 * Updates it in the DB
	 * 
	 * @param e
	 *            Object to update
	 * @return True if objects was updated
	 */
	public abstract boolean update(E e);

}
