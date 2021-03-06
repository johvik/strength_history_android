package strength.history.data.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import strength.history.data.db.entry.NameColumn;
import strength.history.data.db.entry.SyncColumns;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.SyncBase.State;

/**
 * DB helper for Exercise
 */
public class ExerciseDBHelper extends DBHelperBase<Exercise> {
	private interface Entry extends BaseColumns, NameColumn, SyncColumns {
		static final String TABLE_NAME = "exercise";
		static final String STANDARD_INCREASE = "standard_inc";
		static final String[] ALL_COLUMNS = new String[] { _ID, SYNC, NAME,
				STANDARD_INCREASE };
	}

	public static final String DATABASE_NAME = "exercise.db";
	private static final int DATABASE_VERSION = 1;

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
	protected ContentValues toContentValues(Exercise e) {
		ContentValues values = new ContentValues();
		values.put(Entry.SYNC, e.getSync());
		values.put(Entry.NAME, e.getName());
		values.put(Entry.STANDARD_INCREASE, e.getStandardIncrease());

		return values;
	}

	@Override
	protected String getDBFileName() {
		return DATABASE_NAME;
	}

	@Override
	public boolean delete(Exercise e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.delete(Entry.TABLE_NAME, Entry._ID + "=?",
				new String[] { Long.toString(e.getId()) });
		db.close();
		return rows != 0;
	}

	private static boolean insert(SQLiteDatabase db, Exercise e) {
		long id = db
				.insert(Entry.TABLE_NAME, null, instance.toContentValues(e));
		e.setId(id);
		return id != -1;
	}

	@Override
	public boolean insert(Exercise e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		boolean inserted = insert(db, e);
		db.close();
		return inserted;
	}

	@Override
	public void purge() {
		SQLiteDatabase db = instance.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
			onCreate(db);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
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
			long sync = cursor.getLong(1);
			String name = cursor.getString(2);
			double standardIncrease = cursor.getDouble(3);
			// TODO Add serverId and state to the db
			String serverId = "";
			State state = State.NEW;
			res.add(new Exercise(id, sync, serverId, state, name,
					standardIncrease));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return res;
	}

	@Override
	public boolean update(Exercise e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		int rows = db.update(Entry.TABLE_NAME, instance.toContentValues(e),
				Entry._ID + "=?", new String[] { Long.toString(e.getId()) });
		db.close();
		return rows != 0;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Entry.TABLE_NAME + " (" + Entry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Entry.SYNC
				+ " INTEGER NOT NULL, " + Entry.NAME + " TEXT NOT NULL, "
				+ Entry.STANDARD_INCREASE + " REAL NOT NULL);");
	}

	public ArrayList<Exercise> createDefaults() {
		ArrayList<Exercise> res = new ArrayList<Exercise>();
		res.add(new Exercise("Overhead Press", Exercise.DEFAULT_INCREASE));
		res.add(new Exercise("Bench Press", Exercise.DEFAULT_INCREASE));
		res.add(new Exercise("Bent-Over Row", Exercise.DEFAULT_INCREASE));
		res.add(new Exercise("Squat", Exercise.DEFAULT_INCREASE));
		res.add(new Exercise("Dead Lift", 5.0));

		for (Exercise e : res) {
			insert(e);
		}
		return res;
	}
}
