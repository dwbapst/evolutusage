package pl.edu.agh.evolutus.foram;

import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAgent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.evolutus.environment.IOceanFragment;
import pl.edu.agh.evolutus.strategy.IEnergyConsumptionStrategy;
import pl.edu.agh.evolutus.strategy.IFeedingStrategy;

import javax.inject.Inject;
import java.util.Random;

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

	private static int instancesCounter = 0;

	private synchronized static int newInstance() {
		return instancesCounter++;
	}

	private final int instanceIndex;

	private boolean alive = true;
	private double energy = 0.0;
	private int chambersCount = 1;

	private IFeedingStrategy feedingStrategy;
	private IEnergyConsumptionStrategy energyConsumptionStrategy;
	private IOceanFragment oceanFragment;

	private Random random = new Random();

	@Inject
	public Foram(AgentAddressSupplier supplier) {
		this(supplier, INITIAL_ENERGY);
	}

	public Foram(AgentAddressSupplier supplier, double energy) {
		super(supplier);
		this.energy = energy;
		this.instanceIndex = newInstance();
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Inject
	@Override
	public void setFeedingStrategy(IFeedingStrategy feedingStrategy) {
		this.feedingStrategy = feedingStrategy;
	}

	@Inject
	@Override
	public void setEnergyConsumptionStrategy(IEnergyConsumptionStrategy energyConsumptionStrategy) {
		this.energyConsumptionStrategy = energyConsumptionStrategy;
	}

	@Override
	public void setOceanFragment(IOceanFragment oceanFragment) {
		this.oceanFragment = oceanFragment;
	}

	@Override
	public boolean finish() throws ComponentException {
		logger.debug("\tFORAM finished: {}", getAddress());
		return super.finish();
	}

	private int step = 0;

	@Override
	public void step() {
		if (instanceIndex == 1 || instanceIndex == instancesCounter - 1) {
			step++;
			if (step % 1000 == 0) {
				logger.info("\tFORAM step #{}: {} from {}", step, getAddress(), Thread.currentThread().getName());
			}
		}

		if (!alive) {
			logger.warn("Called step() on dead foram: {}", getAddress());
			return;
		}
		//		dieIfShould(); FIXME

		if (eat() <= 0) {
			move();
		}
		if (canReproduce()) {
			reproduce();
		}
		if (canCreateChamber()) {
			createChamber();
		}
	}

	private boolean canReproduce() {
		return energy > REPRODUCTION_MINIMUM && random.nextDouble() > REPRODUCTION_PROBABILITY;
	}

	private void reproduce() {
		int childrenCount = random.nextInt(NEW_BORN_LIMIT);
		double energy = this.energy / childrenCount * 2;
		for (int i = 0; i < childrenCount; i++) {
			oceanFragment.createForam(energy);
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

	private void dieIfShould() {
		if (energy <= 0.0) {
			alive = false;
			oceanFragment.removeForam(this);
		}
	}

	private double eat() {
		double initialEnergy = energy;
		double capacity = energyCapacity();
		energy += takeAlgae(capacity) - energyDemand();
		return energy - initialEnergy;
	}

	private double takeAlgae(double capacity) {
		int demand = (int) capacity;
		return oceanFragment.takeAlgae(demand);
	}

	private double energyCapacity() {
		return chambersCount + CAPACITY_FACTOR;
	}

	private double energyDemand() {
		return (chambersCount + 1) * ENERGY_NEED;
	}

	private void move() {
		// TODO: implement
	}
}
