package strength.history.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import strength.history.R;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.ui.active.ActiveExerciseEditFragment;
import strength.history.ui.custom.CustomTitleFragmentActivity;

public class ExerciseDataEditActivity extends CustomTitleFragmentActivity
		implements ActiveExerciseEditFragment.Listener {
	public static final String EXERCISE_DATA = "exda";
	public static final String EXERCISE_NAME = "exna";
	public static final String SET_DATA = "sed";
	private static final String SELECTED_INDEX = "sei";
	private ActiveExerciseEditFragment activeExerciseEditFragment;
	private View menuItemDelete;
	private View menuItemEdit;
	private ExerciseData exerciseData;
	private SetData setData;
	private int selectedIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String exerciseName = getIntent().getStringExtra(EXERCISE_NAME);
		if (savedInstanceState == null) {
			Intent i = getIntent();
			exerciseData = i.getParcelableExtra(EXERCISE_DATA);
			setData = i.getParcelableExtra(SET_DATA);
			selectedIndex = AdapterView.INVALID_POSITION;
		} else {
			exerciseData = savedInstanceState.getParcelable(EXERCISE_DATA);
			selectedIndex = savedInstanceState.getInt(SELECTED_INDEX,
					AdapterView.INVALID_POSITION);
			setData = savedInstanceState.getParcelable(SET_DATA);
		}
		if (exerciseData == null || exerciseName == null) {
			Toast.makeText(this, R.string.error_exercise_data_not_found,
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Button buttonOk = (Button) findViewById(R.id.buttonOk);
			buttonOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.putExtra(EXERCISE_DATA,
							activeExerciseEditFragment.getExerciseData().first);
					setResult(RESULT_OK, i);
					finish();
				}
			});
			Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
			buttonCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED);
					finish();
				}
			});
			setTitle(exerciseName);
			setCustomBackButton(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED);
					finish();
				}
			});
			addMenuItem(createMenuItem(R.drawable.ic_action_plus,
					R.string.add_set, new OnClickListener() {
						@Override
						public void onClick(View v) {
							activeExerciseEditFragment.addSetData();
						}
					}));
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
			activeExerciseEditFragment = (ActiveExerciseEditFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragmentActiveExerciseEdit);
			updateMenu(selectedIndex != AdapterView.INVALID_POSITION);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		activeExerciseEditFragment.setExerciseData(Pair.create(exerciseData,
				Pair.create(selectedIndex, setData)));
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_exercisedata_edit;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Pair<ExerciseData, Pair<Integer, SetData>> p = activeExerciseEditFragment
				.getExerciseData();
		outState.putParcelable(EXERCISE_DATA, p.first);
		outState.putInt(SELECTED_INDEX, p.second.first);
		outState.putParcelable(SET_DATA, p.second.second);
	}

	private void updateMenu(boolean show) {
		removeMenuItem(menuItemEdit);
		removeMenuItem(menuItemDelete);
		if (show) {
			addMenuItem(menuItemDelete, 0);
			addMenuItem(menuItemEdit, 0);
		}
	}

	@Override
	public void onItemSelected() {
		updateMenu(true);
	}
}
