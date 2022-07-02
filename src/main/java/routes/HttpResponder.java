package routes;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class HttpResponder {
	private static String JSON_TYPE = "application/JSON";
	private static String PLAIN_TXT_TYPE = "text/plain";
	private static String CHAR_ENCODING = "utf-8";

	public static void sendResponse(HttpServletResponse res, String body) {
		sendResponse(res, body, 200, JSON_TYPE);
	}

	public static void sendResponse(HttpServletResponse res, String body, int status) {
		sendResponse(res, body, status, JSON_TYPE);
	}

	public static void sendResponse(HttpServletResponse res, String body, int status, String contentType) {
		res.setCharacterEncoding(CHAR_ENCODING);
		res.setStatus(status);
		res.setContentType(contentType);
		try {
			res.getWriter().print(body);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static String HTML_CREATE(String s) {
		return "<html><head><title>Example</title></head><body><p>" + s
				+ "</p></body></html>";
	}
}
