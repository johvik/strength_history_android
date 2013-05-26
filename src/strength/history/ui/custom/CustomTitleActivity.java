package strength.history.ui.custom;

import strength.history.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class CustomTitleActivity extends Activity {
	private TextView textViewWindowTitle = null;
	private ProgressBar progressBarTitle = null;
	private LinearLayout linearLayoutTitleRight = null;
	private ImageView imageViewTitleBack = null;
	private RelativeLayout relativeLayoutTitleIcon = null;
	private Toast toast = null;

	@SuppressLint("ShowToast")
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

	@Override
	protected void onPause() {
		super.onPause();
		if (toast != null) {
			toast.cancel();
		}
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

	public View createMenuItem(int resId, final int textResId, OnClickListener l) {
		ImageButton b = (ImageButton) getLayoutInflater().inflate(
				R.layout.menu_item, linearLayoutTitleRight, false);
		b.setImageResource(resId);
		b.setOnClickListener(l);
		b.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (toast != null) {
					toast.cancel();
				}
				toast = Toast.makeText(CustomTitleActivity.this, textResId,
						Toast.LENGTH_SHORT);
				int[] location = new int[2];
				v.getLocationOnScreen(location);
				int x = getWindowManager().getDefaultDisplay().getWidth()
						- location[0] - v.getWidth() / 2;
				int y = location[1] + v.getHeight() / 2;
				toast.setGravity(Gravity.RIGHT | Gravity.TOP, x, y);
				toast.show();
				return true;
			}
		});
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
