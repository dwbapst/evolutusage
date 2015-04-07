package pl.edu.agh.evolutus.foram;

import java.util.Collection;
import java.util.List;
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

import pl.edu.agh.evolutus.config.ConfigFactory;
import pl.edu.agh.evolutus.config.ForamConfig;
import pl.edu.agh.evolutus.environment.IOceanFragment;
import pl.edu.agh.evolutus.environment.OceanFragment;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.Genotype;

public class Foram extends SimpleAgent implements IForam {

	private static final Logger logger = LoggerFactory.getLogger(Foram.class);

	private boolean alive = true;
	private double energy;
	private int chambersCount = 1;
	private int age = 0;

	private Genotype genotype = null;

	private ForamConfig config;

	private Random random = new Random();

	@Inject
	public Foram(AgentAddressSupplier supplier, ConfigFactory configFactory) {
		super(supplier);
		this.config = configFactory.getForamConfig();
		this.energy = config.initialEnergy();
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Override
	public double getEnergy() {
		return energy;
	}

	@Override
	public void setGenotype(Genotype genotype) {
		if (this.genotype == null) {
			this.genotype = genotype;
		} else {
			throw new IllegalStateException("setGenotype() called multiple times on foram: " + getAddress());
		}
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

	@Override
	public void step() {
		if (!couldForamPerformStep()) {
			return;
		}

		energy -= config.energyDemand(chambersCount);

		try {
			if (shouldDie()) {
				die();
			}
			eat();
			if (canReproduce()) {
				reproduce();
			}
			if (canCreateChamber()) {
				createChamber();
			}

			tryMigrate();

			age++;
		} catch (AgentDiedException e) {
			logger.debug("Foram died: {}", getAddress());
		}
	}

	private boolean couldForamPerformStep() {
		if (genotype == null) {
			logger.warn("Called step() on foram with uninitialized genotype: {}", getAddress());
			return false;
		}
		if (!alive) {
			logger.warn("Called step() on dead foram: {}", getAddress());
			return false;
		}
		return true;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	private boolean shouldDie() {
		return energy <= genotype.getMinEnergyGene().getValue();
	}

	private void die() throws AgentDiedException {
		energy = 0.0;
		alive = false;
		// TODO: persist genotype in virtual fossilization service
		doAction(AgentActions.death(this));
		throw new AgentDiedException();
	}

	private void eat() {
		double capacity = config.energyCapacity(chambersCount);
		energy += getOceanFragment().takeAlgae(capacity);
	}

	private AgentAddress lastParentAddress = null;
	private OceanFragment lastParentReference = null;

	private double getCurrentStrength() {
		return getOceanFragment().getOceanFragmentProperties().getCurrentDirection().getStrength();
	}

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
		boolean oldEnough = age >= genotype.getMinAdultAgeGene().getValue();
		boolean energyEnough = energy > config.energyNeededToReproduce();
		boolean reproductionProbable = random.nextDouble() > config.reproductionProbability();
		return oldEnough && energyEnough && reproductionProbable;
	}

	private void reproduce() throws AgentDiedException {
		int gametesProduction = config.gametesProduction(chambersCount);
		List<Genome> gametes = genotype.createGametes(gametesProduction, config.gametesSievingCoefficient());
		getOceanFragment().addGametes(gametes);
		die();
	}

	private boolean canCreateChamber() {
		double energyNeededForGrowth = config.energyNeededForGrowth();
		int chambersLimit = config.chambersLimit();
		double growthProbability = config.growthProbability();
		return energy > energyNeededForGrowth && chambersCount < chambersLimit && random.nextDouble() > growthProbability;
	}

	private void createChamber() {
		energy -= config.chamberGrowthEnergyCost(chambersCount);
		chambersCount++;
	}

	private double migrationProbability = 0.0;

	private void tryMigrate() {
		migrationProbability += 0.01;
		if (random.nextDouble() < migrationProbability + getCurrentStrength()) {
			migrationProbability = 0.0;
			flowWithCurrent();
		}
	}

	private void flowWithCurrent() {
		IOceanFragment oceanFragment = getOceanFragment();
		AgentAddress migrationTarget = oceanFragment.getMigrationTarget();
		if (migrationTarget != null && !migrationTarget.equals(lastParentAddress)) {
			doAction(AgentActions.migrate(this, migrationTarget));
		}
	}

	private static class AgentDiedException extends Exception {

	}
}
