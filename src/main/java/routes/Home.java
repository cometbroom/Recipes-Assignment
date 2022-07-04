package routes;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;
import routes.Tools.HttpResponder;

public class Home implements Route {

	@Override
	public void handle(Request req, HttpServletResponse res) {
		boolean isGet = HttpMethod.GET.is(req.getMethod());

		if (isGet) {
			getController(req, res);
		}

	}

	public void getController(Request req, HttpServletResponse res) {
		try {
			HttpResponder.sendResponse(res, "Welcome");
		} catch (Exception e) {
		}
	}

	@Override
	public void postController(Request req, HttpServletResponse res) {
		// TODO Auto-generated method stub

	}

	@Override
	public void putController(Request req, HttpServletResponse res) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteController(Request req, HttpServletResponse res) {
		// TODO Auto-generated method stub

	}
}
