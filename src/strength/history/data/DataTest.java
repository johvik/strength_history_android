package strength.history.data;

import java.util.Collection;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.structure.Exercise;
import junit.framework.Assert;
import android.app.Activity;
import android.os.Bundle;

public class DataTest extends Activity implements ExerciseProvider.Events {
	private Exercise e = null;
	private DataProvider dataProvider = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataProvider = DataListener.add(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Remove all data
					dataProvider.purge(getApplicationContext());
					Thread.sleep(500);
					dataProvider.insert(new Exercise("abc"),
							getApplicationContext());
					dataProvider.delete(e, getApplicationContext());
					dataProvider.insert(new Exercise("abc"),
							getApplicationContext());
					dataProvider.update(e, getApplicationContext());
					dataProvider.queryExercise(getApplicationContext());
					// TODO Add more tests
					Thread.sleep(100);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DataListener.remove(this);
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
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		Assert.assertFalse(e.getId() == -1);
		Assert.assertEquals("abc", e.getName());
		Assert.assertTrue(ok);
	}
}
