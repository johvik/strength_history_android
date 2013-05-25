package strength.history.ui.exercise;

import strength.history.data.SortedList;
import strength.history.data.structure.Exercise;
import strength.history.ui.SortedAdapter;
import android.content.Context;

public class ExerciseAdapter extends SortedAdapter<Exercise> {
	public ExerciseAdapter(Context context, SortedList<Exercise> list,
			boolean spinner) {
		super(context, list, spinner);
	}

	@Override
	protected String toString(Exercise e) {
		return e.getName();
	}
}
