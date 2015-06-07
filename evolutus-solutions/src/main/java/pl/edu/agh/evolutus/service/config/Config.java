package pl.edu.agh.evolutus.service.config;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;

import pl.edu.agh.evolutus.service.config.utils.UnitsConverter;

abstract class Config implements IStatefulComponent {

	@Inject
	private ConfigFactory configFactory;

	@Inject
	protected UnitsConverter unitsConverter;

	protected IConfigJS configJS;

	@Override
	public void init() throws ComponentException {
		this.configJS = configFactory.getConfigJS();
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}
}
