package strength.history.data.db;

import java.util.ArrayList;

import strength.history.data.db.entry.SyncColumns;
import strength.history.data.db.entry.TimeColumn;
import strength.history.data.structure.Weight;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class WeightDBHelper extends DBHelperBase<Weight> {
	public abstract class WeightEntry implements BaseColumns, SyncColumns,
			TimeColumn {
		private WeightEntry() {
		}

		public static final String TABLE_NAME = "weight";
		public static final String WEIGHT = "weight";
	}

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "weight.db";
	public static final String[] ALL_COLUMNS = new String[] { WeightEntry._ID,
			WeightEntry.TIME, WeightEntry.WEIGHT, WeightEntry.SYNC };

	private static WeightDBHelper instance = null;

	private WeightDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + WeightEntry.TABLE_NAME + " ("
				+ WeightEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ WeightEntry.TIME + " INTEGER NOT NULL, " + WeightEntry.WEIGHT
				+ " REAL NOT NULL, " + WeightEntry.SYNC + " INTEGER NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("WeightDBHelper", "onUpgrade not supported");
	}

	@Override
	protected ContentValues toContentValues(Weight weight) {
		ContentValues values = new ContentValues();
		values.put(WeightEntry.TIME, weight.getTime());
		values.put(WeightEntry.WEIGHT, weight.getWeight());
		values.put(WeightEntry.SYNC, weight.getSync());

		return values;
	}

	@Override
	public boolean delete(Weight weight) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.delete(WeightEntry.TABLE_NAME, WeightEntry._ID + "=?",
				new String[] { Long.toString(weight.getId()) });
		return rows != 0;
	}

	@Override
	public boolean insert(Weight weight) {
		SQLiteDatabase db = instance.getWritableDatabase();
		long id = db.insert(WeightEntry.TABLE_NAME, null,
				instance.toContentValues(weight));
		weight.setId(id);
		return id != -1;
	}

	@Override
	public ArrayList<Weight> query(int offset, int limit) {
		ArrayList<Weight> res = new ArrayList<Weight>();
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.query(WeightEntry.TABLE_NAME,
				WeightDBHelper.ALL_COLUMNS, null, null, null, null,
				WeightEntry.TIME + " DESC, " + WeightEntry._ID + " ASC", offset
						+ ", " + limit);

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
		int rows = db.update(WeightEntry.TABLE_NAME,
				instance.toContentValues(weight), WeightEntry._ID + "=?",
				new String[] { Long.toString(weight.getId()) });
		return rows != 0;
	}
}
