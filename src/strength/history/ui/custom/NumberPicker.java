package strength.history.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

public class NumberPicker extends NumberPickerBase<Integer> {
	private int increase = 1;

	public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NumberPicker(Context context) {
		super(context);
		init();
	}

	public void init() {
		setNumber(0);
	}

	@Override
	public Integer getNumber() {
		try {
			return Integer.parseInt(editText1.getText().toString());
		} catch (NumberFormatException e) {
			setNumber(0);
			return 0;
		}
	}

	@Override
	public void setNumber(Integer number) {
		editText1.setText(Integer.toString(number));
	}

	@Override
	protected Integer dec() {
		return getNumber() - increase;
	}

	@Override
	protected Integer inc() {
		return getNumber() + increase;
	}
}
