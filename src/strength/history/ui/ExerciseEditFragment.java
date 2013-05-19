package strength.history.ui;

import strength.history.R;
import strength.history.data.structure.Exercise;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ExerciseEditFragment extends Fragment {
	public interface Listener {
		public void saveCallback(Exercise e);

		public void cancelCallback();

		public void deleteCallback();
	}

	private Button saveButton;
	private Button cancelButton;
	private ImageButton deleteButton;
	private EditText editTextName;
	private Exercise mExercise = null;
	private AlertDialog alertDialogDeleteConfirm = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentActivity fa = getActivity();
		if (fa instanceof Listener) {
			final Listener l = (Listener) fa;
			// TODO Show dialog again after rotation?
			alertDialogDeleteConfirm = new AlertDialog.Builder(fa)
					.setMessage(R.string.dialog_exercise_delete)
					.setPositiveButton(R.string.button_ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									l.deleteCallback();
								}
							}).setNegativeButton(R.string.button_cancel, null)
					.create();
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
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					alertDialogDeleteConfirm.show();
				}
			});
		} else {
			throw new ClassCastException();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		alertDialogDeleteConfirm.dismiss();
		save();
	}

	private void save() {
		if (mExercise != null) {
			mExercise.setName(editTextName.getText().toString());
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
		deleteButton = (ImageButton) view.findViewById(R.id.imageButtonDelete);
		return view;
	}
}
