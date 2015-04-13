package pl.edu.agh.evolutus.statistics.dao;

import pl.edu.agh.evolutus.statistics.model.ForamFossil;

public class ForamFossilDao extends Dao<ForamFossil> {

	private static final String COLLECTION_NAME = "ForamFossil";

	@Override
	protected String getCollectionName() {
		return COLLECTION_NAME;
	}

}

