package strength.history.ui.dialog;

import strength.history.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class WorkoutDeleteConfirmDialog extends DialogFragment {
	public interface Listener {
		public void onWorkoutDeleteConfirm();
	}

	private Listener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Listener)) {
			throw new ClassCastException();
		}
		listener = (Listener) activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_workout_delete)
				.setMessage(R.string.dialog_delete_info)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								listener.onWorkoutDeleteConfirm();
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();
	}
}
