package strength.history.data;

import java.util.Collection;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.structure.Exercise;
import junit.framework.Assert;
import android.os.Bundle;

public class DataTest extends DataListener implements ExerciseProvider.Events {
	private Exercise e = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO Clear DB and provider
					Thread.sleep(100);
					data.insert(new Exercise("abc"), getApplicationContext());
					Thread.sleep(100);
					data.delete(e, getApplicationContext());
					Thread.sleep(100);
					data.insert(new Exercise("abc"), getApplicationContext());
					Thread.sleep(100);
					data.update(e, getApplicationContext());
					Thread.sleep(100);
					data.query((Exercise) null, getApplicationContext());
					Thread.sleep(100);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		Assert.assertFalse(e.getId() == -1);
		Assert.assertEquals("abc", e.getName());
		Assert.assertTrue(ok);
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		Assert.assertFalse(e.getId() == -1);
		Assert.assertEquals("abc", e.getName());
		Assert.assertTrue(ok);
		this.e = e;
	}

	private boolean first = true;

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		if (!first) {
			Assert.assertEquals(1, e.size());
			for (Exercise ex : e) {
				Assert.assertEquals("abc", ex.getName());
			}
			Assert.assertTrue(done);
		} else {
			// The initial callback
			Assert.assertFalse(done);
			first = false;
		}
	}

	@Override
	public void updateCallback(Exercise e, boolean ok) {
		Assert.assertFalse(e.getId() == -1);
		Assert.assertEquals("abc", e.getName());
		Assert.assertTrue(ok);
	}
}
