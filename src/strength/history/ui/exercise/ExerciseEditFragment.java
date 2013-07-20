package strength.history.ui.exercise;

import strength.history.R;
import strength.history.data.structure.Exercise;
import strength.history.ui.custom.NumberDecimalPicker;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ExerciseEditFragment extends Fragment {
	public interface Listener {
		public void saveCallback(Exercise e);

		public void deleteCallback();
	}

	private EditText editTextName;
	private NumberDecimalPicker numberDecimalPickerStandardIncrease;
	private Exercise mExercise = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentActivity fa = getActivity();
		if (!(fa instanceof Listener)) {
			throw new ClassCastException();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		save();
	}

	private void save() {
		if (mExercise != null) {
			mExercise.setName(editTextName.getText().toString());
			mExercise.setStandardIncrease(numberDecimalPickerStandardIncrease
					.getNumber());
		}
	}

	public Exercise getExercise() {
		save();
		return mExercise;
	}

	public void setExercise(Exercise e) {
		mExercise = e;
		if (mExercise != null) {
			editTextName.setText(mExercise.getName());
			numberDecimalPickerStandardIncrease.setNumber(mExercise
					.getStandardIncrease());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exercise_edit,
				container, false);
		editTextName = (EditText) view.findViewById(R.id.editTextName);
		numberDecimalPickerStandardIncrease = (NumberDecimalPicker) view
				.findViewById(R.id.numberDecimalPickerStandardIncrease);
		numberDecimalPickerStandardIncrease.setStepSize(0.1);
		return view;
	}
}
