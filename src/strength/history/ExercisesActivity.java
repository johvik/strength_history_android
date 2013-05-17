package strength.history;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class ExercisesActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercises, menu);
		return true;
	}

}
