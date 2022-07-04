package routes.Tools;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import java.util.stream.Collectors;

public class HttpResponder {
	private static String JSON_TYPE = "application/JSON";
	private static String PLAIN_TXT_TYPE = "text/plain";

	public static String parseBody(Request req) {
		try {
			return req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		} catch (IOException e) {
			System.out.println("Parsing request body failed: " + e);
			return "";
		}
	}

	public static void sendResponse(HttpServletResponse res, String body) {
		sendResponse(res, body, 200, JSON_TYPE);
	}

	public static void sendResponse(HttpServletResponse res, String body, int status) {
		sendResponse(res, body, status, JSON_TYPE);
	}

	public static void sendResponse(HttpServletResponse res, String body, int status, String contentType) {
		res.setCharacterEncoding(server.Config.getProp("encoding", "utf-8"));
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
