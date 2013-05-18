package strength.history.ui;

import strength.history.R;
import strength.history.data.structure.Exercise;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ExerciseEditFragment extends Fragment {
	public interface Creator {
		public void saveCallback();

		public void cancelCallback();
	}

	private View relativeLayoutActive;
	private View relativeLayoutInactive;
	private Button saveButton;
	private Button cancelButton;
	private EditText editTextName;
	public static Exercise editExercise = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentActivity f = getActivity();
		if (f instanceof Creator) {
			final Creator c = (Creator) f;
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					c.cancelCallback();
				}
			});
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					save();
					c.saveCallback();
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
		String name = editTextName.getText().toString();
		if (editExercise != null) {
			editExercise.setName(name);
		}
	}

	public void update() {
		if (editExercise != null) {
			relativeLayoutActive.setVisibility(View.VISIBLE);
			relativeLayoutInactive.setVisibility(View.GONE);
			String name = editExercise.getName();
			editTextName.setText(name);
			editTextName.setSelection(name.length());
			editTextName.requestFocus();
		} else {
			relativeLayoutActive.setVisibility(View.GONE);
			relativeLayoutInactive.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exercise_edit,
				container, false);
		editTextName = (EditText) view.findViewById(R.id.editTextName);
		cancelButton = (Button) view.findViewById(R.id.buttonCancel);
		saveButton = (Button) view.findViewById(R.id.buttonSave);
		relativeLayoutActive = view.findViewById(R.id.relativeLayoutActive);
		relativeLayoutInactive = view.findViewById(R.id.relativeLayoutInactive);
		return view;
	}
}
