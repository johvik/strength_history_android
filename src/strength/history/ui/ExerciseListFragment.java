package strength.history.ui;

import strength.history.R;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ExerciseListFragment extends Fragment {
	public interface Listener {
		public void onExerciseItemClick(int position);

		public void onExerciseCreateClick();
	}

	private ListView listViewExercises;
	private Button buttonExerciseCreate;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity f = getActivity();
		if (f instanceof Listener) {
			final Listener l = (Listener) f;
			listViewExercises.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					l.onExerciseItemClick(position);
				}
			});
			buttonExerciseCreate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					l.onExerciseCreateClick();
				}
			});
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				listViewExercises.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				// TODO Move to other class
			} else {
				listViewExercises.setChoiceMode(ListView.CHOICE_MODE_NONE);
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
}