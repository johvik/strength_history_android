package strength.history;

import java.util.Calendar;
import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.ui.SettingsActivity;
import strength.history.ui.active.ActiveWorkoutListFragment;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.dialog.CreateDefaultDialog;
import strength.history.ui.dialog.DateDialog;
import strength.history.ui.dialog.WeightDialog;
import strength.history.ui.history.HistoryActivity;
import strength.history.ui.workout.RunWorkoutActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Main Activity
 */
public class MainActivity extends CustomTitleFragmentActivity implements
		ActiveWorkoutListFragment.Listener, WeightProvider.Events.Latest,
		WeightDialog.Listener, DateDialog.Listener {
	private static final String CUSTOM_DATE = "cdate";
	private static final String SELECTED_WEIGHT = "sweight";
	private static final String FIRST_RUN = "first_run";

	// TODO Fix when multiple things are added with a custom date! (same mstime)

	private DataProvider mDataProvider = null;
	private ImageButton imageButtonChangeDate;
	private TextView textViewDate;
	private boolean customDate = false;
	private static Calendar customCalendar = Calendar.getInstance();
	private boolean fragmentLoaded = false;
	private boolean weightLoaded = false;
	private double savedWeight = Weight.DEFAULT;
	private static String unit = "kg";
	private Handler refreshHandler = new Handler();
	private Runnable refreshRunnable = new Runnable() {
		@Override
		public void run() {
			refreshDate();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		setTitle(R.string.app_name);
		addMenuItem(createMenuItem(R.drawable.ic_action_weight,
				R.string.self_weight_entry, new OnClickListener() {
					@Override
					public void onClick(View v) {
						showWeightDialog(savedWeight);
					}
				}));
		addMenuItem(createMenuItem(R.drawable.ic_action_history,
				R.string.history, new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(MainActivity.this,
								HistoryActivity.class);
						startActivity(i);
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
					showDateDialog(customCalendar);
				}
			}
		});
		if (savedInstanceState != null) {
			customDate = savedInstanceState.getBoolean(CUSTOM_DATE, false);
			if (customDate) {
				onDateDoneClick();
			}
		}

		updateProgressBar();
		if (savedInstanceState != null) {
			savedWeight = savedInstanceState.getDouble(SELECTED_WEIGHT,
					Weight.DEFAULT);
			weightLoaded = true;
			updateProgressBar();
		}

		// Handle first run
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean firstRun = sharedPreferences.getBoolean(FIRST_RUN, true);
		if (firstRun) {
			Editor e = sharedPreferences.edit();
			e.putBoolean(FIRST_RUN, false);
			e.commit();
			onFirstRun();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(CUSTOM_DATE, customDate);
		outState.putDouble(SELECTED_WEIGHT, savedWeight);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDataProvider = DataListener.add(this);
		if (!weightLoaded) {
			mDataProvider.latestWeight(getApplicationContext());
		}
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		unit = sharedPreferences.getString(
				SettingsActivity.PREF_WEIGHT_UNITS_KEY,
				getString(R.string.pref_unit_kg));
		refreshDate();
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_main;
	}

	@Override
	protected void onPause() {
		super.onPause();
		refreshHandler.removeCallbacks(refreshRunnable);
		DataListener.remove(this);
	}

	private void refreshDate() {
		if (!customDate) {
			updateTextViewDate(new Date());
		}
		refreshHandler.removeCallbacks(refreshRunnable); // Double check
		// +50 for some extra margin
		refreshHandler.postDelayed(refreshRunnable, timeToNextDay() + 50);
	}

	private static long timeToNextDay() {
		Calendar c = Calendar.getInstance();
		long now = c.getTimeInMillis();
		c.add(Calendar.DAY_OF_MONTH, 1); // day+1
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis() - now;
	}

	private void onFirstRun() {
		FragmentManager fm = getSupportFragmentManager();
		CreateDefaultDialog d = new CreateDefaultDialog();
		d.show(fm, "fragment_create_default_dialog");
	}

	private void showDateDialog(Calendar c) {
		FragmentManager fm = getSupportFragmentManager();
		DateDialog d = new DateDialog();
		Bundle b = new Bundle();
		b.putSerializable(DateDialog.DATE, c);
		d.setArguments(b);
		d.show(fm, "fragment_date_dialog");
	}

	private void showWeightDialog(double weight) {
		FragmentManager fm = getSupportFragmentManager();
		WeightDialog d = new WeightDialog();
		Bundle b = new Bundle();
		b.putDouble(WeightDialog.WEIGHT, weight);
		d.setArguments(b);
		d.show(fm, "fragment_weight_dialog");
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
			savedWeight = e.getWeight();
		}
		weightLoaded = true;
		updateProgressBar();
	}

	public static String getUnit() {
		return unit;
	}

	@Override
	public void onWeightOk(double weight) {
		mDataProvider.insert(new Weight(getDate().getTime(), weight),
				getApplicationContext());
		onWeightCancel(weight); // handle the same way
	}

	@Override
	public void onWeightCancel(double weight) {
		savedWeight = weight;
	}

	@Override
	public void onDateSet(Calendar c) {
		customCalendar = c;
		onDateDoneClick();
	}

	@Override
	public void onDateCancel(Calendar c) {
		customCalendar = c;
		imageButtonChangeDate.setImageResource(R.drawable.ic_action_date_gray);
		customDate = false;
		updateTextViewDate(new Date());
	}
}
