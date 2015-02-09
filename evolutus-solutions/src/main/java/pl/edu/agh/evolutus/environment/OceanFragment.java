package pl.edu.agh.evolutus.environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.ISimpleAgent;
import org.jage.agent.SimpleAggregate;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.supplier.CoordinatesSupplier;

public class OceanFragment extends SimpleAggregate implements IOceanFragment {

	private static final Logger logger = LoggerFactory.getLogger(OceanFragment.class);

	private IEnvironmentInfo environmentInfo;

	private List<IForam> foramsToAdd = new ArrayList<>();

	private List<IForam> foramsToRemove = new ArrayList<>();

	private Random random = new Random();

	@Inject
	public OceanFragment(AgentAddressSupplier supplier, CoordinatesSupplier coordinatesSupplier) {
		super(supplier);
		Coordinates oceanSize = coordinatesSupplier.getSize();
		Coordinates position = coordinatesSupplier.createCoordinates();
		environmentInfo = new EnvironmentInfo(oceanSize, position, 1.0);
	}

	@Override
	public IEnvironmentInfo getIEnvironmentInfo() {
		return environmentInfo;
	}

	@Override
	public void createForam(double energy) {
		//		IForam foram = instanceProvider.getInstance(IForam.class); FIXME
		//		foram.setEnergy(energy);
		//		foram.setOceanFragment(this);
		//		addForam(foram);
	}

	@Override
	public synchronized void addForam(IForam foram) {
		//		foramsToAdd.add(foram); FIXME
	}

	@Override
	public synchronized void removeForam(IForam foram) {
		//		foramsToRemove.add(foram); FIXME
	}

	@Override
	public int takeAlgae(int demand) {
		return random.nextInt(demand / 2);
	}

	@Override
	public void init() throws ComponentException {
		super.init();

		logger.debug("Initialized ocean fragment: {} {}", getAddress(), environmentInfo.getPosition());

		for (ISimpleAgent foram : temporaryAgentsList) {
			((IForam) foram).setOceanFragment(this);
		}
	}

	@Override
	public void step() {
		logger.debug("OCEAN_FRAGMENT step: {} {}", getAddress(), environmentInfo.getPosition());

		super.step();

		synchronized (this) {
			removeAll(foramsToRemove);
			addAll(foramsToAdd);
			foramsToRemove.clear();
			foramsToAdd.clear();
		}
	}

}
