package strength.history.ui.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import strength.history.R;
import strength.history.data.DataListener;
import strength.history.data.DataProvider;
import strength.history.data.provider.ExerciseProvider;
import strength.history.data.structure.Exercise;
import strength.history.data.structure.Workout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ProgressBar;

public class CreateDefaultDialog extends DialogFragment implements
		ExerciseProvider.Events.Defaults {
	private DataProvider dataProvider;
	private Context applicationContext;

	@Override
	public void onResume() {
		super.onResume();
		dataProvider = DataListener.add(this);
		applicationContext = getActivity().getApplicationContext();
		dataProvider.createDefaults(null, applicationContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataListener.remove(this);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Activity a = getActivity();
		// TODO This looks weird on older phones
		ProgressBar p = new ProgressBar(a);
		setCancelable(false);
		return new AlertDialog.Builder(a).setCancelable(false)
				.setTitle(getString(R.string.generating_defaults)).setView(p)
				.create();
	}

	@Override
	public void exerciseCreateDefaultsCallback(Collection<Exercise> e) {
		ArrayList<String> search = new ArrayList<String>();
		String squat = "Squat";
		search.add(squat);
		String overheadPress = "Overhead Press";
		search.add(overheadPress);
		String deadLift = "Dead Lift";
		search.add(deadLift);
		String benchPress = "Bench Press";
		search.add(benchPress);
		String bentOverRow = "Bent-Over Row";
		search.add(bentOverRow);

		HashMap<String, Exercise> res = new HashMap<String, Exercise>();
		for (Exercise ex : e) {
			String name = ex.getName();
			for (int i = 0, j = search.size(); i < j; i++) {
				String si = search.get(i);
				if (si.equals(name)) {
					search.remove(i);
					j--;
					res.put(si, ex);
				}
			}
		}

		Exercise eSquat = res.get(squat);
		Exercise eOverheadPress = res.get(overheadPress);
		Exercise eDeadLift = res.get(deadLift);
		Exercise eBenchPress = res.get(benchPress);
		Exercise eBentOverRow = res.get(bentOverRow);

		try {
			Workout a = new Workout("StrongLifts 5x5 A");
			a.add(eSquat.getId());
			a.add(eBenchPress.getId());
			a.add(eBentOverRow.getId());
			Workout b = new Workout("StrongLifts 5x5 B");
			b.add(eSquat.getId());
			b.add(eOverheadPress.getId());
			b.add(eDeadLift.getId());
			dataProvider.insert(a, applicationContext);
			dataProvider.insert(b, applicationContext);
		} catch (NullPointerException ex) {
			Log.e("CreateDefaultDialog", ex.getLocalizedMessage());
		}
		dismiss();
	}
}
