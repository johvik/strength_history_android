package strength.history.ui.history;

import strength.history.R;
import strength.history.data.SortedList;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;

import android.content.Context;

public class HistoryEvent {
	private Weight w = null;
	private WorkoutData d = null;

	public HistoryEvent(Weight w) {
		this.w = w;
	}

	public HistoryEvent(WorkoutData d) {
		this.d = d;
	}

	public boolean isSame(HistoryEvent e) {
		if (w != null) {
			if (e.w != null) {
				return w.getId() == e.w.getId();
			}
		} else if (d != null) {
			if (e.d != null) {
				return d.getId() == e.d.getId();
			}
		}
		return false;
	}

	public boolean isWeight() {
		return w != null;
	}

	public Weight getWeight() {
		return w;
	}

	public WorkoutData getWorkoutData() {
		return d;
	}

	public String getEventString(Context context, SortedList<Workout> workouts) {
		if (d != null) {
			int pos = workouts.indexOf(new Workout(d.getWorkoutId(), 0, ""));
			if (pos != -1) {
				return workouts.get(pos).getName();
			} else {
				return "?";
			}
		} else {
			return context.getString(R.string.weight);
		}
	}

	public long getTime() {
		if (d != null) {
			return d.getTime();
		} else {
			return w.getTime();
		}
	}
}
