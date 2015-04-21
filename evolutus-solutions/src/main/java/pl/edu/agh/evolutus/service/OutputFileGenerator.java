package pl.edu.agh.evolutus.service;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jage.platform.component.provider.IComponentInstanceProvider;

import com.google.common.collect.Lists;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.utils.Utils;

public abstract class OutputFileGenerator {

	public static List<OutputFileGenerator> getGenerators(IComponentInstanceProvider instanceProvider) {
		return Lists.newArrayList(
				instanceProvider.getInstance(PsiFileGenerator.class),
				instanceProvider.getInstance(CsvFileGenerator.class),
				instanceProvider.getInstance(HtmlFileGenerator.class),
				instanceProvider.getInstance(ConfFileGenerator.class)
		);
	}

	@Inject
	protected TemplateRenderer templateRenderer;

	protected abstract String outputDirectoryName();

	protected abstract void generateInner(String simulationStartString, File outputDirectory, Map<Long,
			List<OceanFragmentInfo>> infoMap) throws IOException, FileGeneratorException;

	public void generate(Timestamp simulationStart, File outputDirectory, Map<Long, List<OceanFragmentInfo>> infoMap)
			throws FileGeneratorException {
		outputDirectory = new File(outputDirectory, outputDirectoryName());
		outputDirectory.mkdirs();

		String simulationStartString = Utils.getTimestampAsString(simulationStart);

		try {
			generateInner(simulationStartString, outputDirectory, infoMap);
		} catch (IOException e) {
			throw new FileGeneratorException(e);
		}
	}

	protected List<Stats> infoMapToStatsList(Map<Long, List<OceanFragmentInfo>> infoMap) {
		List<Stats> statsList = new ArrayList<>();
		for (Long stepNo : infoMap.keySet()) {
			long foramsCount = 0;
			double algaeAvailability = 0.0;
			for (OceanFragmentInfo info : infoMap.get(stepNo)) {
				foramsCount += info.getForamsCount();
				algaeAvailability += info.getAlgaeAvailability();
			}
			statsList.add(new Stats(stepNo, foramsCount, algaeAvailability));
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
		public final double algaeAvailability;

		private Stats(Long stepNo, Long foramsCount, Double algaeAvailability) {
			this.stepNo = stepNo;
			this.foramsCount = foramsCount;
			this.algaeAvailability = algaeAvailability;
		}

		public long getStepNo() {
			return stepNo;
		}

		public long getForamsCount() {
			return foramsCount;
		}

		public double getAlgaeAvailability() {
			return algaeAvailability;
		}
	}

}

