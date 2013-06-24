package strength.history.ui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import strength.history.R;
import strength.history.data.db.ExerciseDBHelper;
import strength.history.ui.custom.CustomTitleActivity;

public class ImportActivity extends CustomTitleActivity {
	private static final int FILE_SELECT_CODE = 0;

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
		showFileChooser();
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_import;
	}

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// TODO Set filter and finish activity
		intent.setType("*/*");
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
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				try {
					InputStream is = getContentResolver().openInputStream(uri);
					ZipInputStream zis = new ZipInputStream(
							new BufferedInputStream(is));
					ZipEntry ze;
					byte[] buffer = new byte[1024];
					Context c = getApplicationContext();
					while ((ze = zis.getNextEntry()) != null) {
						String fileName = ze.getName();
						Log.d("ImportActivity", "Importing: " + fileName);
						if (fileName.equals(ExerciseDBHelper.DATABASE_NAME)) {
							ExerciseDBHelper.getInstance(c).importBackup(c,
									zis, buffer);
						} else {
							// TODO Add rest
						}
					}
					zis.close();
					is.close();
				} catch (IOException e) {
					Toast.makeText(this, getString(R.string.error_file_error),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
