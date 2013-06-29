package strength.history.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import strength.history.R;
import strength.history.data.DataImport;
import strength.history.ui.custom.CustomTitleActivity;

public class ImportActivity extends CustomTitleActivity {
	private static final int FILE_SELECT_CODE = 0;
	private ProgressBar progressBarImport;
	private static ImportActivity importActivity = null;
	private static boolean importing = false;

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
		progressBarImport = (ProgressBar) findViewById(R.id.progressBarImport);
		importActivity = this;
		if (savedInstanceState == null) {
			showFileChooser();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressBarImport.setVisibility(importing ? View.VISIBLE
				: View.INVISIBLE);
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_import;
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("application/zip");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent,
					getString(R.string.select_import_file)), FILE_SELECT_CODE);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, getString(R.string.error_no_file_manager),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILE_SELECT_CODE) {
			if (resultCode == RESULT_OK) {
				if (!importing) {
					importing = true;
					progressBarImport.setVisibility(View.VISIBLE);
					Uri uri = data.getData();
					new Thread(new DataImport(getApplicationContext(), uri))
							.start();
				}
			} else {
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void onImportDone(boolean ok) {
		importing = false;
		if (importActivity != null) {
			importActivity.dataImported(ok);
		}
	}

	private void dataImported(final boolean ok) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressBarImport.setVisibility(View.INVISIBLE);
				if (ok) {
					Toast.makeText(ImportActivity.this,
							getString(R.string.import_done), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(ImportActivity.this,
							getString(R.string.error_import),
							Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});
	}
}
