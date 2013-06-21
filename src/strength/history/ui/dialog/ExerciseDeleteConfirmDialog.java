package strength.history.ui.dialog;

import strength.history.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ExerciseDeleteConfirmDialog extends DialogFragment {
	public interface Listener {
		public void onExerciseDeleteConfirm();
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
				.setTitle(R.string.dialog_exercise_delete_title)
				.setMessage(R.string.dialog_exercise_delete_message)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								listener.onExerciseDeleteConfirm();
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();
	}
}
