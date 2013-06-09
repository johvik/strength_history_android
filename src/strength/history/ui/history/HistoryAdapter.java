package strength.history.ui.history;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import strength.history.R;
import strength.history.data.SortedList;
import strength.history.data.structure.Workout;

public class HistoryAdapter extends BaseAdapter {
	private final Context context;
	private SortedList<HistoryEvent> list;
	private SortedList<Workout> workouts;

	public HistoryAdapter(Context context, SortedList<HistoryEvent> list,
			SortedList<Workout> workouts) {
		this.context = context;
		this.list = list;
		this.workouts = workouts;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
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
					R.layout.list_item_small_split_data, parent, false);
			text1 = (TextView) convertView.findViewById(R.id.text1);
			text2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(new ViewHolder(text1, text2));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
			text2 = viewHolder.text2;
		}

		HistoryEvent e = list.get(position);
		text1.setText(e.getEventString(context, workouts));
		text2.setText(DateFormat.getMediumDateFormat(context).format(
				new Date(e.getTime())));
		return convertView;
	}
}
