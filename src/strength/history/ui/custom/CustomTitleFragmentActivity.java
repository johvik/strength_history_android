package strength.history.ui.custom;

import strength.history.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Same code as in CustomTitleActivity
 */
public abstract class CustomTitleFragmentActivity extends FragmentActivity {
	private TextView textViewWindowTitle = null;
	private ProgressBar progressBarTitle = null;
	private LinearLayout linearLayoutTitleRight = null;
	private ImageView imageViewTitleBack = null;
	private RelativeLayout relativeLayoutTitleIcon = null;

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
		imageViewTitleBack = (ImageView) findViewById(R.id.imageViewTitleBack);
		relativeLayoutTitleIcon = (RelativeLayout) findViewById(R.id.relativeLayoutTitleIcon);
	}

	/**
	 * Don't use setContentView this will be called
	 * 
	 * @return
	 */
	protected abstract int getLayoutResID();

	public void setCustomBackButton(OnClickListener l) {
		if (relativeLayoutTitleIcon != null && imageViewTitleBack != null) {
			if (l != null) {
				relativeLayoutTitleIcon.setFocusable(true);
				relativeLayoutTitleIcon.setClickable(true);
				relativeLayoutTitleIcon.setOnClickListener(l);
				imageViewTitleBack.setVisibility(View.VISIBLE);
			} else {
				relativeLayoutTitleIcon.setFocusable(false);
				relativeLayoutTitleIcon.setClickable(false);
				relativeLayoutTitleIcon.setOnClickListener(l);
				imageViewTitleBack.setVisibility(View.INVISIBLE);
			}
		}
	}

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

	public View createMenuItem(int resId, OnClickListener l) {
		ImageButton b = (ImageButton) getLayoutInflater().inflate(
				R.layout.menu_item, linearLayoutTitleRight, false);
		b.setImageResource(resId);
		b.setBackgroundResource(R.drawable.menu_item);
		b.setOnClickListener(l);
		return b;
	}

	public void addMenuItem(View v) {
		if (linearLayoutTitleRight != null) {
			linearLayoutTitleRight.addView(v);
		}
	}

	public void addMenuItem(View v, int index) {
		if (linearLayoutTitleRight != null) {
			linearLayoutTitleRight.addView(v, index);
		}
	}

	public void removeMenuItem(View v) {
		if (linearLayoutTitleRight != null) {
			linearLayoutTitleRight.removeView(v);
		}
	}
}
