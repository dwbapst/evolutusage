package pl.edu.agh.evolutus.statistics.dao;

import pl.edu.agh.evolutus.statistics.model.Simulation;

public class SimulationDao extends Dao<Simulation> {

	@Override
	protected Class<Simulation> getReturnType() {
		return Simulation.class;
	}
}

