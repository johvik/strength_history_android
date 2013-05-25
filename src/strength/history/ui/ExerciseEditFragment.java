package strength.history.ui;

import strength.history.R;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Exercise.MuscleGroup;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ExerciseEditFragment extends Fragment {
	public interface Listener {
		public void saveCallback(Exercise e);

		public void cancelCallback();

		public void deleteCallback();
	}

	private Button saveButton;
	private Button cancelButton;
	private EditText editTextName;
	private Spinner spinnerMuscleGroup;
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
		if (fa instanceof Listener) {
			final Listener l = (Listener) fa;
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					save();
					l.saveCallback(mExercise);
				}
			});
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					l.cancelCallback();
				}
			});
		} else {
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
		cancelButton = (Button) view.findViewById(R.id.buttonCancel);
		saveButton = (Button) view.findViewById(R.id.buttonSave);
		return view;
	}
}
