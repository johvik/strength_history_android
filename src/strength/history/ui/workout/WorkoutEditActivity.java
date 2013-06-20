package strength.history.ui.workout;

import strength.history.R;
import strength.history.data.structure.Workout;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.dialog.WorkoutDeleteConfirmDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;

public class WorkoutEditActivity extends CustomTitleFragmentActivity implements
		WorkoutEditFragment.Listener, WorkoutDeleteConfirmDialog.Listener {
	public static final int RESULT_ORIENTATION = RESULT_FIRST_USER + 1;
	public static final int RESULT_DELETE = RESULT_FIRST_USER + 2;
	public static final String WORKOUT = "work";

	private WorkoutEditFragment workoutEditFragment;
	private WorkoutDeleteConfirmDialog workoutDeleteConfirmDialog = null;

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
						showWorkoutDeleteConfirmDialog();
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
		if (workoutDeleteConfirmDialog != null) {
			workoutDeleteConfirmDialog.dismiss();
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

	private void showWorkoutDeleteConfirmDialog() {
		FragmentManager fm = getSupportFragmentManager();
		workoutDeleteConfirmDialog = new WorkoutDeleteConfirmDialog();
		workoutDeleteConfirmDialog.show(fm,
				"fragment_workout_delete_confirm_dialog");
	}

	@Override
	public void onWorkoutDeleteConfirm() {
		deleteCallback();
	}
}
