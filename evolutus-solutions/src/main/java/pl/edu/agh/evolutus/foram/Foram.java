package pl.edu.agh.evolutus.foram;

import java.util.Collection;
import java.util.Random;

import javax.inject.Inject;

import org.jage.action.AgentActions;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.query.AgentEnvironmentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.environment.IOceanFragment;
import pl.edu.agh.evolutus.environment.OceanFragment;

public class Foram extends SimpleAgent implements IForam {

	private static final Logger logger = LoggerFactory.getLogger(Foram.class);
	public static final double CAPACITY_FACTOR = 1.1;
	public static final double ENERGY_NEED = 0.2;
	public static final double GROWTH_COST_FACTOR = 0.5;
	public static final int GROWTH_MINIMUM = 10;
	public static final int CHAMBERS_LIMIT = 20;
	public static final double GROWTH_PROBABILITY = 0.8;
	public static final int NEW_BORN_LIMIT = 9;
	public static final int INITIAL_ENERGY = 5;
	public static final int REPRODUCTION_MINIMUM = 10;
	public static final double REPRODUCTION_PROBABILITY = 0.8;

	private boolean alive = true;
	private double energy;
	private int chambersCount = 1;

	private Random random = new Random();

	@Inject
	public Foram(AgentAddressSupplier supplier) {
		this(supplier, INITIAL_ENERGY);
	}

	public Foram(AgentAddressSupplier supplier, double energy) {
		super(supplier);
		this.energy = energy;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Override
	public void init() throws ComponentException {
		super.init();
	}

	@Override
	public boolean finish() throws ComponentException {
		logger.debug("\tFORAM finished: {}", getAddress());
		return super.finish();
	}

	private long steps = 0;

	@Override
	public void step() {
		if (!alive) {
			logger.warn("Called step() on dead foram: {}", getAddress());
			return;
		}
		energy -= stepEnergyDemand();

		if (shouldDie()) {
			die();
			return;
		}
		eat();
		if (canReproduce()) {
			reproduce();
		}
		if (canCreateChamber()) {
			createChamber();
		}

		if (steps % 5 == 0) {
			flowWithCurrent();
		}

		steps++;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	private boolean shouldDie() {
		return energy <= 0.0;
	}

	private void die() {
		alive = false;
		doAction(AgentActions.death(this));
	}

	private void eat() {
		double capacity = energyCapacity();
		energy += getOceanFragment().takeAlgae(capacity);
	}

	private AgentAddress lastParentAddress = null;
	private OceanFragment lastParentReference = null;

	private IOceanFragment getOceanFragment() {
		if (lastParentAddress == null || !lastParentAddress.equals(getParentAddress())) {
			AgentEnvironmentQuery<OceanFragment, OceanFragment> query = new AgentEnvironmentQuery<>(OceanFragment.class);
			Collection<OceanFragment> result = queryParentEnvironment(
					query.matching(oceanFragment -> getParentAddress() != null && getParentAddress()
							.equals(oceanFragment.getAddress())));
			lastParentReference = result.iterator().next();
			lastParentAddress = lastParentReference.getAddress();
		}
		return lastParentReference;
	}

	private double energyCapacity() {
		return chambersCount + CAPACITY_FACTOR;
	}

	private double stepEnergyDemand() {
		return (chambersCount + 1) * ENERGY_NEED;
	}

	private boolean canReproduce() {
		return energy > REPRODUCTION_MINIMUM && random.nextDouble() > REPRODUCTION_PROBABILITY;
	}

	private void reproduce() {
		int childrenCount = random.nextInt(NEW_BORN_LIMIT);
		double energy = this.energy / childrenCount * 2;
		for (int i = 0; i < childrenCount; i++) {
			IForam foram = instanceProvider.getInstance(IForam.class);
			foram.setEnergy(energy);
			doAction(AgentActions.addToParent(this, foram));
		}
		this.energy = 0; // die
	}

	private boolean canCreateChamber() {
		return energy > GROWTH_MINIMUM && chambersCount < CHAMBERS_LIMIT && random.nextDouble() > GROWTH_PROBABILITY;
	}

	private void createChamber() {
		energy -= GROWTH_COST_FACTOR * energy;
		chambersCount++;
	}

	private void flowWithCurrent() {
		IOceanFragment oceanFragment = getOceanFragment();
		AgentAddress migrationTarget = oceanFragment.getMigrationTarget();
		if (migrationTarget != null) {
			doAction(AgentActions.migrate(this, migrationTarget));
		}
	}
}
