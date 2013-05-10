package strength.history.data.service.local;

import android.content.Intent;
import android.os.Messenger;
import android.util.Log;
import strength.history.data.db.ExerciseDBHelper;
import strength.history.data.service.LocalServiceBase;
import strength.history.data.structure.Exercise;

/**
 * Local exercise service
 */
public class LocalExerciseService extends
		LocalServiceBase<Exercise, ExerciseDBHelper> {
	/**
	 * Name of the weight data passed with the intent
	 */
	public static final String EXERCISE = "EXERCISE";

	/**
	 * Constructor
	 */
	public LocalExerciseService() {
		super("LocalExerciseService");
	}

	@Override
	protected ExerciseDBHelper getDB() {
		return ExerciseDBHelper.getInstance(getApplicationContext());
	}

	@Override
	protected int getArg1() {
		return Service.EXERCISE.ordinal();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("LocalExerciseService", "onHandleIntent");

		Request request = (Request) intent.getSerializableExtra(REQUEST);
		Messenger messenger = intent.getParcelableExtra(MESSENGER);
		if (request != null && messenger != null) {
			switch (request) {
			case DELETE:
				delete((Exercise) intent.getParcelableExtra(EXERCISE),
						messenger);
				break;
			case INSERT:
				insert((Exercise) intent.getParcelableExtra(EXERCISE),
						messenger);
				break;
			case QUERY:
				query(messenger);
				break;
			case UPDATE:
				update((Exercise) intent.getParcelableExtra(EXERCISE),
						messenger);
				break;
			}
		}
	}
}
