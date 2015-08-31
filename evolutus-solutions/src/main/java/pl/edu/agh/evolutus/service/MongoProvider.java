package pl.edu.agh.evolutus.service;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class MongoProvider implements IStatefulComponent {

	private static String HOST = "localhost";
	private static int PORT = 27017;

	public static void initializeParametersStatically(String host, String port) {
		if (host != null) {
			HOST = host;
		}
		if (port != null) {
			PORT = Integer.valueOf(port);
		}
	}

	public MongoProvider() {
		try {
			getMongoClient();
		} catch (MongoException e) {
			throw new ComponentException(String.format("Cannot connect to mongodb at %s:%d", HOST, PORT));
		}
	}

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public boolean finish() throws ComponentException {
		return false;
	}

	public MongoClient getMongoClient() {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		mongoClient.listDatabaseNames().first(); // test connection
		return mongoClient;
	}
}
