package strength.history.ui;

import strength.history.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	public static final String PREF_WEIGHT_UNITS_KEY = "pref_weight_units_key";
	public static final String PREF_EMAIL_KEY = "pref_email_key";
	// TODO Improve password security
	public static final String PREF_PASSWORD_KEY = "pref_password_key";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		TextView textViewWindowTitle = (TextView) findViewById(R.id.textViewWindowTitle);
		ImageView imageViewTitleBack = (ImageView) findViewById(R.id.imageViewTitleBack);
		RelativeLayout relativeLayoutTitleIcon = (RelativeLayout) findViewById(R.id.relativeLayoutTitleIcon);

		if (textViewWindowTitle != null) {
			textViewWindowTitle.setText(R.string.settings);
		}
		if (imageViewTitleBack != null && relativeLayoutTitleIcon != null) {
			relativeLayoutTitleIcon.setFocusable(true);
			relativeLayoutTitleIcon.setClickable(true);
			relativeLayoutTitleIcon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			imageViewTitleBack.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = getPreferenceScreen()
				.getSharedPreferences();
		updateUnitSummary(sharedPreferences);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PREF_WEIGHT_UNITS_KEY)) {
			updateUnitSummary(sharedPreferences);
		}
	}

	private void updateUnitSummary(SharedPreferences sharedPreferences) {
		findPreference(PREF_WEIGHT_UNITS_KEY).setSummary(
				getString(R.string.unit_preference_summary, sharedPreferences
						.getString(PREF_WEIGHT_UNITS_KEY,
								getString(R.string.pref_unit_kg))));
	}
}
