package strength.history.ui.exercise;

import strength.history.R;
import strength.history.data.structure.Exercise;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;

public class ExerciseEditActivity extends CustomTitleFragmentActivity implements
		ExerciseEditFragment.Listener {
	public static final int RESULT_ORIENTATION = RESULT_FIRST_USER + 1;
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 2;
	public static final String EXERCISE = "exer";

	private ExerciseEditFragment exerciseEditFragment;
	private AlertDialog alertDialogDeleteConfirm = null;

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
		alertDialogDeleteConfirm = new AlertDialog.Builder(this)
				.setMessage(R.string.dialog_exercise_delete)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteCallback();
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);

		if (savedInstanceState == null) {
			exerciseEditFragment.setExercise((Exercise) getIntent()
					.getParcelableExtra(EXERCISE));
		} else {
			exerciseEditFragment.setExercise((Exercise) savedInstanceState
					.getParcelable(EXERCISE));
		}

		setTitle(R.string.edit_exercise);
		addMenuItem(createMenuItem(R.drawable.ic_action_save,
				R.string.save_exercise, new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveCallback(exerciseEditFragment.getExercise());
					}
				}));
		addMenuItem(createMenuItem(R.drawable.ic_action_delete,
				R.string.delete_exercise, new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialogDeleteConfirm.show();
					}
				}));
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_exercise_edit;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (alertDialogDeleteConfirm != null) {
			alertDialogDeleteConfirm.dismiss();
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
	public void deleteCallback() {
		setResult(RESULT_DELETE);
		finish();
	}
}
