package strength.history;

import strength.history.ui.ExerciseEditFragment;
import android.os.Bundle;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;

public class ExerciseEditActivity extends FragmentActivity implements
		ExerciseEditFragment.Creator {
	public static final int RESULT_ORIENTATION = RESULT_FIRST_USER + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// screen rotated
			setResult(RESULT_ORIENTATION);
			finish();
			return;
		}
		setContentView(R.layout.activity_exercise_edit);
		ExerciseEditFragment f = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
		f.update();
	}

	@Override
	public void saveCallback() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void cancelCallback() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
