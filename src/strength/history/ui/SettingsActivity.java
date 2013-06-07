package strength.history.ui;

import strength.history.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsActivity extends PreferenceActivity {
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
}
