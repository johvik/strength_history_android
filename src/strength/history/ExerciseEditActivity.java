package strength.history;

import strength.history.data.structure.Exercise;
import strength.history.ui.ExerciseEditFragment;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;

public class ExerciseEditActivity extends FragmentActivity implements
		ExerciseEditFragment.Listener {
	public static final int RESULT_ORIENTATION = RESULT_FIRST_USER + 1;
	public static final String EXERCISE = "exer";

	private ExerciseEditFragment exerciseEditFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// screen rotated
			Intent intent = new Intent();
			intent.putExtra(EXERCISE,
					(Exercise) savedInstanceState.getParcelable(EXERCISE));
			setResult(RESULT_ORIENTATION, intent);
			finish();
			return;
		}
		setContentView(R.layout.activity_exercise_edit);
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);

		if (savedInstanceState == null) {
			exerciseEditFragment.setExercise((Exercise) getIntent()
					.getParcelableExtra(EXERCISE));
		} else {
			exerciseEditFragment.setExercise((Exercise) savedInstanceState
					.getParcelable(EXERCISE));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (exerciseEditFragment != null) {
			outState.putParcelable(EXERCISE, exerciseEditFragment.getExercise());
		}
	}

	@Override
	public void saveCallback(Exercise e) {
		Intent intent = new Intent();
		intent.putExtra(EXERCISE, e);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void cancelCallback() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
