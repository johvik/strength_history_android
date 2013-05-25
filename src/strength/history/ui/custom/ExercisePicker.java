package strength.history.ui.custom;

import strength.history.R;
import strength.history.ui.exercise.ExerciseAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

public class ExercisePicker extends FrameLayout {
	private Spinner spinner;

	public ExercisePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ExercisePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ExercisePicker(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.custom_exercise_picker,
				this, true);
		spinner = (Spinner) findViewById(R.id.spinnerSelectedExercise);
		Button button = (Button) findViewById(R.id.buttonDelete);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((ViewGroup) getParent()).removeView(ExercisePicker.this);
			}
		});
	}

	public void setAdapter(ExerciseAdapter adapter) {
		spinner.setAdapter(adapter);
	}

	public void setSelection(int position) {
		spinner.setSelection(position);
	}

	public int getSelectedItemPosition() {
		return spinner.getSelectedItemPosition();
	}
}
