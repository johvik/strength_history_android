package strength.history.ui.workout;

import java.util.Collection;
import java.util.Comparator;

import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Workout;
import strength.history.ui.custom.ExercisePicker;
import strength.history.ui.exercise.ExerciseAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WorkoutEditFragment extends Fragment implements
		ExerciseProvider.Events {
	public interface Listener {
		public void saveCallback(Workout e);

		public void deleteCallback();

		public void setLoaded(boolean loaded);
	}

	private Button addButton;
	private EditText editTextName;
	private LinearLayout linearLayoutSpinners;
	private Workout mWorkout = null;
	private DataProvider dataProvider = null;
	private ExerciseAdapter exerciseAdapter;
	private SortedList<Exercise> exerciseList = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					int c = lhs.getName().compareTo(rhs.getName());
					if (c == 0) {
						c = lhs.compareTo(rhs);
					}
					return c;
				}
			}, true);
	private Listener masterActivity;
	private boolean loaded = false;
	private Toast toast;

	@SuppressLint("ShowToast")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof Listener) {
			masterActivity = (Listener) activity;
		} else {
			throw new ClassCastException();
		}
		exerciseAdapter = new ExerciseAdapter(activity, exerciseList, true);
		toast = Toast.makeText(getActivity(), R.string.no_exercises_found,
				Toast.LENGTH_SHORT);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loaded = false;
		masterActivity.setLoaded(loaded);
		dataProvider = DataListener.add(this);
		dataProvider.queryExercise(getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DataListener.remove(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (exerciseList.isEmpty()) {
					toast.show();
				} else {
					ExercisePicker p = new ExercisePicker(getActivity());
					p.setAdapter(exerciseAdapter);
					p.performClick();
					linearLayoutSpinners.addView(p);
				}
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		save();
		toast.cancel();
	}

	private void save() {
		if (mWorkout != null) {
			mWorkout.setName(editTextName.getText().toString());
			mWorkout.clear();
			for (int i = 0, j = linearLayoutSpinners.getChildCount(); i < j; i++) {
				ExercisePicker p = (ExercisePicker) linearLayoutSpinners
						.getChildAt(i);
				int pos = p.getSelectedItemPosition();
				if (pos != -1) {
					Exercise e = exerciseList.get(pos);
					mWorkout.add(e.getId());
				}
			}
		}
	}

	public Workout getWorkout() {
		save();
		return mWorkout;
	}

	public void setWorkout(Workout e) {
		mWorkout = e;
		if (mWorkout != null) {
			editTextName.setText(mWorkout.getName());
			linearLayoutSpinners.removeAllViews();
			for (int i = 0, j = mWorkout.size(); i < j; i++) {
				ExercisePicker p = new ExercisePicker(getActivity());
				p.setAdapter(exerciseAdapter);
				linearLayoutSpinners.addView(p);
			}
			if (loaded) {
				updateSelection();
			}
		}
	}

	private void updateSelection() {
		if (mWorkout != null) {
			for (int i = 0, j = mWorkout.size(); i < j; i++) {
				long id = mWorkout.get(i);
				ExercisePicker p = (ExercisePicker) linearLayoutSpinners
						.getChildAt(i);
				p.setSelection(-1);
				for (Exercise ex : exerciseList) {
					if (ex.getId() == id) {
						p.setSelection(exerciseList.indexOf(ex));
						break;
					}
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_workout_edit, container,
				false);
		editTextName = (EditText) view.findViewById(R.id.editTextName);
		addButton = (Button) view.findViewById(R.id.buttonAdd);
		linearLayoutSpinners = (LinearLayout) view
				.findViewById(R.id.linearLayoutSpinners);
		return view;
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		if (ok) {
			exerciseList.remove(e);
			exerciseAdapter.notifyDataSetChanged();
			updateSelection();
		}
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		if (ok) {
			exerciseList.add(e);
			exerciseAdapter.notifyDataSetChanged();
			updateSelection();
		}
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		exerciseList.addAll(e);
		exerciseAdapter.notifyDataSetChanged();
		if (done) {
			loaded = true;
			masterActivity.setLoaded(loaded);
			updateSelection();
		}
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		if (ok) {
			if (old != null) {
				exerciseList.remove(old);
			}
			exerciseList.add(e);
			exerciseAdapter.notifyDataSetChanged();
			updateSelection();
		}
	}
}
