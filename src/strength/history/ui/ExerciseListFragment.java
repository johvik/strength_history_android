package strength.history.ui;

import strength.history.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ExerciseListFragment extends Fragment {
	public interface Listener {
		public void onExerciseItemClick(int position);

		public void onExerciseCreateClick();
	}

	private ListView listViewExercises;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity fa = getActivity();
		if (fa instanceof Listener) {
			final Listener l = (Listener) fa;
			listViewExercises.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					l.onExerciseItemClick(position);
				}
			});
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
		return view;
	}
}
