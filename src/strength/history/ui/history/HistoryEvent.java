package strength.history.ui.history;

import strength.history.R;
import strength.history.data.SortedList;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;

import android.content.Context;

public class HistoryEvent implements Comparable<HistoryEvent> {
	private Weight w = null;
	private WorkoutData d = null;

	public HistoryEvent(Weight w) {
		this.w = w;
	}

	public HistoryEvent(WorkoutData d) {
		this.d = d;
	}

	@Override
	public int compareTo(HistoryEvent another) {
		boolean isWeight = isWeight();
		boolean anoterIsWeight = another.isWeight();
		if (isWeight && anoterIsWeight) {
			// Both weight
			return w.compareTo(another.w);
		} else if (!isWeight && !anoterIsWeight) {
			// Both workout data
			return d.compareTo(another.d);
		} else {
			// Different type
			// Descending time
			int c = Long.valueOf(another.getTime()).compareTo(getTime());
			if (c == 0) {
				// For consistency
				if (isWeight) {
					return -1;
				} else {
					return 1;
				}
			}
			return c;
		}
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
			Workout w = d.getWorkout(workouts);
			if (w != null) {
				return w.getName();
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
