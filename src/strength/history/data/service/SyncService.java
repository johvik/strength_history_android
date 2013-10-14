package strength.history.data.service;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import strength.history.data.api.Config;
import strength.history.data.api.RequestHandler;
import strength.history.data.api.Response;
import strength.history.data.api.User;
import strength.history.data.structure.Weight;
import strength.history.ui.SettingsActivity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;

public class SyncService extends IntentService {
	public enum Request {
		SYNC;

		private static final Request[] REQUEST_VALUES = Request.values();

		/**
		 * Converts a number to a enum
		 * 
		 * @param i
		 *            Index in the enum
		 * @return The enum at index i % length
		 */
		public static Request parse(int i) {
			return REQUEST_VALUES[i % REQUEST_VALUES.length];
		}
	}

	/**
	 * Name of the messenger passed with the intent
	 */
	public static final String MESSENGER = "MESSENGER";
	/**
	 * Name of the request passed with the intent
	 */
	public static final String REQUEST = "REQUEST";

	/**
	 * Constructor
	 */
	public SyncService() {
		super("SyncService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("SyncService", "onHandleIntent");
		int id = intent.getIntExtra(REQUEST, -1);
		if (id != -1) {
			Request request = Request.parse(id);
			Log.d("SyncService", "request=" + request);
			Messenger messenger = intent.getParcelableExtra(MESSENGER);
			if (messenger != null) {
				switch (request) {
				case SYNC:
					// TODO Implement sync service
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(this);
					String email = sharedPreferences.getString(
							SettingsActivity.PREF_EMAIL_KEY, "").trim();
					String password = sharedPreferences.getString(
							SettingsActivity.PREF_PASSWORD_KEY, "");

					if (!email
							.matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
							|| password.length() < 7) {
						// TODO How to handle errors here?
						Log.d("SyncService", "Bad user data");
					} else {
						User u = new User(email, password);
						try {
							u.login();
							//u.logout();
							Response r = RequestHandler.getInstance().get(
									Config.SERVER_ADDRESS + "/weight");
							try {
								JSONArray array = new JSONArray(r.text);
								for (int i = 0, j = array.length(); i < j; i++) {
									Weight w = Weight.fromJSON(array
											.getJSONObject(i));
									Log.d("Test", w.toString());
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}
	}
}
