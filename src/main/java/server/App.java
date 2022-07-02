package server;

import java.io.PrintWriter;

import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import routes.RouteMap;

public class App {

	public App() {
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setName("server");

		Server server = new Server(threadPool);
		int port = Integer.parseInt(Config.getProp("port", "3000"));
		setupConnector(server, port);
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

	private static String HTML_CREATE(String s) {
		return "<html><head><title>Example</title></head><body><p>" + s
				+ "</p></body></html>";
	}

}
