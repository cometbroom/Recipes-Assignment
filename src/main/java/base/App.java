package base;

public class App {

    public static void main(String[] args) {
        server.Config.loadInputs();
        server.Config.setProp("port", "3000", "Server starting port");
        server.App server = new server.App();
    }

}
