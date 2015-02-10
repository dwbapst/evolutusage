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

import pl.edu.agh.evolutus.config.IForamConfigService;
import pl.edu.agh.evolutus.environment.IOceanFragment;
import pl.edu.agh.evolutus.environment.OceanFragment;

public class Foram extends SimpleAgent implements IForam {

	private static final Logger logger = LoggerFactory.getLogger(Foram.class);

	private boolean alive = true;
	private double energy;
	private int chambersCount = 1;

	@Inject
	private IForamConfigService configService;

	private Random random = new Random();

	@Inject
	public Foram(AgentAddressSupplier supplier, IForamConfigService configService) {
		super(supplier);
		this.energy = configService.getForamInitialEnergy();
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
		energy -= configService.getEnergyDemand(chambersCount);

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
		double capacity = configService.getEnergyCapacity(chambersCount);
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

	private boolean canReproduce() {
		double energyNeededToReproduce = configService.getEnergyNeededToReproduce();
		double reproductionProbability = configService.getReproductionProbability();
		return energy > energyNeededToReproduce && random.nextDouble() > reproductionProbability;
	}

	private void reproduce() {
		int childrenCount = random.nextInt(configService.getNewBornLimit());
		double energy = this.energy / childrenCount * 2;
		for (int i = 0; i < childrenCount; i++) {
			IForam foram = instanceProvider.getInstance(IForam.class);
			foram.setEnergy(energy);
			doAction(AgentActions.addToParent(this, foram));
		}
		this.energy = 0; // die
	}

	private boolean canCreateChamber() {
		double energyNeededForGrowth = configService.getEnergyNeededForGrowth();
		int chambersLimit = configService.getChambersLimit();
		double growthProbability = configService.getGrowthProbability();
		return energy > energyNeededForGrowth && chambersCount < chambersLimit && random.nextDouble() > growthProbability;
	}

	private void createChamber() {
		energy -= configService.getChamberGrowthEnergyCost(chambersCount);
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
