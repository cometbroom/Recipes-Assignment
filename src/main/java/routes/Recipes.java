package routes;

import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;

import jakarta.servlet.http.HttpServletResponse;

public class Recipes implements Route {

	@Override
	public void handle(Request req, HttpServletResponse res) {
		boolean isGet = HttpMethod.GET.is(req.getMethod());
		boolean isPost = HttpMethod.POST.is(req.getMethod());

		if (isGet)
			getController(req, res);
		if (isPost)
			postController(req, res);

	}

	public void getController(Request req, HttpServletResponse res) {
		try {
			db.sql.IEntity recipes = new db.sql.RecipeSQL();

			String id = req.getParameter("id");
			String limit = req.getParameter("limit");
			String offset = req.getParameter("offset");

			String result = id == null
					? recipes.readMany(
							limit == null ? 0 : Integer.parseInt(limit), offset == null ? 0 : Integer.parseInt(offset))
					: recipes.read(id);
			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void postController(Request req, HttpServletResponse res) {
		try {
			db.sql.IEntity recipes = new db.sql.RecipeSQL();

			String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			System.out.println(body);

			HttpResponder.sendResponse(res, body);
		} catch (Exception e) {
			System.out.println(e);
		}
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
