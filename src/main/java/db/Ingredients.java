package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jooq.JSONFormat;
import org.jooq.SQLDialect;
import org.jooq.JSONFormat.RecordFormat;
import org.jooq.impl.DSL;

import db.sql.IEntity;
import db.sql.IngredientSQL;

public class Ingredients {

	public static String getIngredients() {
		IEntity ings = new IngredientSQL();
		// String result = ings.read("2");
		// HashMap<String, String> list = new HashMap<String, String>();
		// list.put("6", "Brussels Sprouts");
		// list.put("7", "Sesame Seeds");

		// ings.updateMany(list);
		ings.deleteMany(new String[] { "28", "29" });

		String result = ings.readMany(0, 5);
		return result;

	}
}
