package pl.edu.agh.evolutus.statistics.dao;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import pl.edu.agh.evolutus.service.MongoProvider;

public abstract class Dao<T> implements IStatefulComponent {

	@Inject
	private MongoProvider mongoProvider;

	protected Datastore ds;

	protected abstract String getCollectionName();

	@Override
	public void init() throws ComponentException {
		ds = new Morphia().createDatastore(mongoProvider.getMongoClient(), getCollectionName());
	}

	@Override
	public boolean finish() throws ComponentException {
		return true;
	}

	public void insert(T... objects) {
		insert(Arrays.asList(objects));
	}

	public void insert(Collection<T> objects) {
		ds.save(objects);
	}
}

