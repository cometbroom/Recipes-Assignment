package db.sql;

import java.util.HashMap;

public interface IEntity {

	public void create(String name);

	public void createMany(String[] names);

	public String read(String id);

	public String readMany(int limit, int offset);

	public void update(String id, String name);

	public void updateMany(HashMap<String, String> idNameMap);

	public void delete(String id);

	public void deleteMany(String[] ids);

}
