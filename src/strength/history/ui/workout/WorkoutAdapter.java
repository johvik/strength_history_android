package strength.history.ui.workout;

import android.content.Context;
import strength.history.data.SortedList;
import strength.history.data.structure.Workout;
import strength.history.ui.SortedAdapter;

public class WorkoutAdapter extends SortedAdapter<Workout> {
	public WorkoutAdapter(Context context, SortedList<Workout> list) {
		super(context, list);
	}

	@Override
	protected String toString(Workout e) {
		return e.getName();
	}
}
