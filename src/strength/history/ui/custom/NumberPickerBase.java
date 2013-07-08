package strength.history.ui.custom;

import strength.history.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public abstract class NumberPickerBase<T extends Number> extends FrameLayout {
	protected int repeatDelay = 75;
	protected EditText editText1;
	private Button buttonMinus;
	private Button buttonPlus;
	private final Handler repeatHandler = new Handler();

	public NumberPickerBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public NumberPickerBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NumberPickerBase(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.custom_number_picker,
				this, true);

		editText1 = (EditText) findViewById(R.id.editText1);
		buttonMinus = (Button) findViewById(R.id.buttonMinus);
		buttonPlus = (Button) findViewById(R.id.buttonPlus);

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

	public abstract T getNumber();

	public abstract void setNumber(T number);

	public abstract void setStepSize(T number);

	protected abstract T dec();

	protected abstract T inc();

	private void onMinusClick() {
		setNumber(dec());
	}

	private void onPlusClick() {
		setNumber(inc());
	}

	private class RepeatMinus implements Runnable {
		@Override
		public void run() {
			if (buttonMinus.isPressed()) {
				onMinusClick();
				repeatHandler.postDelayed(this, repeatDelay);
			}
		}
	}

	private class RepeatPlus implements Runnable {
		@Override
		public void run() {
			if (buttonPlus.isPressed()) {
				onPlusClick();
				repeatHandler.postDelayed(this, repeatDelay);
			}
		}
	}
}
