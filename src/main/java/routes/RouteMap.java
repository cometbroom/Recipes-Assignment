package routes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

public class RouteMap {
	private Map<String, Route> routesList;

	public RouteMap() {
		routesList = new HashMap<String, Route>();
		routesList.put("/", new Home());
		routesList.put("recipes", new Recipes());
		routesList.put("ingredients", new Ingredients());

	}

	public void handleRequest(String target, Request req, HttpServletResponse res) {

		List<String> paths = getPaths(target);

		Route targetRoute = routesList.get(paths.get(0).isBlank() ? "/" : paths.get(0));
		if (targetRoute == null) {
			sendError(res, 404);
		}
		targetRoute.handle(req, res);
	}

	private List<String> getPaths(String target) {
		try {
			URIBuilder uri = new URIBuilder(target);
			return uri.getPathSegments();
		} catch (URISyntaxException e) {
			System.out.println(e);
			return null;
		}

	}

	private void sendJsonList(HttpServletResponse res, List<NameValuePair> listToSend) {
		Gson g = new Gson();

		String json = g.toJson(listToSend);
		HttpResponder.sendResponse(res, json);

	}

	private void sendError(HttpServletResponse res, int status) {
		try {
			res.sendError(status);
			return;
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
