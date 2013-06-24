package strength.history.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import strength.history.R;
import strength.history.ui.custom.CustomTitleActivity;

public class ImportActivity extends CustomTitleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.importt);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_import;
	}
}
