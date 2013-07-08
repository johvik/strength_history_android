package strength.history.ui.dialog;

import strength.history.R;
import strength.history.data.structure.Weight;
import strength.history.ui.custom.NumberDecimalPicker;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class WeightDialog extends DialogFragment {
	public interface Listener {
		public void onWeightOk(double weight);

		public void onWeightCancel(double weight);
	}

	public static final String WEIGHT = "weight";

	private Listener listener;
	private NumberDecimalPicker weightPicker;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Listener)) {
			throw new ClassCastException();
		}
		weightPicker = new NumberDecimalPicker(activity);
		weightPicker.setStepSize(0.1);
		listener = (Listener) activity;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		double weight;
		Bundle b = getArguments();
		if (b != null) {
			weight = b.getDouble(WEIGHT, Weight.DEFAULT);
		} else {
			weight = Weight.DEFAULT;
		}
		weightPicker.setNumber(weight);
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.self_weight_entry)
				.setView(weightPicker)
				.setPositiveButton(R.string.button_save, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						listener.onWeightOk(weightPicker.getNumber());
					}
				})
				.setNegativeButton(R.string.button_cancel,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		listener.onWeightCancel(weightPicker.getNumber());
	}
}
