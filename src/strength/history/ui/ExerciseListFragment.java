package strength.history.ui;

import strength.history.ExerciseEditActivity;
import strength.history.R;
import strength.history.data.structure.Exercise;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ExerciseListFragment extends Fragment {
	public interface Creator {
		public ExerciseAdapter getExerciseAdapter();
	}

	private static final int REQUEST_CODE = 1;

	public static final String EXERCISE = "exe";

	private boolean mDualPane;
	private ListView listViewExercises;
	private Button buttonExerciseCreate;
	private ExerciseEditFragment.Creator c2;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity f = getActivity();
		mDualPane = f.findViewById(R.id.fragmentExerciseEdit) != null;
		if (f instanceof Creator) {
			final ExerciseAdapter adapter = ((Creator) f).getExerciseAdapter();
			listViewExercises.setAdapter(adapter);
			listViewExercises.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					editExercise((Exercise) adapter.getItem(position), position);
				}
			});
			if (mDualPane) {
				listViewExercises.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				// TODO
			}
		} else {
			throw new ClassCastException();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exercise_list,
				container, false);
		listViewExercises = (ListView) view
				.findViewById(R.id.listViewExercises);
		buttonExerciseCreate = (Button) view
				.findViewById(R.id.buttonExerciseCreate);
		return view;
	}

	private void editExercise(Exercise e, int position) {
		Log.d("ExerciseListFragment", e.toString());
		listViewExercises.setItemChecked(position, true);
		if (mDualPane) {
			ExerciseEditFragment f = (ExerciseEditFragment) getFragmentManager()
					.findFragmentById(R.id.fragmentExerciseEdit);
			f.setExercise(e);
		} else {
			Intent intent = new Intent(getActivity(),
					ExerciseEditActivity.class);
			intent.putExtra(EXERCISE, e);
			startActivityForResult(intent, REQUEST_CODE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("ExerciseListFragment", "res " + requestCode);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == ExerciseEditActivity.RESULT_OK) {
				Exercise e = data.getParcelableExtra(EXERCISE);
			} else if (resultCode == ExerciseEditActivity.RESULT_CANCELED) {

			} else if (resultCode == ExerciseEditActivity.RESULT_ORIENTATION) {
				Exercise e = data.getParcelableExtra(EXERCISE);
			}
		}
	}
}
