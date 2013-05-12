package strength.history.data;

import java.util.ArrayList;

import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.provider.WorkoutProvider;
import strength.history.data.service.ServiceBase;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Weight;
import strength.history.data.structure.Workout;
import strength.history.data.structure.WorkoutData;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Combines several data providers into one
 */
public class DataProvider extends Handler implements ExerciseProvider.Provides,
		WeightProvider.Provides, WorkoutProvider.Provides,
		WorkoutDataProvider.Provides, ExerciseProvider.Events,
		WeightProvider.Events, WorkoutDataProvider.Events,
		WorkoutProvider.Events {
	/**
	 * Interface that contains callback events
	 */
	public interface Events {
		public void exerciseCallback(Iterable<Exercise> data);

		public void weightCallback(Iterable<Weight> data);

		public void workoutCallback(Iterable<Workout> data);

		public void workoutDataCallback(Iterable<WorkoutData> data);
	}

	private Messenger messenger = new Messenger(this);
	private ExerciseProvider<DataProvider> exerciseProvider = new ExerciseProvider<DataProvider>(
			this);
	private WeightProvider<DataProvider> weightProvider = new WeightProvider<DataProvider>(
			this);
	private WorkoutProvider<DataProvider> workoutProvider = new WorkoutProvider<DataProvider>(
			this);
	private WorkoutDataProvider<DataProvider> workoutDataProvider = new WorkoutDataProvider<DataProvider>(
			this);
	private DataListener dataListener = null;

	public void setListener(DataListener dataListener) {
		this.dataListener = dataListener;
		if (dataListener != null) {
			dataListener.exerciseCallback(exerciseProvider.get());
			dataListener.weightCallback(weightProvider.get());
			dataListener.workoutCallback(workoutProvider.get());
			dataListener.workoutDataCallback(workoutDataProvider.get());
		}
	}

	@Override
	public final void handleMessage(Message msg) {
		ServiceBase.Service service = ServiceBase.Service.parse(msg.arg1);
		ServiceBase.Request request = ServiceBase.Request.parse(msg.arg2);
		boolean ok = msg.what == 1;
		Log.d("MessageHandler", "service=" + service + " request=" + request
				+ " ok=" + ok);

		switch (service) {
		case EXERCISE:
			exerciseProvider.handleMessage(request, msg.obj, ok);
			break;
		case WEIGHT:
			weightProvider.handleMessage(request, msg.obj, ok);
			break;
		case WORKOUT:
			workoutProvider.handleMessage(request, msg.obj, ok);
			break;
		case WORKOUT_DATA:
			workoutDataProvider.handleMessage(request, msg.obj, ok);
			break;
		}
	}

	@Override
	public void delete(Exercise e) {
		exerciseProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Exercise e) {
		exerciseProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Exercise e) {
		exerciseProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Exercise e) {
		exerciseProvider.update(e, dataListener, messenger);
	}

	@Override
	public void delete(Weight e) {
		weightProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Weight e) {
		weightProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Weight e) {
		weightProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Weight e) {
		weightProvider.update(e, dataListener, messenger);
	}

	@Override
	public void delete(Workout e) {
		workoutProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(Workout e) {
		workoutProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(Workout e) {
		workoutProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(Workout e) {
		workoutProvider.update(e, dataListener, messenger);
	}

	@Override
	public void delete(WorkoutData e) {
		workoutDataProvider.delete(e, dataListener, messenger);
	}

	@Override
	public void insert(WorkoutData e) {
		workoutDataProvider.insert(e, dataListener, messenger);
	}

	@Override
	public void query(WorkoutData e) {
		workoutDataProvider.query(e, dataListener, messenger);
	}

	@Override
	public void update(WorkoutData e) {
		workoutDataProvider.update(e, dataListener, messenger);
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.exerciseCallback(exerciseProvider.get());
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.exerciseCallback(exerciseProvider.get());
	}

	@Override
	public void exerciseQueryCallback(ArrayList<Exercise> e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.exerciseCallback(exerciseProvider.get());
	}

	@Override
	public void updateCallback(Exercise e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.exerciseCallback(exerciseProvider.get());
	}

	@Override
	public void deleteCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.weightCallback(weightProvider.get());
	}

	@Override
	public void insertCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.weightCallback(weightProvider.get());
	}

	@Override
	public void weightQueryCallback(ArrayList<Weight> e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.weightCallback(weightProvider.get());
	}

	@Override
	public void updateCallback(Weight e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.weightCallback(weightProvider.get());
	}

	@Override
	public void deleteCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutDataCallback(workoutDataProvider.get());
	}

	@Override
	public void insertCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutDataCallback(workoutDataProvider.get());
	}

	@Override
	public void workoutDataQueryCallback(ArrayList<WorkoutData> e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutDataCallback(workoutDataProvider.get());
	}

	@Override
	public void updateCallback(WorkoutData e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutDataCallback(workoutDataProvider.get());
	}

	@Override
	public void deleteCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutCallback(workoutProvider.get());
	}

	@Override
	public void insertCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutCallback(workoutProvider.get());
	}

	@Override
	public void workoutQueryCallback(ArrayList<Workout> e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutCallback(workoutProvider.get());
	}

	@Override
	public void updateCallback(Workout e, boolean ok) {
		// TODO Auto-generated method stub
		dataListener.workoutCallback(workoutProvider.get());
	}
}
