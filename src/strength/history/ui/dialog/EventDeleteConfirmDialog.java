package strength.history.ui.dialog;

import java.util.Date;

import strength.history.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;

public class EventDeleteConfirmDialog extends DialogFragment {
	public interface Listener {
		public void onEventDeleteConfirm();
	}

	public static final String NAME = "nam";
	public static final String TIME = "tim";

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
		FragmentActivity a = getActivity();
		Bundle b = getArguments();
		String name = "?";
		String time = "?";
		if (b != null) {
			name = b.getString(NAME);
			time = DateFormat.getMediumDateFormat(a).format(
					new Date(b.getLong(TIME)));
			if (name == null) {
				name = "?";
			}
		}
		return new AlertDialog.Builder(a)
				.setTitle(R.string.dialog_event_delete_title)
				.setMessage(
						a.getString(R.string.dialog_event_delete_message, name,
								time))
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								listener.onEventDeleteConfirm();
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();
	}
}
