package strength.history.data;

import java.util.Collection;

import org.json.JSONException;
import org.json.JSONObject;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Exercise.MuscleGroup;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;
import junit.framework.Assert;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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
					dataProvider
							.insert(new Exercise("abc", MuscleGroup.ARMS,
									Exercise.DEFAULT_INCREASE),
									getApplicationContext());
					dataProvider.delete(e, getApplicationContext());
					dataProvider
							.insert(new Exercise("abc", MuscleGroup.ARMS,
									Exercise.DEFAULT_INCREASE),
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

	public static void testJSON() {
		try {
			{
				Exercise e1 = new Exercise(1, 2, "test", MuscleGroup.BACK, 65.2);
				JSONObject o1 = e1.toJSON();
				Exercise e2 = Exercise.fromJSON(o1);
				JSONObject o2 = e2.toJSON();
				Assert.assertEquals(o1.toString(), o2.toString());
			}
			{
				Workout e1 = new Workout(2, 3, "test");
				e1.add(55L);
				JSONObject o1 = e1.toJSON();
				Workout e2 = Workout.fromJSON(o1);
				JSONObject o2 = e2.toJSON();
				Assert.assertEquals(o1.toString(), o2.toString());
			}
			{
				Weight e1 = new Weight(2, 3, 5, 52.5);
				JSONObject o1 = e1.toJSON();
				Weight e2 = Weight.fromJSON(o1);
				JSONObject o2 = e2.toJSON();
				Assert.assertEquals(o1.toString(), o2.toString());
			}
			{
				WorkoutData e1 = new WorkoutData(3, 4, 5, 6);
				ExerciseData d = new ExerciseData(52, 242);
				d.add(new SetData(85, 984.2, 2));
				e1.add(d);
				JSONObject o1 = e1.toJSON();
				WorkoutData e2 = WorkoutData.fromJSON(o1);
				JSONObject o2 = e2.toJSON();
				Assert.assertEquals(o1.toString(), o2.toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		Log.d("DataTest", "testJSON done");
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
