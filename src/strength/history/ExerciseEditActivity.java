package strength.history;

import strength.history.data.structure.Exercise;
import strength.history.ui.ExerciseEditFragment;
import strength.history.ui.ExerciseListFragment;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;

public class ExerciseEditActivity extends FragmentActivity implements
		ExerciseEditFragment.Creator {
	public static final int RESULT_ORIENTATION = RESULT_FIRST_USER + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Exercise e = (Exercise) intent
				.getParcelableExtra(ExerciseListFragment.EXERCISE);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Intent i = new Intent();
			intent.putExtra(ExerciseListFragment.EXERCISE, e);
			setResult(RESULT_ORIENTATION, i);
			finish();
			return;
		}
		setContentView(R.layout.activity_exercise_edit);
		ExerciseEditFragment f = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
		e.updateFrom(new Exercise("new"));
		f.setExercise(e);
	}

	@Override
	public void saveCallback(Exercise e) {
		Intent intent = new Intent();
		intent.putExtra(ExerciseListFragment.EXERCISE, e);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void cancelCallback() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
