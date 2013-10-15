package strength.history.data.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

public class User {
	private String email;
	private String password;
	private boolean authenticated = false;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public void login() throws IOException {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("email", email));
		parameters.add(new BasicNameValuePair("password", password));

		Response response = RequestHandler.getInstance().postNoRead(
				Config.SERVER_ADDRESS + "/login", parameters);

		if (response.code == HttpURLConnection.HTTP_OK) {
			authenticated = true;
		} else {
			throw new IOException("Failed to login");
		}
	}

	public void logout() throws IOException {
		Response response = RequestHandler.getInstance().getNoRead(
				Config.SERVER_ADDRESS + "/logout?no_redirect");

		if (response.code == HttpURLConnection.HTTP_OK) {
			authenticated = false;
		} else {
			throw new IOException("Failed to logout");
		}
	}

	public boolean isAuthenticated() {
		return authenticated;
	}
}
