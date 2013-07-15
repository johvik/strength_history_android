package strength.history.ui.exercise;

import strength.history.R;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.ui.custom.NumberDecimalPicker;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ExerciseEditFragment extends Fragment {
	public interface Listener {
		public void saveCallback(Exercise e);

		public void deleteCallback();
	}

	private EditText editTextName;
	private Spinner spinnerMuscleGroup;
	private NumberDecimalPicker numberDecimalPickerStandardIncrease;
	private Exercise mExercise = null;
	private ArrayAdapter<MuscleGroup> muscleGroupAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		muscleGroupAdapter = new ArrayAdapter<Exercise.MuscleGroup>(activity,
				android.R.layout.simple_spinner_item, MuscleGroup.SORTED_VALUES);
		muscleGroupAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

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
			mExercise.setMuscleGroup(muscleGroupAdapter
					.getItem(spinnerMuscleGroup.getSelectedItemPosition()));
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
			spinnerMuscleGroup.setSelection(muscleGroupAdapter
					.getPosition(mExercise.getMuscleGroup()));
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
		spinnerMuscleGroup = (Spinner) view
				.findViewById(R.id.spinnerMuscleGroup);
		spinnerMuscleGroup.setAdapter(muscleGroupAdapter);
		numberDecimalPickerStandardIncrease = (NumberDecimalPicker) view
				.findViewById(R.id.numberDecimalPickerStandardIncrease);
		numberDecimalPickerStandardIncrease.setStepSize(0.1);
		return view;
	}
}
