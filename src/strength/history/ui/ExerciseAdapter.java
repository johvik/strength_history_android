package strength.history.ui;

import java.util.List;

import strength.history.R;
import strength.history.data.structure.Exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExerciseAdapter extends BaseAdapter {
	private List<Exercise> exercises;
	private final Context context;

	public ExerciseAdapter(Context context, List<Exercise> exercises) {
		this.context = context;
		this.exercises = exercises;
	}

	@Override
	public int getCount() {
		return exercises.size();
	}

	@Override
	public Object getItem(int position) {
		return exercises.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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

		Exercise e = exercises.get(position);
		textView1.setText(e.getName());
		return convertView;
	}
}
