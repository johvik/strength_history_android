package strength.history.ui.active;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import strength.history.R;
import strength.history.data.SortedList;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import strength.history.ui.SortedAdapter;

public class ActiveWorkoutAdapter extends
		SortedAdapter<Pair<Workout, WorkoutData>> {

	public ActiveWorkoutAdapter(Context context,
			SortedList<Pair<Workout, WorkoutData>> list) {
		super(context, list, false);
	}

	private static class ViewHolder {
		public final TextView text1;
		public final TextView text2;

		public ViewHolder(TextView text1, TextView text2) {
			this.text1 = text1;
			this.text2 = text2;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text1;
		TextView text2;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_active_workout, parent, false);
			text1 = (TextView) convertView.findViewById(R.id.text1);
			text2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(new ViewHolder(text1, text2));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
			text2 = viewHolder.text2;
		}

		Pair<Workout, WorkoutData> e = list.get(position);
		Workout w = e.first;
		text1.setText(w.getName());
		WorkoutData d = e.second;
		if (d != null) {
			text2.setText(DateFormat.getMediumDateFormat(context).format(
					d.getTime()));
			text2.setVisibility(View.VISIBLE);
		} else {
			text2.setVisibility(View.GONE);
		}
		return convertView;
	}
}
