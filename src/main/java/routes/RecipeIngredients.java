package routes;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;
import routes.Tools.HttpResponder;

public class RecipeIngredients implements Route {
	db.sql.RecipeIngredientsSQL _recipeIngredientsSQL = new db.sql.RecipeIngredientsSQL("recipe_ingredients", "recipe_id",
			"ingredient_id");

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

			// Read many or one according to parameters
			String result = id == null
					? _recipeIngredientsSQL.readMany(
							limit == null ? 0 : Integer.parseInt(limit), offset == null ? 0 : Integer.parseInt(offset))
					: _recipeIngredientsSQL.read(id);
			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void postController(Request req, HttpServletResponse res) {
		try {
			Gson g = new Gson();

			String body = HttpResponder.parseBody(req);
			routes.DTOs.RecipeIngredients recipeIngredients = g.fromJson(body, routes.DTOs.RecipeIngredients.class);
			String result = _recipeIngredientsSQL.create(recipeIngredients.recipe_name, recipeIngredients.ingredients);

			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void putController(Request req, HttpServletResponse res) {
		try {
			boolean replace = Boolean.parseBoolean(req.getParameter("replace"));
			Gson g = new Gson();
			String body = HttpResponder.parseBody(req);
			routes.DTOs.RecipeIngredients recipes = g.fromJson(body, routes.DTOs.RecipeIngredients.class);
			String result = _recipeIngredientsSQL.update(recipes.id, recipes.ingredients, replace);

			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void deleteController(Request req, HttpServletResponse res) {
		try {
			String id = req.getParameter("id");
			if (id == null)
				return;
			String result = _recipeIngredientsSQL.delete(id);
			HttpResponder.sendResponse(res, result);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
