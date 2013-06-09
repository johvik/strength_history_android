package strength.history.ui.workout.active;

import strength.history.MainActivity;
import strength.history.R;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetDataAdapter extends BaseAdapter {
	private final Context context;
	private ExerciseData list = null;

	public SetDataAdapter(Context context) {
		this.context = context;
	}

	public void setList(ExerciseData list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
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
					R.layout.list_item_single_small, parent, false);
			text1 = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(new ViewHolder(text1));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
		}

		if (list != null) {
			SetData e = list.get(position);
			text1.setText(e.getRepetitions() + "x" + e.getWeight() + " "
					+ MainActivity.getUnit());
		}
		return convertView;
	}
}
