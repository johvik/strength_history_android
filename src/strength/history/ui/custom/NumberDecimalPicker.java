package strength.history.ui.custom;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

public class NumberDecimalPicker extends NumberPickerBase<Double> {
	private double decimalFix = 10; // 1 decimal
	private double increase = 0.5;

	public NumberDecimalPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NumberDecimalPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NumberDecimalPicker(Context context) {
		super(context);
		init();
	}

	private void init() {
		editText1.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL
				| InputType.TYPE_NUMBER_FLAG_SIGNED);
		setNumber(0.0);
	}

	public void setDecimals(int decimals) {
		double newFix = 1;
		for (int i = 0; i < decimals; i++) {
			newFix = newFix * 10;
		}
		decimalFix = newFix;
	}

	@Override
	public Double getNumber() {
		try {
			return Double.parseDouble(editText1.getText().toString());
		} catch (NumberFormatException e) {
			setNumber(0.0);
			return 0.0;
		}
	}

	@Override
	public void setNumber(Double number) {
		number = Math.round(number * decimalFix) / decimalFix;
		editText1.setText(Double.toString(number));
	}

	@Override
	protected Double dec() {
		return getNumber() - increase;
	}

	@Override
	protected Double inc() {
		return getNumber() + increase;
	}
}
