package routes;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;

public class Home implements Route {

	public Home() {
	}

	@Override
	public void handle(Request req, HttpServletResponse res) {
		boolean isGet = HttpMethod.GET.is(req.getMethod());

		if (isGet) {
			getController(req, res);
		}

	}

	public void getController(Request req, HttpServletResponse res) {
		try {
			HttpResponder.sendResponse(res, db.Ingredients.getFood());
		} catch (Exception e) {
		}
	}
}
