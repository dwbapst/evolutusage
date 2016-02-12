package pl.edu.agh.evolutus.statistics.dao;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;
import pl.edu.agh.evolutus.statistics.model.Simulation;

import java.util.*;

public class OceanFragmentInfoDao extends Dao<OceanFragmentInfo> {

	@Override
	protected Class<OceanFragmentInfo> getReturnType() {
		return OceanFragmentInfo.class;
	}

	public List<OceanFragmentInfo> getStats(Simulation simulation) {
		return createQuery(simulation)
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

