package strength.history.ui.history;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import strength.history.MainActivity;
import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.SortedList;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.provider.WeightProvider;
import strength.history.data.provider.WorkoutDataProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.ExerciseData;
import strength.history.data.structure.SetData;
import strength.history.data.structure.Weight;
import strength.history.data.structure.WorkoutData;
import strength.history.ui.custom.CustomTitleFragmentActivity;
import strength.history.ui.dialog.StringSelectDialog;

public class HistoryChartActivity extends CustomTitleFragmentActivity implements
		WorkoutDataProvider.Events, WeightProvider.Events,
		ExerciseProvider.Events, StringSelectDialog.Master {
	private static final String SELECTED_INDEX = "sind";
	private static final int[] COLOR = { Color.BLUE, Color.RED, Color.GREEN,
			Color.MAGENTA, Color.CYAN };
	private XYMultipleSeriesDataset series;
	private XYMultipleSeriesRenderer renderer;
	private GraphicalView chartView;
	private boolean weightLoaded = false;
	private boolean workoutDataLoaded = false;
	private boolean exercisesLoaded = false;
	private SortedList<Exercise> exercises = new SortedList<Exercise>(
			new Comparator<Exercise>() {
				@Override
				public int compare(Exercise lhs, Exercise rhs) {
					int c = lhs.getName().compareTo(rhs.getName());
					if (c == 0) {
						c = lhs.compareTo(rhs);
					}
					return c;
				}
			}, true);
	private SortedList<WorkoutData> workoutData = new SortedList<WorkoutData>(
			new Comparator<WorkoutData>() {
				@Override
				public int compare(WorkoutData lhs, WorkoutData rhs) {
					return rhs.compareTo(lhs);
				}
			}, true);
	private SortedList<Weight> weightData = new SortedList<Weight>(
			new Comparator<Weight>() {
				@Override
				public int compare(Weight lhs, Weight rhs) {
					return rhs.compareTo(lhs);
				}
			}, true);
	private Comparator<Pair<Long, SetData>> pairCmp = new Comparator<Pair<Long, SetData>>() {
		@Override
		public int compare(Pair<Long, SetData> lhs, Pair<Long, SetData> rhs) {
			int c = rhs.first.compareTo(lhs.first);
			if (c == 0) {
				return lhs.second.compareTo(rhs.second);
			}
			return c;
		}
	};
	private HashMap<Long, SortedList<Pair<Long, SetData>>> groupedData = new HashMap<Long, SortedList<Pair<Long, SetData>>>();
	private int selectedIndex = 0;
	private StringSelectDialog stringSelectDialog = null;

	// TODO Chart hangs when two values share the same y-value

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.charts);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		addMenuItem(createMenuItem(R.drawable.ic_action_open,
				R.string.select_chart, new OnClickListener() {
					@Override
					public void onClick(View v) {
						showStringSelectDialog();
					}
				}));

		if (savedInstanceState != null) {
			selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0);
		}

		Button buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
		buttonPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				previous();
			}
		});
		Button buttonNext = (Button) findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next();
			}
		});

		series = new XYMultipleSeriesDataset();
		renderer = new XYMultipleSeriesRenderer();
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setXTitle(getString(R.string.chart_label_x));
		renderer.setYTitle(getString(R.string.chart_label_y,
				MainActivity.getUnit()));
		renderer.setXLabelsColor(Color.argb(255, 0, 0, 0));
		renderer.setYLabelsColor(0, Color.argb(255, 0, 0, 0));
		renderer.setAxesColor(Color.argb(255, 0, 0, 0));
		renderer.setLabelsColor(Color.argb(255, 0, 0, 0));
		renderer.setMarginsColor(Color.argb(0, 1, 1, 1));

		chartView = ChartFactory.getTimeChartView(this, series, renderer,
				"MMM d yyyy");
		FrameLayout l = (FrameLayout) findViewById(R.id.frameLayoutChart);
		l.addView(chartView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		DataProvider dataProvider = DataListener.remove(this);
		Context c = getApplicationContext();
		dataProvider.stop((WorkoutData) null, c);
		dataProvider.stop((Weight) null, c);
		if (stringSelectDialog != null) {
			stringSelectDialog.dismiss();
		}
		weightData.clear();
		workoutData.clear();
		exercises.clear();
		groupedData.clear();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SELECTED_INDEX, selectedIndex);
	}

	@Override
	protected void onResume() {
		super.onResume();
		weightLoaded = false;
		workoutDataLoaded = false;
		exercisesLoaded = false;
		weightData.clear();
		workoutData.clear();
		exercises.clear();
		groupedData.clear();
		setCustomProgressBarVisibility(true);
		DataProvider dataProvider = DataListener.add(this);
		Context c = getApplicationContext();
		dataProvider.queryWorkoutData(c);
		dataProvider.queryWeight(c);
		dataProvider.queryExercise(c);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_history_chart;
	}

	private void showStringSelectDialog() {
		FragmentManager fm = getSupportFragmentManager();
		stringSelectDialog = new StringSelectDialog();
		stringSelectDialog.show(fm, "fragment_string_select_dialog");
	}

	private void update() {
		if (weightLoaded && workoutDataLoaded && exercisesLoaded) {
			setCustomProgressBarVisibility(false);
			groupData();
			resetChart();
		}
	}

	private void groupData() {
		// Groups data by exercise id
		groupedData.clear();
		for (WorkoutData w : workoutData) {
			for (ExerciseData d : w) {
				long id = d.getExerciseId();
				SetData s = d.getBestWeightSet();
				if (s != null) {
					SortedList<Pair<Long, SetData>> l = groupedData.get(id);
					if (l == null) {
						l = new SortedList<Pair<Long, SetData>>(pairCmp, true);
					}
					l.add(Pair.create(w.getTime(), s));
					groupedData.put(id, l);
				}
			}
		}
	}

	private void select(int index) {
		int size = exercises.size();
		if (index < 0) {
			index = size;
		} else if (index > size) {
			index = 0;
		}
		selectedIndex = index;
		resetChart();
	}

	private void previous() {
		select(selectedIndex - 1);
	}

	private void next() {
		select(selectedIndex + 1);
	}

	private void addSeries(int index) {
		XYSeries s;
		if (index == 0) {
			s = new XYSeries(getString(R.string.weight));
			for (Weight w : weightData) {
				s.add(w.getTime(), w.getWeight());
			}
		} else {
			Exercise e = exercises.get(index - 1);
			s = new XYSeries(e.getName());
			long id = e.getId();
			SortedList<Pair<Long, SetData>> data = groupedData.get(id);
			if (data != null) {
				for (Pair<Long, SetData> p : data) {
					s.add(p.first, p.second.getWeight());
				}
			}
		}
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(COLOR[series.getSeriesCount() % COLOR.length]);
		series.addSeries(s);
		renderer.addSeriesRenderer(r);
		chartView.repaint();
	}

	private void resetChart() {
		series.clear();
		renderer.removeAllRenderers();
		addSeries(selectedIndex);
	}

	@Override
	public void deleteCallback(Weight e, boolean ok) {
		if (ok) {
			weightData.remove(e);
			update();
		}
	}

	@Override
	public void insertCallback(Weight e, boolean ok) {
		if (ok) {
			weightData.add(e);
			update();
		}
	}

	@Override
	public void updateCallback(Weight old, Weight e, boolean ok) {
		if (ok) {
			weightData.remove(old);
			weightData.add(e);
			update();
		}
	}

	@Override
	public void weightQueryCallback(Collection<Weight> e, boolean done) {
		weightData.addAll(e);
		if (done) {
			weightLoaded = true;
			update();
		}
	}

	@Override
	public void deleteCallback(WorkoutData e, boolean ok) {
		if (ok) {
			workoutData.remove(e);
			update();
		}
	}

	@Override
	public void insertCallback(WorkoutData e, boolean ok) {
		if (ok) {
			workoutData.add(e);
			update();
		}
	}

	@Override
	public void updateCallback(WorkoutData old, WorkoutData e, boolean ok) {
		if (ok) {
			workoutData.remove(old);
			workoutData.add(e);
			update();
		}
	}

	@Override
	public void workoutDataQueryCallback(Collection<WorkoutData> e, boolean done) {
		workoutData.addAll(e);
		if (done) {
			workoutDataLoaded = true;
			update();
		}
	}

	@Override
	public void deleteCallback(Exercise e, boolean ok) {
		if (ok) {
			exercises.remove(e);
			update();
		}
	}

	@Override
	public void insertCallback(Exercise e, boolean ok) {
		if (ok) {
			exercises.add(e);
			update();
		}
	}

	@Override
	public void updateCallback(Exercise old, Exercise e, boolean ok) {
		if (ok) {
			exercises.remove(old);
			exercises.add(e);
			update();
		}
	}

	@Override
	public void exerciseQueryCallback(Collection<Exercise> e, boolean done) {
		exercises.addAll(e);
		if (done) {
			exercisesLoaded = true;
			update();
		}
	}

	@Override
	public String getStringSelectTitle() {
		return getString(R.string.select_chart);
	}

	@Override
	public CharSequence[] getStringSelectArray() {
		int size = exercises.size();
		CharSequence[] array = new CharSequence[size + 1];
		array[0] = getString(R.string.weight);
		for (int i = 0; i < size; i++) {
			array[i + 1] = exercises.get(i).getName();
		}
		return array;
	}

	@Override
	public void onStringSelectClick(int index) {
		select(index);
	}

	@Override
	public int getStringSelectSelected() {
		return selectedIndex;
	}
}
