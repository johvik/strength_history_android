package strength.history.ui.active;

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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ActiveWorkoutListFragment extends Fragment implements
		WorkoutProvider.Events, WorkoutDataProvider.Events.LatestWorkoutData {
	public interface Listener {
		public void startWorkout(Workout w);

		public void setLoaded(boolean loaded);
	}

	private ListView listViewActiveWorkouts;
	private SortedList<Pair<Workout, WorkoutData>> activeWorkoutList = new SortedList<Pair<Workout, WorkoutData>>(
			new Comparator<Pair<Workout, WorkoutData>>() {
				@Override
				public int compare(Pair<Workout, WorkoutData> lhs,
						Pair<Workout, WorkoutData> rhs) {
					WorkoutData lhsd = lhs.second;
					WorkoutData rhsd = rhs.second;
					if (lhsd == null) {
						if (rhsd == null) {
							return lhs.first.getName().compareTo(
									rhs.first.getName());
						} else {
							return -1;
						}
					} else if (rhsd == null) {
						return 1;
					} else {
						return rhsd.compareTo(lhsd);
					}
				}
			}, true);
	private ActiveWorkoutAdapter activeWorkoutAdapter;
	private DataProvider dataProvider = null;
	private Listener masterActivity;
	private int totalWorkouts = 0;
	private int loadedWorkouts = 0;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof Listener) {
			masterActivity = (Listener) activity;
			activeWorkoutAdapter = new ActiveWorkoutAdapter(activity,
					activeWorkoutList);
		} else {
			throw new ClassCastException();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		activeWorkoutList.clear();
		masterActivity.setLoaded(false);
		dataProvider = DataListener.add(this);
		totalWorkouts = 0;
		loadedWorkouts = 0;
		dataProvider.queryWorkout(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
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
		listViewActiveWorkouts
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						masterActivity.startWorkout(activeWorkoutList
								.get(position).first);
					}
				});
		listViewActiveWorkouts
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						Intent i = new Intent(getActivity(),
								LatestWorkoutDataActivity.class);
						i.putExtra(LatestWorkoutDataActivity.WORKOUT,
								activeWorkoutList.get(position).first);
						startActivity(i);
						return true;
					}
				});
		return view;
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		if (ok) {
			long id = e.getId();
			for (int i = 0, j = activeWorkoutList.size(); i < j; i++) {
				if (id == activeWorkoutList.get(i).first.getId()) {
					activeWorkoutList.remove(i);
					totalWorkouts--;
					activeWorkoutAdapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		if (ok) {
			// New ones doesn't have history
			activeWorkoutList.add(Pair.create(e, (WorkoutData) null));
			totalWorkouts++;
			activeWorkoutAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void updateCallback(Workout old, Workout e, boolean ok) {
		if (ok) {
			long id = e.getId();
			for (int i = 0, j = activeWorkoutList.size(); i < j; i++) {
				if (id == activeWorkoutList.get(i).first.getId()) {
					// Use old data
					Pair<Workout, WorkoutData> p = activeWorkoutList.remove(i);
					activeWorkoutList.add(Pair.create(e, p.second));
					activeWorkoutAdapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}

	@Override
	public void workoutQueryCallback(Collection<Workout> e, boolean done) {
		totalWorkouts += e.size();
		if (done) {
			totalWorkouts = activeWorkoutList.size();
			if (totalWorkouts == 0) {
				masterActivity.setLoaded(true);
			}
		}
		for (Workout w : e) {
			activeWorkoutList.add(Pair.create(w, (WorkoutData) null));
			dataProvider.latestWorkoutData(w.getId(), getActivity());
		}
	}

	@Override
	public void latestCallback(WorkoutData e, long workoutId, boolean ok) {
		loadedWorkouts++;
		if (ok) {
			for (int i = 0, j = activeWorkoutList.size(); i < j; i++) {
				long id = activeWorkoutList.get(i).first.getId();
				if (id == workoutId) {
					Pair<Workout, WorkoutData> p = activeWorkoutList.remove(i);
					activeWorkoutList.add(Pair.create(p.first, e));
					break;
				}
			}
		}
		activeWorkoutAdapter.notifyDataSetChanged();
		if (loadedWorkouts >= totalWorkouts) {
			// TODO This might get messed up
			masterActivity.setLoaded(true);
		}
	}
}
