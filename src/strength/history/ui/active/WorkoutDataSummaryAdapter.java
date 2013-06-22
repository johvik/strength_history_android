package strength.history.ui.active;

import strength.history.MainActivity;
import strength.history.R;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WorkoutDataSummaryAdapter extends BaseAdapter {
	public interface Master {
		int getSize();

		ExerciseData getItem(int position);

		Pair<ExerciseData, Exercise> getPairItem(int position);
	}

	private final Context context;
	private Master master;

	public WorkoutDataSummaryAdapter(Context context, Master master) {
		this.context = context;
		this.master = master;
	}

	@Override
	public int getCount() {
		return master.getSize();
	}

	@Override
	public Object getItem(int position) {
		return master.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		public final TextView text1;
		public final TextView text2;

		public ViewHolder(TextView text1, TextView text2) {
			this.text1 = text1;
			this.text2 = text2;
		}
	}

	/**
	 * Override to provide custom stuff
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text1;
		TextView text2;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_exercise_data, parent, false);
			text1 = (TextView) convertView.findViewById(R.id.text1);
			text2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(new ViewHolder(text1, text2));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
			text2 = viewHolder.text2;
		}

		Pair<ExerciseData, Exercise> p = master.getPairItem(position);
		ExerciseData d = p.first;
		Exercise e = p.second;
		if (e != null) {
			text1.setText(e.getName());
		} else {
			text1.setText("?");
		}
		if (d != null) {
			String r = "";
			for (SetData s : d) {
				r += (r == "" ? "" : "\n") + s.getRepetitions() + "x"
						+ s.getWeight() + " " + MainActivity.getUnit();
			}
			if (r.length() == 0) {
				text2.setText("-");
			} else {
				text2.setText(r);
			}
		} else {
			text2.setText("-");
		}
		return convertView;
	}
}
