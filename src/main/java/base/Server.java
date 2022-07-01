package base;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void run() throws IOException {
		int port = Integer.parseInt(config.Server.getProp("port", "3000"));

		ServerSocket listener = new ServerSocket(port);
		while (true) {
			System.out.println("Started listening on port: " + port);
			Socket sock = listener.accept();
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

			out.writeUTF(HttpResponder.sendMessage("It works"));
			out.flush();
			out.close();
			sock.close();
		}
	}
}
