package routes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;
import routes.Tools.HttpResponder;

public class Ingredients implements Route {

	db.sql.TwoColumnSQL _ingredientSQL = new db.sql.TwoColumnSQL("ingredients", "id", "ingredient_name");

	@Override
	public void handle(Request req, HttpServletResponse res) {
		boolean isGet = HttpMethod.GET.is(req.getMethod());
		boolean isPost = HttpMethod.POST.is(req.getMethod());
		boolean isPut = HttpMethod.PUT.is(req.getMethod());
		boolean isDelete = HttpMethod.DELETE.is(req.getMethod());

		if (isGet)
			getController(req, res);
		if (isPost)
			postController(req, res);
		if (isPut)
			putController(req, res);
		if (isDelete)
			deleteController(req, res);

	}

	public void getController(Request req, HttpServletResponse res) {
		try {

			String id = req.getParameter("id");
			String limit = req.getParameter("limit");
			String offset = req.getParameter("offset");

			// Read many or one according to parameters.
			String result = id == null
					? _ingredientSQL.readMany(
							limit == null ? 0 : Integer.parseInt(limit), offset == null ? 0 : Integer.parseInt(offset))
					: _ingredientSQL.read(id);
			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void postController(Request req, HttpServletResponse res) {
		try {
			String name = req.getParameter("name");

			if (name != null) {
				HttpResponder.sendResponse(res, _ingredientSQL.create(name));
				return;
			}

			Gson g = new Gson();
			String body = HttpResponder.parseBody(req);
			routes.DTOs.Ingredient[] ingredients = g.fromJson(body, routes.DTOs.Ingredient[].class);
			List<String> ingredientsToCreate = new ArrayList<String>();

			for (routes.DTOs.Ingredient ingredient : ingredients) {
				ingredientsToCreate.add(ingredient.ingredient_name);
			}
			String result = _ingredientSQL.createMany(ingredientsToCreate);

			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void putController(Request req, HttpServletResponse res) {
		try {
			String id = req.getParameter("id");
			String name = req.getParameter("name");
			String result;

			if (id == null || name == null)
				return;

			result = _ingredientSQL.update(id, name);
			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void deleteController(Request req, HttpServletResponse res) {
		try {
			String result;

			String id = req.getParameter("id");

			if (id == null)
				return;
			result = _ingredientSQL.delete(id);
			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
