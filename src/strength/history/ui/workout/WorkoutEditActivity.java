package strength.history.ui.workout;

import strength.history.R;
import strength.history.data.structure.Workout;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;

public class WorkoutEditActivity extends CustomTitleFragmentActivity implements
		WorkoutEditFragment.Listener {
	public static final int RESULT_ORIENTATION = RESULT_FIRST_USER + 1;
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 2;
	public static final String WORKOUT = "work";

	private WorkoutEditFragment workoutEditFragment;
	private AlertDialog alertDialogDeleteConfirm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// screen rotated
			Intent intent = new Intent();
			intent.putExtra(WORKOUT, savedInstanceState.getParcelable(WORKOUT));
			setResult(RESULT_ORIENTATION, intent);
			finish();
			return;
		}
		alertDialogDeleteConfirm = new AlertDialog.Builder(this)
				.setTitle(R.string.dialog_workout_delete)
				.setMessage(R.string.dialog_delete_info)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteCallback();
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();
		workoutEditFragment = (WorkoutEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentWorkoutEdit);

		if (savedInstanceState == null) {
			workoutEditFragment.setWorkout((Workout) getIntent()
					.getParcelableExtra(WORKOUT));
		} else {
			workoutEditFragment.setWorkout((Workout) savedInstanceState
					.getParcelable(WORKOUT));
		}

		setTitle(R.string.edit_workout);
		addMenuItem(createMenuItem(R.drawable.ic_action_save,
				R.string.save_workout, new OnClickListener() {
					@Override
					public void onClick(View v) {
						saveCallback(workoutEditFragment.getWorkout());
					}
				}));
		addMenuItem(createMenuItem(R.drawable.ic_action_delete,
				R.string.delete_workout, new OnClickListener() {
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
		return R.layout.activity_workout_edit;
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
		if (workoutEditFragment != null) {
			outState.putParcelable(WORKOUT, workoutEditFragment.getWorkout());
		}
	}

	@Override
	public void saveCallback(Workout e) {
		Intent intent = new Intent();
		intent.putExtra(WORKOUT, e);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void deleteCallback() {
		setResult(RESULT_DELETE);
		finish();
	}

	@Override
	public void setLoaded(boolean loaded) {
		setCustomProgressBarVisibility(!loaded);
	}
}
