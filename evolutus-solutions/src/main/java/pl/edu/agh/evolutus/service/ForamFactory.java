package pl.edu.agh.evolutus.service;

import org.apache.commons.lang3.StringUtils;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.platform.component.provider.IComponentInstanceProviderAware;
import pl.edu.agh.evolutus.foram.ForamType;
import pl.edu.agh.evolutus.foram.IForam;
import pl.edu.agh.evolutus.genotype.DiploidGenotype;
import pl.edu.agh.evolutus.genotype.Genome;
import pl.edu.agh.evolutus.genotype.HaploidGenotype;
import pl.edu.agh.evolutus.service.config.ForamConfig;
import pl.edu.agh.evolutus.utils.Geometry;

import javax.inject.Inject;

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
		//foram.setEnergy(config.initialEnergy());
		return foram;
	}

	public IForam createForam(ForamType foramType, Genome genome) {
		assertType(foramType, ForamType.SEXUAL_HAPLOID, ForamType.SEXUAL_ASEXUAL_HAPLOID);
		IForam foram = createForam(foramType);
		foram.setGenotype(new HaploidGenotype(genome, foram.getAddress()));
		foram.setShell(shellFactory.createInitialShell(foram));
		foram.setEnergy(foram.getShell().getVolumeShell() * foram.getGenotype().get(Genome.METABOLIC_EFFECTIVENESS).getValue());
		return foram;
	}

	public IForam createForam(ForamType foramType, Genome genomeA, Genome genomeB) {
		assertType(foramType, ForamType.SEXUAL_DIPLOID, ForamType.SEXUAL_ASEXUAL_DIPLOID);
		IForam foram = createForam(foramType);
		foram.setGenotype(new DiploidGenotype(genomeA, genomeB, foram.getAddress()));
		foram.setShell(shellFactory.createInitialShell(foram));
		foram.setEnergy(foram.getShell().getVolumeShell() * foram.getGenotype().get(Genome.METABOLIC_EFFECTIVENESS).getValue());
		return foram;
	}

	private void assertType(ForamType actual, ForamType... expecteds) {
		for (ForamType expected : expecteds) {
			if (expected.equals(actual)) {
				return;
			}
		}
		String msg = String.format("Unexpected foram type: %s. Allowed: %s", actual, StringUtils.join(expecteds, ", "));
		throw new IllegalArgumentException(msg);
	}
}
