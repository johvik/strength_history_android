package strength.history.ui;

import strength.history.R;
import strength.history.data.structure.Exercise;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ExerciseEditFragment extends Fragment {
	public interface Listener {
		public void saveCallback(Exercise e);

		public void cancelCallback();
	}

	private Button saveButton;
	private Button cancelButton;
	private EditText editTextName;
	private Exercise e = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentActivity fa = getActivity();
		if (fa instanceof Listener) {
			final Listener l = (Listener) fa;
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String name = editTextName.getText().toString();
					if (e != null) {
						e.setName(name);
					}
					l.saveCallback(e);
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
		Log.d("ExerciseEditFragment", "" + savedInstanceState);
	}

	public void setExercise(Exercise e) {
		this.e = e;
		if (e != null) {
			editTextName.setText(e.getName());
			// TODO Focus and stuff?
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
		return view;
	}
}
