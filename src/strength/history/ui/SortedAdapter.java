package strength.history.ui;

import strength.history.data.SortedList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SortedAdapter<E> extends BaseAdapter {
	protected final Context context;
	protected final SortedList<E> list;

	public SortedAdapter(Context context, SortedList<E> list) {
		this.context = context;
		this.list = list;
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

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);
}
