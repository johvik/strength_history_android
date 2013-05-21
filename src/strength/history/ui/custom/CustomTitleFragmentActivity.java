package strength.history.ui.custom;

import strength.history.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Same code as in CustomTitleActivity
 */
public abstract class CustomTitleFragmentActivity extends FragmentActivity {
	private TextView textViewWindowTitle = null;
	private ProgressBar progressBarTitle = null;
	private LinearLayout linearLayoutTitleRight = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(getLayoutResID());
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		textViewWindowTitle = (TextView) findViewById(R.id.textViewWindowTitle);
		progressBarTitle = (ProgressBar) findViewById(R.id.progressBarTitle);
		linearLayoutTitleRight = (LinearLayout) findViewById(R.id.linearLayoutTitleRight);
	}

	/**
	 * Don't use setContentView this will be called
	 * 
	 * @return
	 */
	protected abstract int getLayoutResID();

	@Override
	public void setTitle(CharSequence title) {
		if (textViewWindowTitle != null) {
			textViewWindowTitle.setText(title);
		}
	}

	@Override
	public void setTitle(int titleId) {
		if (textViewWindowTitle != null) {
			textViewWindowTitle.setText(titleId);
		}
	}

	public void setCustomProgressBarVisibility(boolean visible) {
		if (progressBarTitle != null) {
			progressBarTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
		}
	}

	public void addMenuItem(View view) {
		if (linearLayoutTitleRight != null) {
			linearLayoutTitleRight.addView(view);
		}
	}
}
