package strength.history.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class StringSelectDialog extends DialogFragment {
	public interface Master {
		public String getStringSelectTitle();

		public CharSequence[] getStringSelectArray();

		public void onStringSelectClick(int index);

		public int getStringSelectSelected();
	}

	private Master master;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Master)) {
			throw new ClassCastException();
		}
		master = (Master) activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setTitle(master.getStringSelectTitle())
				.setSingleChoiceItems(master.getStringSelectArray(),
						master.getStringSelectSelected(),
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								master.onStringSelectClick(which);
								dialog.dismiss();
							}
						}).create();
	}
}
