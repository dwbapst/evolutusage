package pl.edu.agh.evolutus.service.output;

import pl.edu.agh.evolutus.service.TemplateRenderer;
import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class OutputFileGenerator {

	@Inject
	protected TemplateRenderer templateRenderer;

	protected abstract String outputDirectoryName();

	protected abstract void generateInner(Simulation simulation, File outputDirectory,
			Map<Long, List<OceanFragmentInfo>> infoMap) throws IOException, FileGeneratorException;

	public void generate(Simulation simulation, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws FileGeneratorException {
		outputDirectory = new File(outputDirectory, outputDirectoryName());
		outputDirectory.mkdirs();

		try {
			generateInner(simulation, outputDirectory, infoMap);
		} catch (IOException e) {
			throw new FileGeneratorException(e);
		}
	}

	protected List<Stats> infoMapToStatsList(Map<Long, List<OceanFragmentInfo>> infoMap) {
		List<Stats> statsList = new ArrayList<>();
		for (Long stepNo : infoMap.keySet()) {
			long foramsCount = 0;
			long foramsHaploidCount =0;
			long foramsDiploidCount =0;
			double algaeAvailability = 0.0;
			long deadForamsCount = 0;
			long bornForamsCount = 0;
			double averageEnergy = 0;

			for (OceanFragmentInfo info : infoMap.get(stepNo)) {
				foramsCount += info.getForamsCount();
				foramsHaploidCount += info.getHaploidForamsCount();
				foramsDiploidCount +=info.getDiploidForamsCount();
				algaeAvailability += info.getAlgaeAvailability();
				deadForamsCount += info.getDeathCount();
				bornForamsCount += info.getBirthCount();
				averageEnergy += info.getTotalEnergy();
			}
			averageEnergy = averageEnergy/foramsCount;
			statsList.add(new Stats(stepNo, foramsCount, foramsHaploidCount, foramsDiploidCount, deadForamsCount, bornForamsCount, algaeAvailability, averageEnergy));
		}
		return statsList;
	}

	public static class FileGeneratorException extends Exception {
		public FileGeneratorException(Throwable cause) {
			super(cause.getMessage(), cause);
		}
	}

	public static class Stats {
		public final long stepNo;
		public final long foramsCount;
		public final long foramsHaploidCount;
		public final long foramsDiploidCount;
		public final double algaeAvailability;
		public final long deadForamsCount;
		public final long bornForamsCount;
		public final double averageEnergy;

		private Stats(Long stepNo, Long foramsCount, Long foramsHaploidCount, Long foramsDiploidCount,
					  Long deadForamsCount, Long bornForamsCount, Double algaeAvailability, Double averageEnergy) {
			this.stepNo = stepNo;
			this.foramsCount = foramsCount;
			this.foramsHaploidCount = foramsHaploidCount;
			this.foramsDiploidCount = foramsDiploidCount;
			this.algaeAvailability = algaeAvailability;
			this.deadForamsCount = deadForamsCount;
			this.bornForamsCount = bornForamsCount;
			this.averageEnergy = averageEnergy;
		}

		public long getStepNo() {
			return stepNo;
		}

		public long getForamsCount() {
			return foramsCount;
		}

		public long getForamsHaploidCount() { return foramsHaploidCount; }

		public long getForamsDiploidCount() { return foramsDiploidCount; }

		public double getAlgaeAvailability() {
			return algaeAvailability;
		}

		public long getDeadForamsCount() { return deadForamsCount; }

		public long getBornForamsCount() { return bornForamsCount; }

		public double getAverageEnergy() { return averageEnergy; }
	}

}

