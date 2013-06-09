package strength.history;

import java.util.Calendar;
import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.ui.SettingsActivity;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.custom.NumberDecimalPicker;
import strength.history.ui.workout.RunWorkoutActivity;
import strength.history.ui.workout.active.ActiveWorkoutListFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
		ActiveWorkoutListFragment.Listener, WeightProvider.Events.Latest {
	private static final String CUSTOM_DATE = "cdate";
	private static final String SELECTED_WEIGHT = "sweight";

	private DataProvider mDataProvider = null;
	private ImageButton imageButtonChangeDate;
	private DatePickerDialog datePickerDialog;
	private TextView textViewDate;
	private boolean customDate = false;
	private boolean forceSet = false;
	private static Calendar customCalendar = Calendar.getInstance();
	private boolean fragmentLoaded = false;
	private boolean weightLoaded = false;
	private AlertDialog alertDialogAddWeight;
	private NumberDecimalPicker weightPicker;
	private static String unit = "kg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		setTitle(R.string.app_name);
		addMenuItem(createMenuItem(R.drawable.ic_action_weight,
				R.string.add_weight_entry, new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialogAddWeight.show();
					}
				}));
		addMenuItem(createMenuItem(R.drawable.ic_action_settings,
				R.string.settings, new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								SettingsActivity.class);
						startActivity(i);
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
		weightPicker = new NumberDecimalPicker(this);
		weightPicker.setNumber(Weight.DEFAULT);
		alertDialogAddWeight = new AlertDialog.Builder(this)
				.setTitle(R.string.add_weight_entry)
				.setView(weightPicker)
				.setPositiveButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mDataProvider.insert(new Weight(getDate()
										.getTime(), weightPicker.getNumber()),
										getApplicationContext());
							}
						}).setNegativeButton(R.string.button_cancel, null)
				.create();

		updateProgressBar();
		mDataProvider = DataListener.add(this);
		if (savedInstanceState == null) {
			mDataProvider.latestWeight(getApplicationContext());
		} else {
			weightPicker.setNumber(savedInstanceState.getDouble(
					SELECTED_WEIGHT, Weight.DEFAULT));
			weightLoaded = true;
			updateProgressBar();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (forceSet) {
			customDate = false;
		}
		outState.putBoolean(CUSTOM_DATE, customDate);
		outState.putDouble(SELECTED_WEIGHT, weightPicker.getNumber());
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		unit = sharedPreferences.getString(
				SettingsActivity.PREF_WEIGHT_UNITS_KEY,
				getString(R.string.pref_unit_kg));
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
		alertDialogAddWeight.dismiss();
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

	private void updateProgressBar() {
		if (fragmentLoaded && weightLoaded) {
			setCustomProgressBarVisibility(false);
		} else {
			setCustomProgressBarVisibility(true);
		}
	}

	/**
	 * Use this to get the date!
	 * 
	 * @return Selected date if customDate else current time
	 */
	private Date getDate() {
		if (customDate) {
			return customCalendar.getTime();
		} else {
			return new Date();
		}
	}

	@Override
	public void startWorkout(Workout w) {
		Intent i = new Intent(this, RunWorkoutActivity.class);
		i.putExtra(RunWorkoutActivity.WORKOUT, w);
		i.putExtra(RunWorkoutActivity.TIME, getDate().getTime());
		startActivity(i);
		Log.d("MainActivity", "starting: " + w);
	}

	@Override
	public void setLoaded(boolean loaded) {
		fragmentLoaded = loaded;
		updateProgressBar();
	}

	@Override
	public void latestCallback(Weight e, boolean ok) {
		if (ok) {
			weightPicker.setNumber(e.getWeight());
		}
		weightLoaded = true;
		updateProgressBar();
	}

	public static String getUnit() {
		return unit;
	}
}
