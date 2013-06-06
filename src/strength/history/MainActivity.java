package strength.history;

import java.util.Date;

import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.ui.custom.CustomTitleActivity;
import strength.history.ui.workout.WorkoutsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Main Activity
 */
public class MainActivity extends CustomTitleActivity {
	private DataProvider mDataProvider = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = new Intent(this, WorkoutsActivity.class);
		startActivity(i);
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
		ListView listViewActiveWorkouts = (ListView) findViewById(R.id.listViewActiveWorkouts);
		listViewActiveWorkouts
				.setEmptyView(findViewById(R.id.textViewEmptyList));
		TextView textViewDate = (TextView) findViewById(R.id.textViewDate);
		textViewDate.setText(DateFormat.getMediumDateFormat(this).format(
				new Date()));
		// TODO What if date is changed?
		ImageButton imageButtonChangeData = (ImageButton) findViewById(R.id.imageButtonChangeDate);

		mDataProvider = DataListener.add(this);
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
}
