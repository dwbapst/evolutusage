package pl.edu.agh.evolutus.cmd.generation;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class OutputFileGenerator {

	@Inject
	protected TemplateRenderer templateRenderer;

	protected abstract String outputDirectoryName();

	protected File getOutputDirectory(File baseOutputDirectory) {
		File outputDirectory = new File(baseOutputDirectory, outputDirectoryName());
		outputDirectory.mkdirs();
		return outputDirectory;
	}

	protected List<Stats> infoMapToStatsList(Map<Long, List<OceanFragmentInfo>> infoMap) {
		List<Stats> statsList = new ArrayList<>();
		for (Long stepNo : infoMap.keySet()) {
			long foramsCount = 0;
			long foramsHaploidCount = 0;
			long foramsDiploidCount = 0;
			double algaeAvailability = 0.0;
			long deadForamsCount = 0;
			long bornForamsCount = 0;
			double averageEnergy = 0.0;
			double averageShellVolume = 0.0;

			for (OceanFragmentInfo info : infoMap.get(stepNo)) {
				foramsCount += info.getForamsCount();
				foramsHaploidCount += info.getHaploidForamsCount();
				foramsDiploidCount += info.getDiploidForamsCount();
				algaeAvailability += info.getAlgaeAvailability();
				deadForamsCount += info.getDeathCount();
				bornForamsCount += info.getBirthCount();
				averageEnergy += info.getTotalEnergy();
				averageShellVolume += info.getAverageShellVolume();
			}
            averageEnergy = (foramsCount == 0) ? 0.0 : averageEnergy / foramsCount;
            averageShellVolume = (foramsCount == 0) ? 0.0: averageShellVolume / foramsCount;
			statsList.add(new Stats(stepNo, foramsCount, foramsHaploidCount, foramsDiploidCount, deadForamsCount,
					bornForamsCount,
					algaeAvailability, averageEnergy, averageShellVolume));
		}
		return statsList;
	}

	public static class FileGeneratorException extends Exception {
		public FileGeneratorException(String message) {
			super(message);
		}

		public FileGeneratorException(Throwable cause) {
			super(cause.getMessage(), cause);
		}

		public FileGeneratorException(String message, Throwable cause) {
			super(message, cause);
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
		public final double averageShellVolume;


		private Stats(Long stepNo, Long foramsCount,
                      Long foramsHaploidCount, Long foramsDiploidCount,
                      Long deadForamsCount, Long bornForamsCount,
                      Double algaeAvailability,
                      Double averageEnergy,
                      Double averageShellVolume) {
			this.stepNo = stepNo;
			this.foramsCount = foramsCount;
			this.foramsHaploidCount = foramsHaploidCount;
			this.foramsDiploidCount = foramsDiploidCount;
			this.algaeAvailability = algaeAvailability;
			this.deadForamsCount = deadForamsCount;
			this.bornForamsCount = bornForamsCount;
			this.averageEnergy = averageEnergy;
			this.averageShellVolume = averageShellVolume;
		}

		public long getStepNo() {
			return stepNo;
		}

		public long getForamsCount() {
			return foramsCount;
		}

		public long getForamsHaploidCount() {
			return foramsHaploidCount;
		}

		public long getForamsDiploidCount() {
			return foramsDiploidCount;
		}

		public double getAlgaeAvailability() {
			return algaeAvailability;
		}

		public long getDeadForamsCount() {
			return deadForamsCount;
		}

		public long getBornForamsCount() {
			return bornForamsCount;
		}

		public double getAverageEnergy() {
			return averageEnergy;
		}

		public double getAverageShellVolume() { return  averageShellVolume; }


	}

}

