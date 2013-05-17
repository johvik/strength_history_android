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
		public final TextView textView1;

		public ViewHolder(TextView textView1) {
			this.textView1 = textView1;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView1;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_exercise, parent, false);
			textView1 = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(new ViewHolder(textView1));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			textView1 = viewHolder.textView1;
		}

		Exercise e = list.get(position);
		textView1.setText(e.getName());
		return convertView;
	}
}
