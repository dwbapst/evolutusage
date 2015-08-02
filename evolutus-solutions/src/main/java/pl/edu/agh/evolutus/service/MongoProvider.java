package pl.edu.agh.evolutus.service;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.Version.Main;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import pl.edu.agh.evolutus.service.config.SystemConfig;

import javax.inject.Inject;
import java.io.IOException;

public class MongoProvider implements IStatefulComponent {

	private final String host;
	private final int port;
	private final boolean inMemory;
	private final String dbpath;

	@Inject
	public MongoProvider(SystemConfig systemConfig) {
		this.inMemory = systemConfig.isDatabaseInMemory();
		this.host = inMemory ? "localhost" : systemConfig.getDatabaseHost();
		this.port = systemConfig.getDatabasePort();
		this.dbpath = systemConfig.getDatabasePath();
	}

	@Override
	public void init() throws ComponentException {
		if (inMemory) {
			try {
				MongodConfigBuilder configBuilder = new MongodConfigBuilder()
						.version(Main.V3_1)
						.net(new Net(port, false));

				if (!"undefined".equals(this.dbpath)) {
					configBuilder.replication(new Storage(this.dbpath, null, 0));
				}

				IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
						.defaults(Command.MongoD)
						.daemonProcess(true)
						.build();

				MongodStarter runtime = MongodStarter.getInstance(runtimeConfig);
				runtime.prepare(configBuilder.build()).start();
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
