package strength.history.ui.history;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import strength.history.data.structure.WorkoutData;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.ui.active.WorkoutDataSummaryAdapter;
import strength.history.ui.custom.CustomTitleFragmentActivity;

public class WorkoutDataEditActivity extends CustomTitleFragmentActivity
		implements ExerciseProvider.Events, WorkoutDataSummaryAdapter.Master,
		WorkoutDataProvider.Events.LatestExerciseData {
	public static final String WORKOUT_DATA = "woda";
	public static final String WORKOUT_NAME = "wona";
	private static final String EDIT_POSITION = "epos";
	private static final int EDIT_EXERCISE_DATA = 10;
	private WorkoutData workoutData = null;
	private SortedList<Exercise> exercises = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					return lhs.compareTo(rhs);
				}
			}, true);
	private DataProvider dataProvider;
	private WorkoutDataSummaryAdapter workoutDataSummaryAdapter;
	private int editPosition = AdapterView.INVALID_POSITION;
	private boolean exercisesLoaded = false;
	private int latestTotalCount = 0;
	private int latestCallbackCount = 0;
	private HashMap<Long, SetData> latestBestSet = new HashMap<Long, SetData>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		String workoutName = i.getStringExtra(WORKOUT_NAME);
		if (savedInstanceState == null) {
			workoutData = i.getParcelableExtra(WORKOUT_DATA);
		} else {
			workoutData = savedInstanceState.getParcelable(WORKOUT_DATA);
			editPosition = savedInstanceState.getInt(EDIT_POSITION);
		}
		if (workoutName == null || workoutData == null) {
			Toast.makeText(this, R.string.error_workout_data_not_found,
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			setTitle(workoutName);
			setCustomBackButton(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			addMenuItem(createMenuItem(R.drawable.ic_action_save,
					R.string.save_changes, new OnClickListener() {
						@Override
						public void onClick(View v) {
							dataProvider.update(workoutData,
									getApplicationContext());
							finish();
						}
					}));
			workoutDataSummaryAdapter = new WorkoutDataSummaryAdapter(this,
					this);
			ListView listViewExerciseData = (ListView) findViewById(R.id.listViewExerciseData);
			listViewExerciseData
					.setEmptyView(findViewById(R.id.textViewEmptyList));
			listViewExerciseData.setAdapter(workoutDataSummaryAdapter);
			listViewExerciseData
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent i = new Intent(WorkoutDataEditActivity.this,
									ExerciseDataEditActivity.class);
							Pair<ExerciseData, Exercise> p = getPairItem(position);
							Exercise e = p.second;
							ExerciseData d = p.first.copy();
							i.putExtra(ExerciseDataEditActivity.EXERCISE_DATA,
									d);
							SetData s = latestBestSet.get(d.getExerciseId());
							i.putExtra(ExerciseDataEditActivity.SET_DATA, s);
							i.putExtra(ExerciseDataEditActivity.EXERCISE_NAME,
									e != null ? e.getName() : "?");
							i.putExtra(ExerciseDataEditActivity.INCREASE,
									e != null ? e.getStandardIncrease()
											: Exercise.DEFAULT_INCREASE);
							editPosition = position;
							startActivityForResult(i, EDIT_EXERCISE_DATA);
						}
					});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_EXERCISE_DATA) {
			if (resultCode == RESULT_OK) {
				ExerciseData d = data
						.getParcelableExtra(ExerciseDataEditActivity.EXERCISE_DATA);
				if (d != null) {
					if (editPosition >= 0 && editPosition < workoutData.size()) {
						workoutData.set(editPosition, d);
						workoutDataSummaryAdapter.notifyDataSetChanged();
					}
				}
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		exercisesLoaded = false;
		setCustomProgressBarVisibility(true);
		dataProvider = DataListener.add(this);
		Context c = getApplicationContext();
		dataProvider.queryExercise(c);
		if (workoutData != null) {
			latestBestSet.clear();
			latestTotalCount = workoutData.size();
			latestCallbackCount = 0;
			for (ExerciseData d : workoutData) {
				dataProvider.latestExerciseData(d.getExerciseId(), c);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		DataListener.remove(this);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_workoutdata_edit;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(WORKOUT_DATA, workoutData);
		outState.putInt(EDIT_POSITION, editPosition);
	}

	private void update() {
		if (exercisesLoaded && latestCallbackCount >= latestTotalCount) {
			setCustomProgressBarVisibility(false);
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
			update();
			workoutDataSummaryAdapter.notifyDataSetChanged();
		}
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
				MuscleGroup.DEFAULT, Exercise.DEFAULT_INCREASE));
		Exercise e = null;
		if (pos != -1) {
			e = exercises.get(pos);
		}
		return Pair.create(d, e);
	}

	@Override
	public void latestCallback(ExerciseData e, long exerciseId, boolean ok) {
		if (ok) {
			latestBestSet.put(exerciseId, e.getBestWeightSet());
		}
		latestCallbackCount++;
		update();
	}
}
