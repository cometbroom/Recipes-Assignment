package routes;

import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;

public interface Route {
	public void handle(Request req, HttpServletResponse res);

	public void getController(Request req, HttpServletResponse res);

	public void postController(Request req, HttpServletResponse res);

	public void putController(Request req, HttpServletResponse res);

	public void deleteController(Request req, HttpServletResponse res);

}
