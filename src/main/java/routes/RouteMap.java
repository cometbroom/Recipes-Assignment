package routes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hc.core5.net.URIBuilder;
import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;

public class RouteMap {
	private Map<String, Route> routesList;

	public RouteMap() {
		//Add our routes to the map with their relevant objects which all have handlers
		routesList = new HashMap<String, Route>();
		routesList.put("/", new Home());
		routesList.put("recipes", new Recipes());
		routesList.put("ingredients", new Ingredients());
		routesList.put("recipeingredients", new RecipeIngredients());
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

	private void sendError(HttpServletResponse res, int status) {
		try {
			res.sendError(status);
			return;
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
