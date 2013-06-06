package strength.history.ui.workout;

import java.util.Collection;
import java.util.Comparator;

import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
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

public class ActiveWorkoutListFragment extends Fragment implements
		WorkoutProvider.Events, WorkoutDataProvider.Events.LatestWorkoutData {
	public interface Listener {
		public void startWorkout(Workout w);
	}

	private ListView listViewActiveWorkouts;
	private SortedList<Pair<Workout, WorkoutData>> activeWorkoutList = new SortedList<Pair<Workout, WorkoutData>>(
			new Comparator<Pair<Workout, WorkoutData>>() {
				@Override
				public int compare(Pair<Workout, WorkoutData> lhs,
						Pair<Workout, WorkoutData> rhs) {
					// TODO Fix order
					WorkoutData lhsd = lhs.second;
					WorkoutData rhsd = rhs.second;
					if (lhsd == null) {
						if (rhsd == null) {
							return lhs.first.compareTo(rhs.first);
						} else {
							return -1;
						}
					} else if (rhsd == null) {
						return 1;
					} else {
						return lhsd.compareTo(rhsd);
					}
				}
			}, true);
	private ActiveWorkoutAdapter activeWorkoutAdapter;
	private DataProvider dataProvider = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof Listener) {
			activeWorkoutAdapter = new ActiveWorkoutAdapter(activity,
					activeWorkoutList);
		} else {
			throw new ClassCastException();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataProvider = DataListener.add(this);
		dataProvider.queryWorkout(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DataListener.remove(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_active_workout_list,
				container, false);
		listViewActiveWorkouts = (ListView) view
				.findViewById(R.id.listViewActiveWorkouts);
		listViewActiveWorkouts.setEmptyView(view
				.findViewById(R.id.textViewEmptyList));
		listViewActiveWorkouts.setAdapter(activeWorkoutAdapter);
		final Listener l = (Listener) getActivity();
		listViewActiveWorkouts
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						l.startWorkout(activeWorkoutList.get(position).first);
					}
				});
		return view;
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCallback(Workout old, Workout e, boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void workoutQueryCallback(Collection<Workout> e, boolean done) {
		for (Workout w : e) {
			activeWorkoutList.add(Pair.create(w, (WorkoutData) null));
			dataProvider.latestWorkoutData(w.getId(), getActivity());
		}
		activeWorkoutAdapter.notifyDataSetChanged();
	}

	@Override
	public void latestCallback(WorkoutData e, long workoutId, boolean ok) {
		// TODO Show loading
		if (ok) {
			for (int i = 0, j = activeWorkoutList.size(); i < j; i++) {
				long id = activeWorkoutList.get(i).first.getId();
				if (id == workoutId) {
					Pair<Workout, WorkoutData> p = activeWorkoutList.remove(i);
					activeWorkoutList.add(Pair.create(p.first, e));
					activeWorkoutAdapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}
}
