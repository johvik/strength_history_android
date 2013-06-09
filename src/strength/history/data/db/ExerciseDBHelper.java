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
import strength.history.data.structure.Exercise.MuscleGroup;

/**
 * DB helper for Exercise
 */
public class ExerciseDBHelper extends DBHelperBase<Exercise> {
	private interface Entry extends BaseColumns, NameColumn, SyncColumns {
		static final String TABLE_NAME = "exercise";
		static final String MUSCLE_GROUP = "muscle_group";
		static final String[] ALL_COLUMNS = new String[] { _ID, SYNC, NAME,
				MUSCLE_GROUP };
	}

	private static final String DATABASE_NAME = "exercise.db";
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
		values.put(Entry.MUSCLE_GROUP, e.getMuscleGroup().ordinal());

		return values;
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
			MuscleGroup muscleGroup = MuscleGroup.parse(cursor.getInt(3));
			res.add(new Exercise(id, sync, name, muscleGroup));
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
				+ Entry.MUSCLE_GROUP + " INTEGER NOT NULL);");
		createDefaults(db);
	}

	private static void createDefaults(SQLiteDatabase db) {
		// Unsure of the muscle groups :)
		insert(db, new Exercise("Barbell Curl", MuscleGroup.ARMS));
		insert(db, new Exercise("Dumbbell Curl", MuscleGroup.ARMS));
		insert(db, new Exercise("Barbell Tricep Press", MuscleGroup.ARMS));
		insert(db, new Exercise("Dumbbell Tricep Press", MuscleGroup.ARMS));
		insert(db, new Exercise("Overhead Press", MuscleGroup.SHOULDERS));
		insert(db, new Exercise("Wrist Curl", MuscleGroup.ARMS));
		insert(db, new Exercise("Tricep Kickback", MuscleGroup.ARMS));
		insert(db, new Exercise("Bench Press", MuscleGroup.CHEST));
		insert(db, new Exercise("Cable Crossover", MuscleGroup.CHEST));
		insert(db, new Exercise("Dumbbell Fly", MuscleGroup.CHEST));
		insert(db, new Exercise("Incline Bench", MuscleGroup.CHEST));
		insert(db, new Exercise("Dips", MuscleGroup.ARMS));
		insert(db, new Exercise("Pushup", MuscleGroup.CHEST));
		insert(db, new Exercise("Pullup", MuscleGroup.BACK));
		insert(db, new Exercise("Back Raise", MuscleGroup.BACK));
		insert(db, new Exercise("Bent-Over Row", MuscleGroup.BACK));
		insert(db, new Exercise("Seated Row", MuscleGroup.BACK));
		insert(db, new Exercise("Chinup", MuscleGroup.BACK));
		insert(db, new Exercise("Lat Pulldown", MuscleGroup.BACK));
		insert(db, new Exercise("Seated Reverse Fly", MuscleGroup.BACK));
		insert(db, new Exercise("Military Press", MuscleGroup.SHOULDERS));
		insert(db, new Exercise("Upright Row", MuscleGroup.BACK));
		insert(db, new Exercise("Front Raise", MuscleGroup.BACK));
		insert(db, new Exercise("Side Lateral Raise", MuscleGroup.ABS));
		insert(db, new Exercise("Snatch", MuscleGroup.ARMS));
		insert(db, new Exercise("Push Press", MuscleGroup.ARMS));
		insert(db, new Exercise("Shrug", MuscleGroup.SHOULDERS));
		insert(db, new Exercise("Crunch Machine", MuscleGroup.ABS));
		insert(db, new Exercise("Crunch", MuscleGroup.ABS));
		insert(db, new Exercise("Ab Twist", MuscleGroup.ABS));
		insert(db, new Exercise("Bicycle Kick", MuscleGroup.ABS));
		insert(db, new Exercise("Hanging Leg Raise", MuscleGroup.ABS));
		insert(db, new Exercise("Hanging Knee Raise", MuscleGroup.ABS));
		insert(db, new Exercise("Reverse Crunch", MuscleGroup.ABS));
		insert(db, new Exercise("V Up", MuscleGroup.ABS));
		insert(db, new Exercise("Situp", MuscleGroup.ABS));
		insert(db, new Exercise("Squat", MuscleGroup.LEGS));
		insert(db, new Exercise("Lunge", MuscleGroup.LEGS));
		insert(db, new Exercise("Dead Lift", MuscleGroup.LEGS));
		insert(db, new Exercise("Hamstring Curl", MuscleGroup.LEGS));
		insert(db, new Exercise("Good Morning", MuscleGroup.BACK));
		insert(db, new Exercise("Clean", MuscleGroup.LEGS));
		insert(db, new Exercise("Leg Press", MuscleGroup.LEGS));
		insert(db, new Exercise("Leg Extension", MuscleGroup.LEGS));
	}
}
