package strength.history.data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import strength.history.data.db.ExerciseDBHelper;
import strength.history.data.db.WeightDBHelper;
import strength.history.data.db.WorkoutDBHelper;
import strength.history.data.db.WorkoutDataDBHelper;
import strength.history.ui.ImportActivity;
import android.content.Context;
import android.net.Uri;

public class DataImport implements Runnable {
	private Context context;
	private Uri uri;

	public DataImport(Context context, Uri uri) {
		this.context = context;
		this.uri = uri;
	}

	@Override
	public void run() {
		boolean ok = importDBs();
		ImportActivity.onImportDone(ok);
	}

	private boolean importDBs() {
		try {
			InputStream is = context.getContentResolver().openInputStream(uri);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((ze = zis.getNextEntry()) != null) {
				String fileName = ze.getName();
				if (fileName.equals(ExerciseDBHelper.DATABASE_NAME)) {
					ExerciseDBHelper.getInstance(context).importBackup(context,
							zis, buffer);
					count++;
				} else if (fileName.equals(WeightDBHelper.DATABASE_NAME)) {
					WeightDBHelper.getInstance(context).importBackup(context,
							zis, buffer);
					count++;
				} else if (fileName.equals(WorkoutDataDBHelper.DATABASE_NAME)) {
					WorkoutDataDBHelper.getInstance(context).importBackup(
							context, zis, buffer);
					count++;
				} else if (fileName.equals(WorkoutDBHelper.DATABASE_NAME)) {
					WorkoutDBHelper.getInstance(context).importBackup(context,
							zis, buffer);
					count++;
				}
			}
			zis.close();
			is.close();
			// TODO Clear loaded data!
			return count > 0;
		} catch (IOException e) {
		}
		return false;
	}
}
