package strength.history.ui.custom;

import strength.history.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public abstract class CustomTitleActivity extends Activity {
	private TextView textViewWindowTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(getLayoutResID());
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		textViewWindowTitle = (TextView) findViewById(R.id.textViewWindowTitle);
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
}
