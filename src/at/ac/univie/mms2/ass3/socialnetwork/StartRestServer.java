package at.ac.univie.mms2.ass2.socialnetwork;

import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;

public class StartRestServer {

	public static void main(String[] args) throws IOException {
			HttpServer server = HttpServerFactory.create("http://localhost:8081/socialnetwork");
			server.start();
			System.out.println("Social Network Server started ... (hit ENTER to stop)");
			System.in.read();
			server.stop(0);
	}
}
 