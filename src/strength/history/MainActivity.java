package strength.history;

import java.util.Calendar;
import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.structure.Workout;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.workout.ActiveWorkoutListFragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Main Activity
 */
public class MainActivity extends CustomTitleFragmentActivity implements
		ActiveWorkoutListFragment.Listener {
	private static final String CUSTOM_DATE = "cdate";

	private DataProvider mDataProvider = null;
	private ImageButton imageButtonChangeDate;
	private DatePickerDialog datePickerDialog;
	private TextView textViewDate;
	private boolean customDate = false;
	private boolean forceSet = false;
	private static Calendar customCalendar = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.app_name);
		addMenuItem(createMenuItem(R.drawable.ic_action_weight,
				R.string.add_weight_entry, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				}));
		addMenuItem(createMenuItem(R.drawable.ic_action_settings,
				R.string.settings, new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					}
				}));
		textViewDate = (TextView) findViewById(R.id.textViewDate);
		updateTextViewDate(new Date());

		datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar c = Calendar.getInstance();
				customCalendar.set(year, monthOfYear, dayOfMonth,
						c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
				onDateDoneClick();
			}
		}, customCalendar.get(Calendar.YEAR),
				customCalendar.get(Calendar.MONTH),
				customCalendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				imageButtonChangeDate
						.setImageResource(R.drawable.ic_action_date_gray);
				customDate = false;
				updateTextViewDate(new Date());
			}
		});
		// TODO What if date is changed (day etc. changes)?
		imageButtonChangeDate = (ImageButton) findViewById(R.id.imageButtonChangeDate);
		imageButtonChangeDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (customDate) {
					imageButtonChangeDate
							.setImageResource(R.drawable.ic_action_date_gray);
					customDate = false;
					updateTextViewDate(new Date());
				} else {
					datePickerDialog.show();
				}
			}
		});
		if (savedInstanceState != null) {
			customDate = savedInstanceState.getBoolean(CUSTOM_DATE, false);
			if (customDate) {
				onDateDoneClick();
			}
		}

		mDataProvider = DataListener.add(this);
		Log.d("Add!", "" + mDataProvider);
		mDataProvider.queryWorkout(getApplicationContext());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (forceSet) {
			customDate = false;
		}
		outState.putBoolean(CUSTOM_DATE, customDate);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!customDate) {
			updateTextViewDate(new Date());
		}
		forceSet = false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (datePickerDialog.isShowing()) {
			datePickerDialog.cancel();
			forceSet = true;
		}
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_main;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataListener.remove(this);
	}

	private void updateTextViewDate(Date d) {
		textViewDate.setText(DateFormat.getMediumDateFormat(this).format(d));
	}

	private void onDateDoneClick() {
		imageButtonChangeDate
				.setImageResource(R.drawable.ic_action_restore_gray);
		customDate = true;
		updateTextViewDate(customCalendar.getTime());
	}

	@Override
	public void startWorkout(Workout w) {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "starting: " + w);
	}
}
