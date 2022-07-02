package routes;

import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;

public interface Route {
	public void handle(Request req, HttpServletResponse res);
}
