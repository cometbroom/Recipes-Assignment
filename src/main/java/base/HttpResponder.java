package base;

public class HttpResponder {
	private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
			"Content-Type: text/html\r\n" +
			"Content-Length: ";
	private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";

	public static String sendMessage(String msg) {
		String output = HTML_CREATE(msg);

		return OUTPUT_HEADERS + output.length() + OUTPUT_END_OF_HEADERS + output;
	}

	private static String HTML_CREATE(String s) {
		return "<html><head><title>Example</title></head><body><p>" + s
				+ "</p></body></html>";
	}
}
