package strength.history.data.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import strength.history.data.db.entry.NameColumn;
import strength.history.data.db.entry.SyncColumns;
import strength.history.data.structure.Exercise;

/**
 * DB helper for Exercise
 */
public class ExerciseDBHelper extends DBHelperBase<Exercise> {
	private interface Entry extends BaseColumns, NameColumn, SyncColumns {
		public static final String TABLE_NAME = "exercise";
		public static final String[] ALL_COLUMNS = new String[] { _ID, SYNC,
				NAME };
	}

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "exercise.db";

	/**
	 * Singleton instance
	 */
	private static ExerciseDBHelper instance = null;

	private ExerciseDBHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}

	/**
	 * Static access to the instance
	 * 
	 * @param context
	 * @return The instance
	 */
	public static ExerciseDBHelper getInstance(Context context) {
		// Singleton pattern
		if (instance == null) {
			synchronized (ExerciseDBHelper.class) {
				if (instance == null) {
					instance = new ExerciseDBHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	protected ContentValues toContentValues(Exercise exercise) {
		ContentValues values = new ContentValues();
		values.put(Entry.SYNC, exercise.getSync());
		values.put(Entry.NAME, exercise.getName());

		return values;
	}

	@Override
	public boolean delete(Exercise exercise) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.delete(Entry.TABLE_NAME, Entry._ID + "=?",
				new String[] { Long.toString(exercise.getId()) });
		return rows != 0;
	}

	@Override
	public boolean insert(Exercise exercise) {
		SQLiteDatabase db = instance.getWritableDatabase();
		long id = db.insert(Entry.TABLE_NAME, null,
				instance.toContentValues(exercise));
		exercise.setId(id);
		return id != -1;
	}

	@Override
	public ArrayList<Exercise> query(int offset, int limit) {
		ArrayList<Exercise> res = new ArrayList<Exercise>();
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.query(Entry.TABLE_NAME, Entry.ALL_COLUMNS, null,
				null, null, null, null, offset + ", " + limit);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			int sync = cursor.getInt(1);
			String name = cursor.getString(2);
			res.add(new Exercise(id, sync, name));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return res;
	}

	@Override
	public boolean update(Exercise exercise) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.update(Entry.TABLE_NAME,
				instance.toContentValues(exercise), Entry._ID + "=?",
				new String[] { Long.toString(exercise.getId()) });
		return rows != 0;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Entry.TABLE_NAME + " (" + Entry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Entry.SYNC
				+ " INTEGER NOT NULL, " + Entry.NAME + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e(this.getClass().getName(), "onUpgrade not supported");
	}
}
