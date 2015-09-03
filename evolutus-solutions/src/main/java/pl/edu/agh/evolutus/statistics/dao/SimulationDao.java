package pl.edu.agh.evolutus.statistics.dao;

import java.util.List;
import java.util.Optional;

import pl.edu.agh.evolutus.statistics.model.Simulation;

public class SimulationDao extends Dao<Simulation> {

	@Override
	protected Class<Simulation> getReturnType() {
		return Simulation.class;
	}

	public List<Simulation> get() {
		return createQuery().asList();
	}

	public Optional<Simulation> getLatest() {
		return optionalResult(
				createQuery()
						.order("-simulationStart")
						.limit(1)
		);
	}

	public Optional<Simulation> get(long simulationStart) {
		return optionalResult(
				createQuery().field("simulationStart").equal(simulationStart)
		);
	}
}

