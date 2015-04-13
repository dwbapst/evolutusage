package pl.edu.agh.evolutus.statistics.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.evolutus.statistics.model.OceanFragmentInfo;

public class OceanFragmentInfoDao extends Dao<OceanFragmentInfo> {

	private static final String COLLECTION_NAME = "OceanFragmentInfo";

	@Override
	protected String getCollectionName() {
		return COLLECTION_NAME;
	}

	public List<OceanFragmentInfo> getStats(Timestamp simulationStart) {
		return ds.createQuery(OceanFragmentInfo.class)
				.field("simulationStart").equal(simulationStart.getTime())
				.field("stepNo").greaterThan(0L)
				.asList();
	}

	public Map<Long, List<OceanFragmentInfo>> getInfoGroupedByStepNo(Timestamp simulationStart) {
		List<OceanFragmentInfo> infoList = getStats(simulationStart);
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

