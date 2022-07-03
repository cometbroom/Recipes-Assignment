package db.sql;

import java.util.HashMap;

public interface IEntity {

	public String create(String name);

	public String createMany(String[] names);

	public String read(String id);

	public String readMany(int limit, int offset);

	public String update(String id, String name);

	public String updateMany(HashMap<String, String> idNameMap);

	public String delete(String id);

	public String deleteMany(String[] ids);

}
