package strength.history.ui.custom;

import strength.history.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class NumberPicker extends FrameLayout {
	private static final int DELAY_MS = 75;
	private EditText editText1;
	private ImageButton buttonMinus;
	private ImageButton buttonPlus;
	private Handler repeatHandler = new Handler();

	public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public NumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NumberPicker(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.custom_number_picker,
				this, true);

		editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setText(Integer.toString(0));
		buttonMinus = (ImageButton) findViewById(R.id.imageButtonMinus);
		buttonPlus = (ImageButton) findViewById(R.id.imageButtonPlus);

		buttonMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMinusClick();
			}
		});
		buttonMinus.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				repeatHandler.post(new RepeatMinus());
				return true;
			}
		});
		buttonPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onPlusClick();
			}
		});
		buttonPlus.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				repeatHandler.post(new RepeatPlus());
				return true;
			}
		});
	}

	public int getNumber() {
		try {
			return Integer.parseInt(editText1.getText().toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void setNumber(int number) {
		String text = Integer.toString(number);
		editText1.setText(text);
		editText1.setSelection(text.length());
	}

	private void onMinusClick() {
		setNumber(getNumber() - 1);
	}

	private void onPlusClick() {
		setNumber(getNumber() + 1);
	}

	private class RepeatMinus implements Runnable {
		@Override
		public void run() {
			if (buttonMinus.isPressed()) {
				onMinusClick();
				repeatHandler.postDelayed(this, DELAY_MS);
			}
		}
	}

	private class RepeatPlus implements Runnable {
		@Override
		public void run() {
			if (buttonPlus.isPressed()) {
				onPlusClick();
				repeatHandler.postDelayed(this, DELAY_MS);
			}
		}
	}
}
