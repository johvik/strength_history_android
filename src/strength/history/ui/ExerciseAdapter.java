package strength.history.ui;

import strength.history.R;
import strength.history.data.SortedList;
import strength.history.data.structure.Exercise;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExerciseAdapter extends SortedAdapter<Exercise> {
	public ExerciseAdapter(Context context, SortedList<Exercise> list) {
		super(context, list);
	}

	private static class ViewHolder {
		public final TextView text1;

		public ViewHolder(TextView text1) {
			this.text1 = text1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text1;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_exercise, parent, false);
			text1 = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(new ViewHolder(text1));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
		}

		Exercise e = list.get(position);
		text1.setText(e.getName());
		return convertView;
	}
}
