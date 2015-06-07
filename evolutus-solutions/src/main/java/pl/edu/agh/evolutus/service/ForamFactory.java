package pl.edu.agh.evolutus.service;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;

import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.DiploidGenotype;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.HaploidGenotype;
import pl.edu.agh.evolutus.service.config.ForamConfig;

public class ForamFactory implements IComponentInstanceProviderAware {

	@Inject
	private ForamConfig config;

	@Inject
	private ShellFactory shellFactory;

	private IComponentInstanceProvider instanceProvider;

	@Override
	public void setInstanceProvider(IComponentInstanceProvider provider) {
		this.instanceProvider = provider;
	}

	private IForam createForam(ForamType foramType) {
		IForam foram = instanceProvider.getInstance(IForam.class);
		foram.setType(foramType);
		foram.setEnergy(config.initialEnergy());
		return foram;
	}

	public IForam createForam(ForamType foramType, Genome genome) {
		assertType(foramType, ForamType.HAPLOID_BENTHIC);
		IForam foram = createForam(foramType);
		foram.setGenotype(new HaploidGenotype(genome, foram.getAddress()));
		foram.setShell(shellFactory.createInitialShell(foram));
		return foram;
	}

	public IForam createForam(ForamType foramType, Genome genomeA, Genome genomeB) {
		assertType(foramType, ForamType.PLANKTONIC, ForamType.DIPLOID_BENTHIC);
		IForam foram = createForam(foramType);
		foram.setGenotype(new DiploidGenotype(genomeA, genomeB, foram.getAddress()));
		foram.setShell(shellFactory.createInitialShell(foram));
		return foram;
	}

	private void assertType(ForamType actual, ForamType... expecteds) {
		for (ForamType expected : expecteds) {
			if (expected == actual) {
				return;
			}
		}
		String msg = String.format("Unexpected foram type: %s. Allowed: %s", actual, StringUtils.join(expecteds, ", "));
		throw new IllegalArgumentException(msg);
	}
}
