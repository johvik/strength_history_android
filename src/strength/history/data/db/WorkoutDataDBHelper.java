package strength.history.data.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import strength.history.data.db.entry.SyncColumns;
import strength.history.data.db.entry.TimeColumn;
import strength.history.data.db.entry.WeightColumn;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.WorkoutData;

public class WorkoutDataDBHelper extends DBHelperBase<WorkoutData> {
	private interface Entry extends BaseColumns, TimeColumn, SyncColumns {
		static final String TABLE_NAME = "workout_data";
		static final String WORKOUT_ID = "workout_id";
		static final String[] ALL_COLUMNS = new String[] { _ID, SYNC, TIME,
				WORKOUT_ID };

		interface ExerciseData extends BaseColumns {
			static final String TABLE_NAME = "exercise_data";
			static final String WORKOUT_DATA_ID = "workout_data_id";
			static final String EXERCISE_ID = "exercise_id";
			static final String[] ALL_COLUMNS = new String[] { _ID, EXERCISE_ID };

			interface SetData extends BaseColumns, WeightColumn {
				static final String TABLE_NAME = "set_data";
				static final String EXERCISE_DATA_ID = "exercise_data_id";
				static final String REPETITIONS = "repetitions";
				static final String[] ALL_COLUMNS = new String[] { _ID, WEIGHT,
						REPETITIONS };
			}
		}
	}

	private static final String DATABASE_NAME = "workout_data.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * Singleton instance
	 */
	private static WorkoutDataDBHelper instance = null;

	private WorkoutDataDBHelper(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}

	/**
	 * Static access to the instance
	 * 
	 * @param context
	 * @return The instance
	 */
	public static WorkoutDataDBHelper getInstance(Context context) {
		// Singleton pattern
		if (instance == null) {
			synchronized (WorkoutDataDBHelper.class) {
				if (instance == null) {
					instance = new WorkoutDataDBHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	protected ContentValues toContentValues(WorkoutData e) {
		ContentValues values = new ContentValues();
		values.put(Entry.SYNC, e.getSync());
		values.put(Entry.TIME, e.getTime());
		values.put(Entry.WORKOUT_ID, e.getWorkoutId());

		return values;
	}

	@Override
	public boolean delete(WorkoutData e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		String[] id_str = new String[] { Long.toString(e.getId()) };
		int rows = db.delete(Entry.TABLE_NAME, Entry._ID + "=?", id_str);
		db.delete(Entry.ExerciseData.TABLE_NAME,
				Entry.ExerciseData.WORKOUT_DATA_ID + "=?", id_str);
		for (ExerciseData d : e) {
			db.delete(Entry.ExerciseData.SetData.TABLE_NAME,
					Entry.ExerciseData.SetData.EXERCISE_DATA_ID + "=?",
					new String[] { Long.toString(d.getId()) });
		}
		db.close();
		return rows != 0;
	}

	@Override
	public boolean insert(WorkoutData e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		boolean ok = false;
		db.beginTransaction();
		try {
			long id = db.insert(Entry.TABLE_NAME, null, toContentValues(e));
			e.setId(id);
			ok = id != -1;
			if (id != -1) {
				for (ExerciseData d : e) {
					ContentValues values = new ContentValues();
					values.put(Entry.ExerciseData.WORKOUT_DATA_ID, id);
					values.put(Entry.ExerciseData.EXERCISE_ID,
							d.getExerciseId());
					long id2 = db.insert(Entry.ExerciseData.TABLE_NAME, null,
							values);
					d.setId(id2);
					ok = ok && id2 != -1;
					if (id2 != -1) {
						for (SetData s : d) {
							ContentValues values2 = new ContentValues();
							values2.put(
									Entry.ExerciseData.SetData.EXERCISE_DATA_ID,
									id2);
							values2.put(Entry.ExerciseData.SetData.WEIGHT,
									s.getWeight());
							values2.put(Entry.ExerciseData.SetData.REPETITIONS,
									s.getRepetitions());
							long id3 = db.insert(
									Entry.ExerciseData.SetData.TABLE_NAME,
									null, values2);
							s.setId(id3);
							ok = ok && id3 != -1;
						}
					}
				}
			}
			if (ok) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
		db.close();
		if (!ok) {
			// Restore all IDs if they changed...
			e.setId(-1);
			for (ExerciseData d : e) {
				d.setId(-1);
				for (SetData s : d) {
					s.setId(-1);
				}
			}
		}
		return ok;
	}

	@Override
	public void purge() {
		SQLiteDatabase db = instance.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + Entry.ExerciseData.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS "
					+ Entry.ExerciseData.SetData.TABLE_NAME);
			onCreate(db);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}

	/**
	 * Gets the latest non empty exercise data
	 * 
	 * @param exerciseQueryId
	 * @return null if nothing was found
	 */
	@SuppressWarnings("static-method")
	public ExerciseData latestExerciseData(long exerciseQueryId) {
		if (exerciseQueryId == -1) {
			return null;
		}
		// TODO Make the query smarter
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.query(Entry.TABLE_NAME, new String[] { Entry._ID },
				null, null, null, null, Entry.TIME + " desc", null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			// Load data
			Cursor c2 = db.query(Entry.ExerciseData.TABLE_NAME,
					Entry.ExerciseData.ALL_COLUMNS,
					Entry.ExerciseData.WORKOUT_DATA_ID + "=?",
					new String[] { Long.toString(id) }, null, null, null);

			c2.moveToFirst();
			while (!c2.isAfterLast()) {
				long id2 = c2.getLong(0);
				long exercise_id = c2.getLong(1);
				if (exercise_id == exerciseQueryId) {
					ExerciseData e = new ExerciseData(id2, exercise_id);
					// Load sets
					Cursor c3 = db.query(Entry.ExerciseData.SetData.TABLE_NAME,
							Entry.ExerciseData.SetData.ALL_COLUMNS,
							Entry.ExerciseData.SetData.EXERCISE_DATA_ID + "=?",
							new String[] { Long.toString(id2) }, null, null,
							null);

					c3.moveToFirst();
					while (!c3.isAfterLast()) {
						long id3 = c3.getLong(0);
						double weight = c3.getDouble(1);
						int repetitions = c3.getInt(2);

						e.add(new SetData(id3, weight, repetitions));
						c3.moveToNext();
					}
					c3.close();

					if (!e.isEmpty()) {
						// done!
						c2.close();
						cursor.close();
						db.close();
						return e;
					}
				}
				c2.moveToNext();
			}
			c2.close();
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return null;
	}

	/**
	 * 
	 * @param workoutQueryId
	 * @return null if nothing was found
	 */
	@SuppressWarnings("static-method")
	public WorkoutData latestWorkoutData(long workoutQueryId) {
		WorkoutData res = null;
		if (workoutQueryId == -1) {
			return res;
		}
		SQLiteDatabase db = instance.getReadableDatabase();

		Cursor cursor = db.query(Entry.TABLE_NAME, Entry.ALL_COLUMNS,
				Entry.WORKOUT_ID + "=?",
				new String[] { Long.toString(workoutQueryId) }, null, null,
				Entry.TIME + " desc", "1");

		if (cursor.moveToFirst()) {
			long id = cursor.getLong(0);
			int sync = cursor.getInt(1);
			long time = cursor.getLong(2);
			long workout_id = cursor.getLong(3);
			res = new WorkoutData(id, sync, time, workout_id);
			// Load data
			Cursor c2 = db.query(Entry.ExerciseData.TABLE_NAME,
					Entry.ExerciseData.ALL_COLUMNS,
					Entry.ExerciseData.WORKOUT_DATA_ID + "=?",
					new String[] { Long.toString(id) }, null, null, null);

			c2.moveToFirst();
			while (!c2.isAfterLast()) {
				long id2 = c2.getLong(0);
				long exercise_id = c2.getLong(1);
				ExerciseData e = new ExerciseData(id2, exercise_id);
				// Load sets
				Cursor c3 = db.query(Entry.ExerciseData.SetData.TABLE_NAME,
						Entry.ExerciseData.SetData.ALL_COLUMNS,
						Entry.ExerciseData.SetData.EXERCISE_DATA_ID + "=?",
						new String[] { Long.toString(id2) }, null, null, null);

				c3.moveToFirst();
				while (!c3.isAfterLast()) {
					long id3 = c3.getLong(0);
					double weight = c3.getDouble(1);
					int repetitions = c3.getInt(2);

					e.add(new SetData(id3, weight, repetitions));
					c3.moveToNext();
				}
				c3.close();

				res.add(e);
				c2.moveToNext();
			}
			c2.close();
		}
		cursor.close();
		db.close();
		return res;
	}

	@Override
	public ArrayList<WorkoutData> query(int offset, int limit) {
		ArrayList<WorkoutData> res = new ArrayList<WorkoutData>();
		SQLiteDatabase db = instance.getReadableDatabase();
		Cursor cursor = db.query(Entry.TABLE_NAME, Entry.ALL_COLUMNS, null,
				null, null, null, null, offset + ", " + limit);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(0);
			int sync = cursor.getInt(1);
			long time = cursor.getLong(2);
			long workout_id = cursor.getLong(3);
			WorkoutData w = new WorkoutData(id, sync, time, workout_id);
			// Load data
			Cursor c2 = db.query(Entry.ExerciseData.TABLE_NAME,
					Entry.ExerciseData.ALL_COLUMNS,
					Entry.ExerciseData.WORKOUT_DATA_ID + "=?",
					new String[] { Long.toString(id) }, null, null, null);

			c2.moveToFirst();
			while (!c2.isAfterLast()) {
				long id2 = c2.getLong(0);
				long exercise_id = c2.getLong(1);
				ExerciseData e = new ExerciseData(id2, exercise_id);
				// Load sets
				Cursor c3 = db.query(Entry.ExerciseData.SetData.TABLE_NAME,
						Entry.ExerciseData.SetData.ALL_COLUMNS,
						Entry.ExerciseData.SetData.EXERCISE_DATA_ID + "=?",
						new String[] { Long.toString(id2) }, null, null, null);

				c3.moveToFirst();
				while (!c3.isAfterLast()) {
					long id3 = c3.getLong(0);
					double weight = c3.getDouble(1);
					int repetitions = c3.getInt(2);

					e.add(new SetData(id3, weight, repetitions));
					c3.moveToNext();
				}
				c3.close();

				w.add(e);
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
	public boolean update(WorkoutData e) {
		SQLiteDatabase db = instance.getWritableDatabase();
		long id = e.getId();
		String[] id_str = new String[] { Long.toString(id) };
		boolean ok = false;
		db.beginTransaction();
		try {
			int rows = db.update(Entry.TABLE_NAME, instance.toContentValues(e),
					Entry._ID + "=?", id_str);
			ok = rows != 0;
			// Delete old data
			db.delete(Entry.ExerciseData.TABLE_NAME,
					Entry.ExerciseData.WORKOUT_DATA_ID + "=?", id_str);
			for (ExerciseData d : e) {
				db.delete(Entry.ExerciseData.SetData.TABLE_NAME,
						Entry.ExerciseData.SetData.EXERCISE_DATA_ID + "=?",
						new String[] { Long.toString(d.getId()) });
			}
			// Store new ones
			for (ExerciseData d : e) {
				ContentValues values = new ContentValues();
				values.put(Entry.ExerciseData.WORKOUT_DATA_ID, id);
				values.put(Entry.ExerciseData.EXERCISE_ID, d.getExerciseId());
				long id2 = db.insert(Entry.ExerciseData.TABLE_NAME, null,
						values);
				d.setId(id2);
				ok = ok && id2 != -1;
				if (id2 != -1) {
					for (SetData s : d) {
						ContentValues values2 = new ContentValues();
						values2.put(
								Entry.ExerciseData.SetData.EXERCISE_DATA_ID,
								id2);
						values2.put(Entry.ExerciseData.SetData.WEIGHT,
								s.getWeight());
						values2.put(Entry.ExerciseData.SetData.REPETITIONS,
								s.getRepetitions());
						long id3 = db.insert(
								Entry.ExerciseData.SetData.TABLE_NAME, null,
								values2);
						s.setId(id3);
						ok = ok && id3 != -1;
					}
				}
			}
			if (ok) {
				db.setTransactionSuccessful();
			}
		} finally {
			db.endTransaction();
		}
		db.close();
		return ok;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Entry.TABLE_NAME + " (" + Entry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Entry.SYNC
				+ " INTEGER NOT NULL, " + Entry.TIME + " INTEGER NOT NULL, "
				+ Entry.WORKOUT_ID + " INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE " + Entry.ExerciseData.TABLE_NAME + " ("
				+ Entry.ExerciseData._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Entry.ExerciseData.WORKOUT_DATA_ID + " INTEGER NOT NULL, "
				+ Entry.ExerciseData.EXERCISE_ID + " INTEGER NOT NULL);");

		db.execSQL("CREATE TABLE " + Entry.ExerciseData.SetData.TABLE_NAME
				+ " (" + Entry.ExerciseData.SetData._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Entry.ExerciseData.SetData.EXERCISE_DATA_ID
				+ " INTEGER NOT NULL, " + Entry.ExerciseData.SetData.WEIGHT
				+ " REAL NOT NULL, " + Entry.ExerciseData.SetData.REPETITIONS
				+ " INTEGER NOT NULL);");
	}
}
