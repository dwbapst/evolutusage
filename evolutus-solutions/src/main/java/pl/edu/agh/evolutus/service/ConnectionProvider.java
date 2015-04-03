package pl.edu.agh.evolutus.service;

import static pl.edu.agh.evolutus.database.tables.Stats.*;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.SQLDataType;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionProvider implements IStatefulComponent {

	private ComboPooledDataSource cpds;

	@Override
	public void init() throws ComponentException {
		try {
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass("org.h2.Driver");
			//			cpds.setJdbcUrl("jdbc:h2:tcp://localhost/~/programowanie/msc/db/evolutus");
			cpds.setJdbcUrl("jdbc:h2:mem:evolutus");
			cpds.setUser("evolutus");
			cpds.setPassword("evolutus");

			initDatabase();
		} catch (PropertyVetoException | SQLException e) {
			throw new ComponentException(e);
		}
	}

	private void initDatabase() throws SQLException {
		try (Connection connection = getConnection()) {
			Configuration config = new DefaultConfiguration().set(connection).set(SQLDialect.H2);
			boolean doesNotContainsStatsTable = DSL.using(config)
					.meta()
					.getTables()
					.stream()
					.filter(table -> table.getName().equals(STATS.getName()))
					.collect(Collectors.toList())
					.isEmpty();
			if (doesNotContainsStatsTable) {
				DSLContext create = new DefaultDSLContext(connection, SQLDialect.H2);
				create.createTable(STATS)
						.column(STATS.ID, SQLDataType.INTEGER)
						.column(STATS.SIMULATION_START, SQLDataType.TIMESTAMP)
						.column(STATS.STEP_NO, SQLDataType.BIGINT)
						.column(STATS.X, SQLDataType.BIGINT)
						.column(STATS.Y, SQLDataType.BIGINT)
						.column(STATS.Z, SQLDataType.BIGINT)
						.column(STATS.FORAMS_COUNT, SQLDataType.INTEGER)
						.column(STATS.ALGAE_AVAILABILITY, SQLDataType.DOUBLE)
						.column(STATS.TOTAL_ENERGY, SQLDataType.DOUBLE)
						.column(STATS.INSOLATION, SQLDataType.DOUBLE)
						.execute();
			}
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}

	public Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}
}
