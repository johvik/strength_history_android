package strength.history.data.provider;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.DataListener;
import strength.history.data.service.local.LocalWeightService;
import strength.history.data.structure.Weight;

/**
 * Provides data mappings for the weight service
 */
public class WeightProvider extends Provider<Weight> {
	public interface Events {
		public void deleteCallback(Weight e, boolean ok);

		public void insertCallback(Weight e, boolean ok);

		public void weightQueryCallback(ArrayList<Weight> e, boolean ok);

		public void updateCallback(Weight e, boolean ok);
	}

	public interface Provides {
		public void delete(Weight e, Context context);

		public void insert(Weight e, Context context);

		public void query(Weight e, Context context);

		public void update(Weight e, Context context);
	}

	private LinkedHashSet<Events> listeners = new LinkedHashSet<Events>();

	@Override
	public void tryAddListener(DataListener dataListener) {
		if (dataListener instanceof Events) {
			listeners.add((Events) dataListener);
		}
	}

	@Override
	public void tryRemoveListener(DataListener dataListener) {
		if (dataListener instanceof Events) {
			listeners.add((Events) dataListener);
		}
	}

	@Override
	protected Class<?> getLocalServiceClass() {
		return LocalWeightService.class;
	}

	@Override
	protected String getDataFieldName() {
		return LocalWeightService.WEIGHT;
	}

	@Override
	protected void deleteCallback(Weight e, boolean ok) {
		for (Events t : listeners) {
			t.deleteCallback(e, ok);
		}
	}

	@Override
	protected void insertCallback(Weight e, boolean ok) {
		for (Events t : listeners) {
			t.insertCallback(e, ok);
		}
	}

	@Override
	protected void queryCallback(ArrayList<Weight> e, boolean ok) {
		for (Events t : listeners) {
			t.weightQueryCallback(e, ok);
		}
	}

	@Override
	protected void updateCallback(Weight e, boolean ok) {
		for (Events t : listeners) {
			t.updateCallback(e, ok);
		}
	}
}
