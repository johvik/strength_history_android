package strength.history;

import java.util.Comparator;

import strength.history.data.SortedList;
import strength.history.data.structure.Exercise;
import strength.history.ui.ExerciseAdapter;
import strength.history.ui.ExerciseEditFragment;
import strength.history.ui.ExerciseListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ExercisesActivity extends FragmentActivity implements
		ExerciseListFragment.Listener, ExerciseEditFragment.Listener {
	private static final int REQUEST_CODE = 1;

	private SortedList<Exercise> exerciseList;
	private ExerciseAdapter exerciseAdapter;
	private ExerciseEditFragment exerciseEditFragment;
	private TextView textSelectExerciseToEdit;
	private FrameLayout frameLayoutExerciseEditFragment;
	private ListView listViewExercises;
	private boolean mDualPane = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises);
		exerciseEditFragment = (ExerciseEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentExerciseEdit);
		mDualPane = findViewById(R.id.fragmentExerciseEdit) != null;
		if (mDualPane) {
			textSelectExerciseToEdit = (TextView) findViewById(R.id.textSelectExerciseToEdit);
			frameLayoutExerciseEditFragment = (FrameLayout) findViewById(R.id.frameLayoutExerciseEditFragment);
		}
		exerciseList = new SortedList<Exercise>(new Comparator<Exercise>() {
			@Override
			public int compare(Exercise lhs, Exercise rhs) {
				int c = lhs.getName().compareTo(rhs.getName());
				if (c == 0) {
					c = lhs.compareTo(rhs);
				}
				return c;
			}
		});
		exerciseList.add(new Exercise("a"));
		exerciseList.add(new Exercise("aasd"));
		exerciseList.add(new Exercise("aqwe"));
		exerciseList.add(new Exercise("aqweqwe"));
		exerciseList.add(new Exercise("wewa"));
		exerciseAdapter = new ExerciseAdapter(this, exerciseList);
		listViewExercises = (ListView) findViewById(R.id.listViewExercises);
		listViewExercises.setAdapter(exerciseAdapter);
	}

	private void starteEditExercise(Exercise e) {
		if (e != null) {
			// TODO Copy
			// listViewExercises.setItemChecked(position, true);
			if (mDualPane) {
				textSelectExerciseToEdit.setVisibility(View.GONE);
				frameLayoutExerciseEditFragment.setVisibility(View.VISIBLE);
				exerciseEditFragment.setExercise(e);
			} else {
				Intent intent = new Intent(this, ExerciseEditActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// TODO
				// saveCallback();
			} else if (resultCode == RESULT_CANCELED) {
				cancelCallback();
			} else if (resultCode == ExerciseEditActivity.RESULT_ORIENTATION) {
				// TODO
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void saveCallback(Exercise e) {
		// TODO
		if (mDualPane) {
			textSelectExerciseToEdit.setVisibility(View.VISIBLE);
			frameLayoutExerciseEditFragment.setVisibility(View.GONE);
		}
	}

	@Override
	public void cancelCallback() {
		listViewExercises.clearChoices();
		exerciseAdapter.notifyDataSetChanged();

		if (mDualPane) {
			textSelectExerciseToEdit.setVisibility(View.VISIBLE);
			frameLayoutExerciseEditFragment.setVisibility(View.GONE);
		}
	}

	@Override
	public void onExerciseItemClick(int position) {
		starteEditExercise(exerciseList.get(position));
	}

	@Override
	public void onExerciseCreateClick() {
		starteEditExercise(new Exercise(""));
	}
}
