package routes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;

public class RouteMap {
	private Map<String, Route> routesList;

	public RouteMap() {
		routesList = new HashMap<String, Route>();
		routesList.put("/", new Home());
	}

	public void handleRequest(String target, Request req, HttpServletResponse res) {
		routesList.get("/").handle(req, res);
	}


}
