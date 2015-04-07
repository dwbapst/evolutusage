package pl.edu.agh.evolutus.service;

import java.io.IOException;

import javax.inject.Inject;

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
import pl.edu.agh.evolutus.config.ConfigFactory;
import pl.edu.agh.evolutus.config.SystemConfig;

public class MongoProvider implements IStatefulComponent {

	private final String host;
	private final int port;
	private final boolean inMemory;

	@Inject
	public MongoProvider(ConfigFactory configFactory) {
		SystemConfig systemConfig = configFactory.getSystemConfig();
		this.inMemory = systemConfig.isDatabaseInMemory();
		this.host = inMemory ? "localhost" : systemConfig.getDatabaseHost();
		this.port = systemConfig.getDatabasePort();
	}

	@Override
	public void init() throws ComponentException {
		if (inMemory) {
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
				throw new ComponentException(String.format("Cannot connect to mongodb at %s:%d", host, port));
			}
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		return false;
	}

	public MongoClient getMongoClient() {
		MongoClient mongoClient = new MongoClient(host, port);
		mongoClient.listDatabaseNames().first(); // test connection
		return mongoClient;
	}
}
