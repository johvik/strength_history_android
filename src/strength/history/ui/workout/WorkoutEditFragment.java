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

public class WorkoutEditFragment extends Fragment implements
		ExerciseProvider.Events.Edit, ExerciseProvider.Events.Query {
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof Listener) {
			masterActivity = (Listener) activity;
		} else {
			throw new ClassCastException();
		}
		exerciseAdapter = new ExerciseAdapter(activity, exerciseList, true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataProvider = DataListener.add(this);
		masterActivity.setLoaded(false);
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
				ExercisePicker p = new ExercisePicker(getActivity());
				p.setAdapter(exerciseAdapter);
				linearLayoutSpinners.addView(p);
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		save();
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
			for (Long l : mWorkout) {
				ExercisePicker p = new ExercisePicker(getActivity());
				p.setAdapter(exerciseAdapter);
				for (Exercise ex : exerciseList) {
					if (ex.getId() == l) {
						p.setSelection(exerciseList.indexOf(ex));
						break;
					}
				}
				linearLayoutSpinners.addView(p);
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
		if (!ok) {
			exerciseList.add(e);
			exerciseAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		if (!ok) {
			exerciseList.remove(e);
			exerciseAdapter.notifyDataSetChanged();
		} else {
			// update the id...
			long id = e.getId();
			e.setId(-1);
			exerciseList.remove(e);
			e.setId(id);
			exerciseList.add(e);
		}
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		exerciseList.addAll(e);
		exerciseAdapter.notifyDataSetChanged();
		if (done) {
			masterActivity.setLoaded(true);
		}
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		if (!ok) {
			if (old != null) {
				exerciseList.remove(e);
				exerciseList.add(old);
				exerciseAdapter.notifyDataSetChanged();
			}
		}
	}
}
