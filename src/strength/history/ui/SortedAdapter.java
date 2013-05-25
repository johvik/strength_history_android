package strength.history.ui;

import strength.history.R;
import strength.history.data.SortedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class SortedAdapter<E> extends BaseAdapter {
	protected final Context context;
	protected final SortedList<E> list;
	protected boolean spinner;

	public SortedAdapter(Context context, SortedList<E> list, boolean spinner) {
		this.context = context;
		this.list = list;
		this.spinner = spinner;
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

		public ViewHolder(TextView text1) {
			this.text1 = text1;
		}
	}

	/**
	 * Override to provide custom string representation for single view
	 * 
	 * @param e
	 * @return
	 */
	protected String toString(E e) {
		return e.toString();
	}

	/**
	 * Override to provide custom stuff
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text1;
		if (convertView == null) {
			if (spinner) {
				convertView = LayoutInflater.from(context).inflate(
						android.R.layout.simple_spinner_item, parent, false);
				text1 = (TextView) convertView.findViewById(android.R.id.text1);
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_single, parent, false);
				text1 = (TextView) convertView.findViewById(R.id.text1);
			}
			convertView.setTag(new ViewHolder(text1));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
		}

		E e = list.get(position);
		text1.setText(toString(e));
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView text1;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);
			text1 = (TextView) convertView.findViewById(android.R.id.text1);
			convertView.setTag(new ViewHolder(text1));
		} else {
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			text1 = viewHolder.text1;
		}

		E e = list.get(position);
		text1.setText(toString(e));
		return convertView;
	}
}
