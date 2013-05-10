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
	 * Converts e to content values
	 * 
	 * @param e
	 *            Object to convert
	 * @return The content values
	 */
	protected abstract ContentValues toContentValues(E e);

	public abstract boolean delete(E e);

	public abstract boolean insert(E e);

	public abstract ArrayList<E> query(int offset, int limit);

	public abstract boolean update(E e);

}
