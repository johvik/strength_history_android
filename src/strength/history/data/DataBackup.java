package strength.history.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import strength.history.data.db.ExerciseDBHelper;
import strength.history.data.db.WeightDBHelper;
import strength.history.data.db.WorkoutDBHelper;
import strength.history.data.db.WorkoutDataDBHelper;
import strength.history.ui.BackupActivity;
import android.content.Context;
import android.util.Log;

public class DataBackup implements Runnable {
	private Context context;
	private File dataDirectory;

	public DataBackup(Context context, File dataDirectory) {
		this.context = context;
		this.dataDirectory = dataDirectory;
	}

	@Override
	public void run() {
		boolean ok = backupDBs();
		BackupActivity.onBackupDone(ok);
	}

	private boolean backupDBs() {
		if (dataDirectory != null) {
			File file = getBackupFile(dataDirectory);
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
			if (file.canWrite()) {
				try {
					OutputStream os = new FileOutputStream(file);
					ZipOutputStream zos = new ZipOutputStream(os);
					byte[] buffer = new byte[1024];
					Log.d("DataBackup", "Creating backup: " + file);
					ExerciseDBHelper.getInstance(context).createBackup(context,
							zos, buffer);
					WeightDBHelper.getInstance(context).createBackup(context,
							zos, buffer);
					WorkoutDataDBHelper.getInstance(context).createBackup(
							context, zos, buffer);
					WorkoutDBHelper.getInstance(context).createBackup(context,
							zos, buffer);
					zos.close();
					os.close();
					return true;
				} catch (IOException e) {
				}
			}
		}
		return false;
	}

	public static File getBackupFile(File dataDirectory) {
		return new File(dataDirectory, "test.zip");
	}
}
