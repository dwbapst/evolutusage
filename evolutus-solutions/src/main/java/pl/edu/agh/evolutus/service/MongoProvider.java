package pl.edu.agh.evolutus.service;

import java.io.IOException;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version.Main;
import de.flapdoodle.embed.process.config.IRuntimeConfig;

public class MongoProvider implements IStatefulComponent {

	private static final int PORT_EMBEDDED = 65324;
	private static final int PORT = 27017;
	private static final String HOST = "localhost";
	private static final boolean EMBEDDED = true;

	private final int port = EMBEDDED ? PORT_EMBEDDED : PORT;

	@Override
	public void init() throws ComponentException {
		if (EMBEDDED) {
			try {
				IMongodConfig config = new MongodConfigBuilder()
						.version(Main.V3_1)
						.net(new Net(port, false))
						.build();

				IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
						.defaults(Command.MongoD)
						.daemonProcess(true)
						.build();

				MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
				runtime.prepare(config).start();
			} catch (IOException e) {
				throw new ComponentException(e);
			}
		} else {
			try {
				getMongoClient();
			} catch (MongoException e) {
				throw new ComponentException(String.format("Cannot connect to mongodb at %s:%d", HOST, port));
			}
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		return false;
	}

	public MongoClient getMongoClient() {
		MongoClient mongoClient = new MongoClient(HOST, port);
		mongoClient.listDatabaseNames().first(); // test connection
		return mongoClient;
	}
}
