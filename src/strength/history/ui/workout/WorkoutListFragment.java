package strength.history.ui.workout;

import strength.history.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class WorkoutListFragment extends Fragment {
	public interface Listener {
		public void onWorkoutItemClick(int position);

		public void onWorkoutCreateClick();
	}

	private ListView listViewWorkouts;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity fa = getActivity();
		if (fa instanceof Listener) {
			final Listener l = (Listener) fa;
			listViewWorkouts.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					l.onWorkoutItemClick(position);
				}
			});
		} else {
			throw new ClassCastException();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_workout_list, container,
				false);
		listViewWorkouts = (ListView) view.findViewById(R.id.listViewWorkouts);
		listViewWorkouts
				.setEmptyView(view.findViewById(R.id.textViewEmptyList));
		return view;
	}
}
