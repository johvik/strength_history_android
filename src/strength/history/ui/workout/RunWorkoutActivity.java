package strength.history.ui.workout;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.ui.active.ActiveExerciseEditFragment;
import strength.history.ui.active.WorkoutDataSummaryAdapter;
import strength.history.ui.custom.CustomTitleFragmentActivity;

public class RunWorkoutActivity extends CustomTitleFragmentActivity implements
		ExerciseProvider.Events, WorkoutDataProvider.Events.LatestExerciseData,
		WorkoutDataSummaryAdapter.Master, ActiveExerciseEditFragment.Listener {
	public static final String WORKOUT = "rwork";
	public static final String TIME = "rtime";
	private static final String WORKOUT_DATA = "rwdata";
	private static final String SAVED_SET_DATA = "ssdata";
	private static final String INDEX = "rindex";
	private Workout workout = null;
	private long time = -1;
	private WorkoutData workoutData = null;
	private HashMap<Long, Pair<Integer, SetData>> savedSetData = new HashMap<Long, Pair<Integer, SetData>>();
	private int index = 0;
	private ActiveExerciseEditFragment activeExerciseEditFragment;
	private View fragmentActiveExerciseEditView;
	private View menuItemCreate;
	private View menuItemDelete;
	private View menuItemEdit;
	private Button buttonNext;
	private ListView listViewRunSummary;
	private WorkoutDataSummaryAdapter runSummaryAdapter;
	private SortedList<Exercise> exercises = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					return lhs.compareTo(rhs);
				}
			}, true);
	private boolean exercisesLoaded = false;
	private int latestLoaded = 0;
	private boolean showedWarning = false;
	private Toast toastWarning;
	private DataProvider dataProvider = null;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workout = getIntent().getParcelableExtra(WORKOUT);
		time = getIntent().getLongExtra(TIME, -1);
		if (savedInstanceState != null) {
			index = savedInstanceState.getInt(INDEX, 0);
			workoutData = savedInstanceState.getParcelable(WORKOUT_DATA);
			savedSetData = (HashMap<Long, Pair<Integer, SetData>>) savedInstanceState
					.getSerializable(SAVED_SET_DATA);
		}
		if (workout != null && time != -1) {
			toastWarning = Toast.makeText(this, R.string.run_back_warning,
					Toast.LENGTH_SHORT);
			setTitle(workout.getName());
			activeExerciseEditFragment = (ActiveExerciseEditFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragmentActiveExerciseEdit);
			fragmentActiveExerciseEditView = findViewById(R.id.fragmentActiveExerciseEdit);
			fragmentActiveExerciseEditView.setVisibility(View.GONE);
			listViewRunSummary = (ListView) findViewById(R.id.listViewRunSummary);
			runSummaryAdapter = new WorkoutDataSummaryAdapter(this, this);
			listViewRunSummary.setAdapter(runSummaryAdapter);
			menuItemCreate = createMenuItem(R.drawable.ic_action_plus,
					R.string.add_set, new OnClickListener() {
						@Override
						public void onClick(View v) {
							activeExerciseEditFragment.addSetData();
						}
					});
			menuItemDelete = createMenuItem(R.drawable.ic_action_delete,
					R.string.delete_selected, new OnClickListener() {
						@Override
						public void onClick(View v) {
							updateMenu(false);
							activeExerciseEditFragment.removeSetData();
						}
					});
			menuItemEdit = createMenuItem(R.drawable.ic_action_edit,
					R.string.edit_selected, new OnClickListener() {
						@Override
						public void onClick(View v) {
							activeExerciseEditFragment.editSetData();
						}
					});
			Button buttonPrevious = (Button) findViewById(R.id.buttonRunPrevious);
			buttonNext = (Button) findViewById(R.id.buttonRunNext);
			buttonPrevious.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					previous();
				}
			});
			buttonNext.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showedWarning = false;
					toastWarning.cancel();
					update(1);
				}
			});
			setCustomBackButton(new OnClickListener() {
				@Override
				public void onClick(View v) {
					previous();
				}
			});
		} else {
			Toast.makeText(this, R.string.run_workout_error, Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO Change all to resume/pause!
		setCustomProgressBarVisibility(true);
		dataProvider = DataListener.add(this);
		Context c = getApplicationContext();
		if (workoutData == null) {
			latestLoaded = 0;
			workoutData = new WorkoutData(time, workout.getId());
			for (Long l : workout) {
				workoutData.add(new ExerciseData(l));
				dataProvider.latestExerciseData(l, c);
			}
		} else {
			latestLoaded = workoutData.size();
		}
		exercisesLoaded = false;
		dataProvider.queryExercise(c);
	}

	@Override
	protected void onPause() {
		super.onPause();
		DataListener.remove(this);
		toastWarning.cancel();
		save();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(INDEX, index);
		outState.putParcelable(WORKOUT_DATA, workoutData);
		outState.putSerializable(SAVED_SET_DATA, savedSetData);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_run_workout;
	}

	@Override
	public void onBackPressed() {
		previous();
	}

	@Override
	public ExerciseData getItem(int position) {
		return workoutData == null ? null : workoutData.get(position);
	}

	@Override
	public int getSize() {
		return workoutData == null ? 0 : workoutData.size();
	}

	@Override
	public Pair<ExerciseData, Exercise> getPairItem(int position) {
		if (workoutData == null) {
			return Pair.create(null, null);
		}
		ExerciseData d = workoutData.get(position);
		int pos = exercises.indexOf(new Exercise(d.getExerciseId(), 0, "",
				MuscleGroup.DEFAULT));
		Exercise e = null;
		if (pos != -1) {
			e = exercises.get(pos);
		}
		return Pair.create(d, e);
	}

	private void save() {
		if (index >= 0 && index < workoutData.size()) {
			Pair<ExerciseData, Pair<Integer, SetData>> p = activeExerciseEditFragment
					.getExerciseData();
			savedSetData.put(p.first.getExerciseId(), p.second);
			workoutData.set(index, p.first);
		}
	}

	private void previous() {
		if (index == 0) {
			if (showedWarning) {
				finish(); // Cancel
			} else {
				showedWarning = true;
				toastWarning.show();
			}
		}
		update(-1);
	}

	private void updateMenu(boolean show) {
		removeMenuItem(menuItemEdit);
		removeMenuItem(menuItemDelete);
		if (show) {
			addMenuItem(menuItemDelete, 0);
			addMenuItem(menuItemEdit, 0);
		}
	}

	private void update(int change) {
		int size = workoutData.size();
		if (exercisesLoaded && latestLoaded >= size) {
			setCustomProgressBarVisibility(false);
			if (change != 0) {
				save();
			}
			index += change;
			if (index < 0) {
				index = 0;
			}
			if (index > size) {
				index = size;
				// Save
				dataProvider.insert(workoutData, getApplicationContext());
				finish();
			}
			if (index < size) {
				buttonNext.setText(R.string.next);
				ExerciseData e = workoutData.get(index);
				int pos = exercises.indexOf(new Exercise(e.getExerciseId(), 0,
						"", MuscleGroup.DEFAULT));
				if (pos != -1) {
					Exercise ex = exercises.get(pos);
					setTitle((index + 1) + "/" + size + " " + ex.getName());
				}
				Pair<Integer, SetData> p = savedSetData.get(e.getExerciseId());
				int selectedIndex = p.first;
				activeExerciseEditFragment.setExerciseData(Pair.create(e,
						Pair.create(selectedIndex, p.second)));
				fragmentActiveExerciseEditView.setVisibility(View.VISIBLE);
				listViewRunSummary.setVisibility(View.GONE);
				removeMenuItem(menuItemCreate);
				addMenuItem(menuItemCreate);
				updateMenu(selectedIndex != -1);
			} else {
				buttonNext.setText(R.string.finish);
				setTitle(R.string.summary);
				fragmentActiveExerciseEditView.setVisibility(View.GONE);
				runSummaryAdapter.notifyDataSetChanged();
				listViewRunSummary.setVisibility(View.VISIBLE);
				updateMenu(false);
				removeMenuItem(menuItemCreate);
			}
		}
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		if (ok) {
			exercises.remove(e);
		}
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		if (ok) {
			exercises.add(e);
		}
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		if (ok) {
			exercises.remove(old);
			exercises.add(e);
		}
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		exercises.addAll(e);
		if (done) {
			exercisesLoaded = true;
			update(0);
		}
	}

	@Override
	public void latestCallback(ExerciseData e, long exerciseId, boolean ok) {
		savedSetData.put(exerciseId,
				Pair.create(-1, e == null ? null : e.getBestWeightSet()));
		latestLoaded++;
		update(0);
	}

	@Override
	public void onItemSelected() {
		updateMenu(true);
	}
}
