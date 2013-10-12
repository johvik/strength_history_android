package strength.history.data.api;

public class Response {
	public final String text;
	public final int code;

	public Response(String text, int code) {
		this.text = text;
		this.code = code;
	}

	@Override
	public String toString() {
		return text + "\nStatus: " + code;
	}
}
