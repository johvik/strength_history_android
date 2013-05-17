package strength.history.ui;

import strength.history.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ExerciseEditFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exercise_edit,
				container, false);
		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		textView1.setText("Testing...");
		return view;
	}
}
