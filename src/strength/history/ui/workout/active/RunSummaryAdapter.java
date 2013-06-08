package strength.history.ui.workout.active;

import strength.history.R;
import strength.history.ui.workout.RunWorkoutActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RunSummaryAdapter extends BaseAdapter {
	private final Context context;
	private RunWorkoutActivity master;

	public RunSummaryAdapter(Context context, RunWorkoutActivity master) {
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

		public ViewHolder(TextView text1) {
			this.text1 = text1;
		}
	}

	/**
	 * Override to provide custom stuff
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text1;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_single, parent, false);
			text1 = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(new ViewHolder(text1));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
		}

		// TODO Fix
		text1.setText(master.getItemString(position));
		return convertView;
	}
}
