package strength.history.ui.dialog;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DateDialog extends DialogFragment {
	public interface Listener {
		public void onDateSet(Calendar c);

		public void onDateCancel(Calendar c);
	}

	public static final String DATE = "date";

	private Listener listener;
	private Calendar selectedDate;

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
		Calendar c;
		Bundle b = getArguments();
		if (b != null) {
			c = (Calendar) b.getSerializable(DATE);
		} else {
			c = Calendar.getInstance();
		}
		selectedDate = c;
		return new DatePickerDialog(getActivity(), new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				selectedDate = Calendar.getInstance();
				selectedDate.set(year, monthOfYear, dayOfMonth);
				listener.onDateSet(selectedDate);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		listener.onDateCancel(selectedDate);
	}
}
