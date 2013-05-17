package strength.history.ui;

import java.util.Comparator;

import strength.history.R;
import strength.history.data.SortedList;
import strength.history.data.structure.Exercise;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ExerciseListFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exercise_list,
				container, false);
		ListView listViewExercises = (ListView) view
				.findViewById(R.id.listViewExercises);
		final SortedList<Exercise> list = new SortedList<Exercise>(
				new Comparator<Exercise>() {
					@Override
					public int compare(Exercise lhs, Exercise rhs) {
						return lhs.compareTo(rhs);
					}
				});
		final ExerciseAdapter adapter = new ExerciseAdapter(getActivity(), list);
		listViewExercises.setAdapter(adapter);
		Button buttonExerciseCreate = (Button) view
				.findViewById(R.id.buttonExerciseCreate);
		buttonExerciseCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				list.add(new Exercise("Test"));
				adapter.notifyDataSetChanged();
			}
		});
		return view;
	}
}
