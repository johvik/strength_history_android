package strength.history.data.db;

import java.util.ArrayList;

import strength.history.data.db.entry.SyncColumns;
import strength.history.data.db.entry.TimeColumn;
import strength.history.data.db.entry.WeightColumn;
import strength.history.data.structure.Weight;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class WeightDBHelper extends DBHelperBase<Weight> {
	private interface Entry extends BaseColumns, TimeColumn, WeightColumn,
			SyncColumns {
		public static final String TABLE_NAME = "weight";
		public static final String[] ALL_COLUMNS = new String[] { _ID, TIME,
				WEIGHT, SYNC };
	}

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "weight.db";

	/**
	 * Singleton instance
	 */
	private static WeightDBHelper instance = null;

	private WeightDBHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}

	public static WeightDBHelper getInstance(Context context) {
		// Singleton pattern
		if (instance == null) {
			synchronized (WeightDBHelper.class) {
				if (instance == null) {
					instance = new WeightDBHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	protected ContentValues toContentValues(Weight weight) {
		ContentValues values = new ContentValues();
		values.put(Entry.TIME, weight.getTime());
		values.put(Entry.WEIGHT, weight.getWeight());
		values.put(Entry.SYNC, weight.getSync());

		return values;
	}

	@Override
	public boolean delete(Weight weight) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.delete(Entry.TABLE_NAME, Entry._ID + "=?",
				new String[] { Long.toString(weight.getId()) });
		return rows != 0;
	}

	@Override
	public boolean insert(Weight weight) {
		SQLiteDatabase db = instance.getWritableDatabase();
		long id = db.insert(Entry.TABLE_NAME, null,
				instance.toContentValues(weight));
		weight.setId(id);
		return id != -1;
	}

	@Override
	public ArrayList<Weight> query(int offset, int limit) {
		ArrayList<Weight> res = new ArrayList<Weight>();
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.query(Entry.TABLE_NAME, Entry.ALL_COLUMNS, null,
				null, null, null, Entry.TIME + " DESC, " + Entry._ID + " ASC",
				offset + ", " + limit);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			long time = cursor.getLong(1);
			double weight = cursor.getDouble(2);
			int sync = cursor.getInt(3);
			res.add(new Weight(id, time, weight, sync));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return res;
	}

	@Override
	public boolean update(Weight weight) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.update(Entry.TABLE_NAME,
				instance.toContentValues(weight), Entry._ID + "=?",
				new String[] { Long.toString(weight.getId()) });
		return rows != 0;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Entry.TABLE_NAME + " (" + Entry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Entry.TIME
				+ " INTEGER NOT NULL, " + Entry.WEIGHT + " REAL NOT NULL, "
				+ Entry.SYNC + " INTEGER NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e(this.getClass().getName(), "onUpgrade not supported");
	}
}
