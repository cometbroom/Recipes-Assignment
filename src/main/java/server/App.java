package server;

import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import routes.RouteMap;

public class App {

	public App() {
		//Prepare reusable threads for our server app
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setName("server");

		Server server = new Server(threadPool);
		int port = Integer.parseInt(Config.getProp("port", "3000"));
		//Setup out connectors for http 1 and 2 compatibility.
		setupConnector(server, port);

		//Prepare our routes object to run it in the handler
		RouteMap routes = new RouteMap();
		server.setHandler(new AbstractHandler() {
			@Override
			public void handle(String target, Request jettyRequest, HttpServletRequest request,
					HttpServletResponse response) {
				jettyRequest.setHandled(true);
				routes.handleRequest(target, jettyRequest, response);
			}
		});
		try {
			System.out.println("Started listening on port: " + port);
			server.start();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void setupConnector(Server server, int port) {
		//1 acceptor for socket 1 select to pass the request up the chain
		int acceptors = 1, selectors = 1;

		HttpConfiguration httpConfig = new HttpConfiguration();

		HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);
		HTTP2CServerConnectionFactory http2 = new HTTP2CServerConnectionFactory(httpConfig);

		ServerConnector connector = new ServerConnector(server, acceptors, selectors, http11, http2);

		connector.setPort(port);
		connector.setHost("127.0.0.1");
		connector.setAcceptQueueSize(128);
		server.addConnector(connector);
	}


}
