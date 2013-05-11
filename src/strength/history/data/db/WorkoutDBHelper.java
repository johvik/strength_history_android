package strength.history.data.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import strength.history.data.db.entry.NameColumn;
import strength.history.data.db.entry.SyncColumns;
import strength.history.data.structure.Workout;

public class WorkoutDBHelper extends DBHelperBase<Workout> {
	private interface Entry extends BaseColumns, NameColumn, SyncColumns {
		public static final String TABLE_NAME = "workout";
		public static final String[] ALL_COLUMNS = new String[] { _ID, SYNC,
				NAME };

		public interface Binding extends BaseColumns {
			public static final String TABLE_NAME = "binding";
			public static final String WORKOUT_ID = "workout_id";
			public static final String EXERCISE_ID = "exercise_id";
		}
	}

	private static final String DATABASE_NAME = "workout.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * Singleton instance
	 */
	private static WorkoutDBHelper instance = null;

	private WorkoutDBHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}

	/**
	 * Static access to the instance
	 * 
	 * @param context
	 * @return The instance
	 */
	public static WorkoutDBHelper getInstance(Context context) {
		// Singleton pattern
		if (instance == null) {
			synchronized (WorkoutDBHelper.class) {
				if (instance == null) {
					instance = new WorkoutDBHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	protected ContentValues toContentValues(Workout e) {
		ContentValues values = new ContentValues();
		values.put(Entry.SYNC, e.getSync());
		values.put(Entry.NAME, e.getName());

		return values;
	}

	@Override
	public boolean delete(Workout e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		String[] id_str = new String[] { Long.toString(e.getId()) };
		int rows = db.delete(Entry.TABLE_NAME, Entry._ID + "=?", id_str);
		db.delete(Entry.Binding.TABLE_NAME, Entry.Binding.WORKOUT_ID + "=?",
				id_str);
		db.close();
		return rows != 0;
	}

	@Override
	public boolean insert(Workout e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		long id = db.insert(Entry.TABLE_NAME, null, toContentValues(e));
		e.setId(id);
		if (id != -1) {
			for (Long l : e) {
				ContentValues values = new ContentValues();
				values.put(Entry.Binding.WORKOUT_ID, id);
				values.put(Entry.Binding.EXERCISE_ID, l);
				db.insert(Entry.Binding.TABLE_NAME, null, values);
			}
		}
		db.close();
		return id != -1;
	}

	@Override
	public ArrayList<Workout> query(int offset, int limit) {
		ArrayList<Workout> res = new ArrayList<Workout>();
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.query(Entry.TABLE_NAME, Entry.ALL_COLUMNS, null,
				null, null, null, null, offset + ", " + limit);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			int sync = cursor.getInt(1);
			String name = cursor.getString(2);
			Workout w = new Workout(id, sync, name);
			// Load bindings
			Cursor c2 = db.query(Entry.Binding.TABLE_NAME,
					new String[] { Entry.Binding.EXERCISE_ID },
					Entry.Binding.WORKOUT_ID + "=?",
					new String[] { Long.toString(id) }, null, null, null);

			c2.moveToFirst();
			while (!c2.isAfterLast()) {
				w.add(cursor.getLong(0));
				c2.moveToNext();
			}
			c2.close();

			res.add(w);
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return res;
	}

	@Override
	public boolean update(Workout e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		long id = e.getId();
		String[] id_str = new String[] { Long.toString(id) };
		int rows = db.update(Entry.TABLE_NAME, instance.toContentValues(e),
				Entry._ID + "=?", id_str);
		// Delete old bindings
		db.delete(Entry.Binding.TABLE_NAME, Entry.Binding.WORKOUT_ID + "=?",
				id_str);
		// Store new ones
		for (Long l : e) {
			ContentValues values = new ContentValues();
			values.put(Entry.Binding.WORKOUT_ID, id);
			values.put(Entry.Binding.EXERCISE_ID, l);
			db.insert(Entry.Binding.TABLE_NAME, null, values);
		}
		db.close();
		return rows != 0;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Entry.TABLE_NAME + " (" + Entry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Entry.SYNC
				+ " INTEGER NOT NULL, " + Entry.NAME + " TEXT NOT NULL);");

		db.execSQL("CREATE TABLE " + Entry.Binding.TABLE_NAME + " ("
				+ Entry.Binding._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Entry.Binding.WORKOUT_ID + " INTEGER NOT NULL, "
				+ Entry.Binding.EXERCISE_ID + " INTEGER NOT NULL);");
	}
}
