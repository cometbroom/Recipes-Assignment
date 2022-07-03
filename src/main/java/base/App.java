package base;

public class App {

    public static void main(String[] args) {
        server.Config.loadInputs();
        server.Config.setProp("port", "3000", "Server starting port");
        server.Config.setProp("encoding", "utf-8", "Character encoding for out responses.");
        server.Config.setProp("db_name", "recipe_db", null);
        server.Config.setProp("db_user", "hyfuser", null);
        server.Config.setProp("db_pass", "hyfpassword", null);
        db.Seed.run();

        server.App server = new server.App();
    }

}
