package strength.history.ui.workout.active;

import strength.history.R;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.ui.custom.NumberDecimalPicker;
import strength.history.ui.custom.NumberPicker;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ActiveExerciseEditFragment extends Fragment {
	public interface Listener {
		public void onItemSelected();
	}

	private NumberPicker numberPickerRepetitions;
	private NumberDecimalPicker numberDecimalPickerWeight;
	private ListView listViewSetData;
	private SetDataAdapter setDataAdapter;
	private TextView textViewSetResults;
	private ExerciseData exerciseData = null;
	private SetData savedSetData = null;
	private int selectedIndex = AdapterView.INVALID_POSITION;
	private Listener listener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Listener)) {
			throw new ClassCastException();
		}
		listener = (Listener) activity;
		setDataAdapter = new SetDataAdapter(activity.getApplicationContext());
	}

	public void setExerciseData(Pair<ExerciseData, Pair<Integer, SetData>> p) {
		exerciseData = p.first;
		selectedIndex = p.second.first;
		savedSetData = p.second.second;
		update();
	}

	public Pair<ExerciseData, Pair<Integer, SetData>> getExerciseData() {
		save();
		return Pair.create(exerciseData,
				Pair.create(selectedIndex, savedSetData));
	}

	public void addSetData() {
		if (exerciseData != null) {
			exerciseData.add(new SetData(numberDecimalPickerWeight.getNumber(),
					numberPickerRepetitions.getNumber()));
			setDataAdapter.notifyDataSetChanged();
			updateResultText();
		}
	}

	public void editSetData() {
		if (exerciseData != null) {
			int index = listViewSetData.getCheckedItemPosition();
			if (index != AdapterView.INVALID_POSITION) {
				// TODO
			}
		}
	}

	public void removeSetData() {
		if (exerciseData != null) {
			int index = listViewSetData.getCheckedItemPosition();
			if (index != AdapterView.INVALID_POSITION) {
				listViewSetData.clearChoices();
				exerciseData.remove(index);
				setDataAdapter.notifyDataSetChanged();
				updateResultText();
			}
		}
	}

	private void update() {
		if (savedSetData != null) {
			numberPickerRepetitions.setNumber(savedSetData.getRepetitions());
			numberDecimalPickerWeight.setNumber(savedSetData.getWeight());
		}
		if (selectedIndex != AdapterView.INVALID_POSITION) {
			listViewSetData.setItemChecked(selectedIndex, true);
		} else {
			listViewSetData.clearChoices();
		}
		updateResultText();
		setDataAdapter.setList(exerciseData);
	}

	private void updateResultText() {
		int size;
		if (exerciseData != null) {
			size = exerciseData.size();
		} else {
			size = 0;
		}
		textViewSetResults.setText(getString(R.string.results) + " (" + size
				+ ")");
	}

	private void save() {
		savedSetData = new SetData(numberDecimalPickerWeight.getNumber(),
				numberPickerRepetitions.getNumber());
		selectedIndex = listViewSetData.getCheckedItemPosition();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_active_exercise_edit,
				container, false);
		numberPickerRepetitions = (NumberPicker) view
				.findViewById(R.id.numberPickerRepetitions);
		numberDecimalPickerWeight = (NumberDecimalPicker) view
				.findViewById(R.id.numberDecimalPickerWeight);
		listViewSetData = (ListView) view.findViewById(R.id.listViewSetData);
		listViewSetData.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listViewSetData.setAdapter(setDataAdapter);
		listViewSetData.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listener.onItemSelected();
			}
		});
		textViewSetResults = (TextView) view
				.findViewById(R.id.textViewSetResults);
		return view;
	}
}
