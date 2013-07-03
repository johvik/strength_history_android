package strength.history.ui;

import java.io.File;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import strength.history.R;
import strength.history.data.DataBackup;
import strength.history.ui.custom.CustomTitleActivity;

public class BackupActivity extends CustomTitleActivity {
	private static final int FILE_SEND_CODE = 0;
	private static Object sync = new Object();
	private static Thread thread = null;
	private static boolean userQuit = false;
	private ProgressBar progressBarBackup;
	private static BackupActivity backupActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.backup);
		setCustomBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userQuit = true;
				finish();
			}
		});
		progressBarBackup = (ProgressBar) findViewById(R.id.progressBarBackup);
		backupActivity = this;
		userQuit = false;
		if (savedInstanceState == null) {
			synchronized (sync) {
				if (thread == null) {
					thread = new Thread(new DataBackup(getApplicationContext(),
							getBackupDirectory()));
					thread.start();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		progressBarBackup.setVisibility(thread != null ? View.VISIBLE
				: View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		userQuit = true;
	}

	@Override
	protected int getLayoutResID() {
		return R.layout.activity_backup;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILE_SEND_CODE) {
			progressBarBackup.setVisibility(View.INVISIBLE);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void onBackupDone(boolean ok) {
		synchronized (sync) {
			thread = null;
		}
		if (backupActivity != null) {
			backupActivity.backupCreated(ok);
		}
	}

	private void sendFile(File file) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		String email = sharedPreferences.getString(
				SettingsActivity.PREF_BACKUP_EMAIL_KEY, "");
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("application/zip");
		if (email.length() > 0) {
			i.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
		}
		i.putExtra(
				Intent.EXTRA_SUBJECT,
				getString(R.string.backup_subject, getString(R.string.app_name)));
		i.putExtra(
				Intent.EXTRA_TEXT,
				getString(R.string.backup_text,
						DateFormat.getMediumDateFormat(this).format(new Date())));
		i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		startActivityForResult(
				Intent.createChooser(i, getString(R.string.backup_save_via)),
				FILE_SEND_CODE);
	}

	private File getBackupDirectory() {
		return getExternalCacheDir();
	}

	private void backupCreated(boolean ok) {
		if (!userQuit) {
			File file = DataBackup.getBackupFile(getBackupDirectory());
			if (ok && file != null) {
				sendFile(file);
			} else {
				if (file != null) {
					file.delete();
				}
				Toast.makeText(BackupActivity.this,
						getString(R.string.error_backup), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
}
