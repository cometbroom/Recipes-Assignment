package routes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;

public class Recipes implements Route {

	db.sql.TwoColumnSQL _recipeSQL = new db.sql.TwoColumnSQL("recipes", "id", "recipe_name");

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

			String result = id == null
					? _recipeSQL.readMany(
							limit == null ? 0 : Integer.parseInt(limit), offset == null ? 0 : Integer.parseInt(offset))
					: _recipeSQL.read(id);
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
				HttpResponder.sendResponse(res, _recipeSQL.create(name));
				return;
			}

			Gson g = new Gson();
			String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			routes.DTOs.Recipe[] recipes = g.fromJson(body, routes.DTOs.Recipe[].class);
			List<String> recipesToCreate = new ArrayList<String>();

			for (routes.DTOs.Recipe recipe : recipes) {
				recipesToCreate.add(recipe.recipe_name);
			}
			String result = _recipeSQL.createMany(recipesToCreate);

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

			if (id != null && name != null) {
				result = _recipeSQL.update(id, name);
			} else {
				Gson g = new Gson();
				String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				routes.DTOs.Recipe[] recipes = g.fromJson(body, routes.DTOs.Recipe[].class);
				HashMap<String, String> recipesToUpdate = new HashMap<String, String>();

				for (routes.DTOs.Recipe recipe : recipes) {
					recipesToUpdate.put(recipe.id, recipe.recipe_name);
				}
				result = _recipeSQL.updateMany(recipesToUpdate);
			}
			HttpResponder.sendResponse(res, result);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void deleteController(Request req, HttpServletResponse res) {
		try {
			Gson g = new Gson();

			String id = req.getParameter("id");
			String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			if (id != null)
				_recipeSQL.delete(id);
			else
				_recipeSQL.deleteMany(g.fromJson(body, String[].class));
			HttpResponder.sendResponse(res, "");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}