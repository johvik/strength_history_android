package strength.history.data.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.SM;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class RequestHandler {
	private enum RequestMethod {
		DELETE, GET, POST, PUT
	};

	/**
	 * Singleton instance
	 */
	private static RequestHandler instance = null;
	private String sessionCookie = null;

	private RequestHandler() {
		HttpURLConnection.setFollowRedirects(false);
	}

	public static RequestHandler getInstance() {
		// Singleton pattern
		if (instance == null) {
			synchronized (RequestHandler.class) {
				if (instance == null) {
					instance = new RequestHandler();
				}
			}
		}
		return instance;
	}

	/**
	 * Retrieves the response from the connection.
	 * 
	 * @param urlConnection
	 * @param read
	 *            True to read the response from the connection
	 * @return
	 * @throws IOException
	 */
	private Response getResponse(HttpURLConnection urlConnection, boolean read)
			throws IOException {
		int responseCode = urlConnection.getResponseCode();
		Log.d("RequestHandler", urlConnection.getURL().toString() + " status: "
				+ responseCode);
		BasicHeader header = new BasicHeader(SM.SET_COOKIE,
				urlConnection.getHeaderField(SM.SET_COOKIE));
		for (HeaderElement e : header.getElements()) {
			// Get the session cookie and save it
			if (e.getName().equals(Config.SESSION_COOKIE)) {
				sessionCookie = e.getValue();
			}
		}

		if (read) {
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			String responseText = IOUtils.toString(in, HTTP.UTF_8);
			return new Response(responseText, responseCode);
		} else {
			return new Response("", responseCode);
		}
	}

	/**
	 * Sets up the request method and the session cookie for the connection.
	 * 
	 * @param urlConnection
	 * @param requestMethod
	 * @throws IOException
	 */
	private void prepare(HttpURLConnection urlConnection,
			RequestMethod requestMethod) throws IOException {
		urlConnection.setRequestMethod(requestMethod.name());
		if (sessionCookie != null) {
			// Set the session cookie
			urlConnection.setRequestProperty(SM.COOKIE, Config.SESSION_COOKIE
					+ "=" + sessionCookie);
		}
	}

	private Response request(RequestMethod requestMethod, String address,
			boolean read) throws IOException {
		URL url = new URL(address);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		try {
			prepare(urlConnection, requestMethod);
			return getResponse(urlConnection, read);
		} finally {
			urlConnection.disconnect();
		}
	}

	private Response requestWithData(RequestMethod requestMethod,
			String address, List<? extends NameValuePair> parameters,
			boolean read) throws IOException {
		byte[] data = URLEncodedUtils.format(parameters, HTTP.UTF_8).getBytes();
		URL url = new URL(address);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		try {
			prepare(urlConnection, requestMethod);
			urlConnection.setRequestProperty(HTTP.CONTENT_TYPE,
					"application/x-www-form-urlencoded");
			urlConnection.setRequestProperty(HTTP.CONTENT_LEN,
					Integer.toString(data.length));
			urlConnection.setDoOutput(true);

			OutputStream out = new BufferedOutputStream(
					urlConnection.getOutputStream());
			out.write(data);
			out.close();
			return getResponse(urlConnection, read);
		} finally {
			urlConnection.disconnect();
		}
	}

	public Response delete(String address) throws IOException {
		return request(RequestMethod.DELETE, address, true);
	}

	public Response deleteNoRead(String address) throws IOException {
		return request(RequestMethod.DELETE, address, false);
	}

	public Response get(String address) throws IOException {
		return request(RequestMethod.GET, address, true);
	}

	public Response getNoRead(String address) throws IOException {
		return request(RequestMethod.GET, address, false);
	}

	public Response post(String address,
			List<? extends NameValuePair> parameters) throws IOException {
		return requestWithData(RequestMethod.POST, address, parameters, true);
	}

	public Response postNoRead(String address,
			List<? extends NameValuePair> parameters) throws IOException {
		return requestWithData(RequestMethod.POST, address, parameters, false);
	}

	public Response put(String address, List<? extends NameValuePair> parameters)
			throws IOException {
		return requestWithData(RequestMethod.PUT, address, parameters, true);
	}

	public Response putNoRead(String address,
			List<? extends NameValuePair> parameters) throws IOException {
		return requestWithData(RequestMethod.PUT, address, parameters, false);
	}
}
