package pl.edu.agh.evolutus.statistics.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;

public class OceanFragmentInfoDao extends Dao<OceanFragmentInfo> {

	public List<OceanFragmentInfo> getStats(Simulation simulation) {
		return ds.createQuery(OceanFragmentInfo.class)
				.field("simulationStart").equal(simulation.getSimulationStart().getTime())
				.field("stepNo").greaterThan(0L)
				.asList();
	}

	public Map<Long, List<OceanFragmentInfo>> getInfoGroupedByStepNo(Simulation simulation) {
		List<OceanFragmentInfo> infoList = getStats(simulation);
		if (infoList.size() == 0) {
			return new HashMap<>();
		}

		Map<Long, List<OceanFragmentInfo>> result = new LinkedHashMap<>();
		List<OceanFragmentInfo> tmpList = new ArrayList<>();
		long stepNo = infoList.get(0).getStepNo();

		for (OceanFragmentInfo info : infoList) {
			if (stepNo != info.getStepNo()) {
				result.put(stepNo, tmpList);
				tmpList = new ArrayList<>();
			}
			stepNo = info.getStepNo();
			tmpList.add(info);
		}
		result.put(stepNo, tmpList);

		return result;
	}
}

