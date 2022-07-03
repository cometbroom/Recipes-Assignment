package db;

import db.sql.IEntity;
import db.sql.IngredientSQL;
import db.sql.RecipeSQL;

public class Tables {

	public static String getIngredients() {
		IEntity recp = new RecipeSQL();
		// String result = ings.read("2");
		// HashMap<String, String> list = new HashMap<String, String>();
		// list.put("6", "Brussels Sprouts");
		// list.put("7", "Sesame Seeds");

		// ings.updateMany(list);
		// ings.deleteMany(new String[] { "28", "29" });

		String result = recp.readMany(0, 5);
		return result;

	}
}
