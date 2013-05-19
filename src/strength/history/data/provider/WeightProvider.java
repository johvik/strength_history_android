package strength.history.data.provider;

import java.util.Collection;
import java.util.LinkedHashSet;

import android.content.Context;

import strength.history.data.service.local.LocalWeightService;
import strength.history.data.structure.Weight;

/**
 * Provides data mappings for the weight service
 */
public class WeightProvider extends Provider<Weight> {
	public interface Events {
		public void deleteCallback(Weight e, boolean ok);

		public void insertCallback(Weight e, boolean ok);

		public void weightQueryCallback(Collection<Weight> e, boolean done);

		public void updateCallback(Weight old, Weight e, boolean ok);
	}

	public interface Provides {
		public void delete(Weight e, Context context);

		public void insert(Weight e, Context context);

		public void queryWeight(Context context);

		public void stop(Weight e, Context context);

		public void update(Weight e, Context context);
	}

	private LinkedHashSet<Events> listeners = new LinkedHashSet<Events>();

	@Override
	public void tryAddListener(Object object) {
		if (object instanceof Events) {
			Events e = (Events) object;
			e.weightQueryCallback(data, false); // Initial values
			listeners.add(e);
		}
	}

	@Override
	public void tryRemoveListener(Object object) {
		if (object instanceof Events) {
			listeners.remove((Events) object);
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
	protected void queryCallback(Collection<Weight> e, boolean done) {
		for (Events t : listeners) {
			t.weightQueryCallback(e, done);
		}
	}

	@Override
	protected void updateCallback(Weight old, Weight e, boolean ok) {
		for (Events t : listeners) {
			t.updateCallback(old, e, ok);
		}
	}
}
