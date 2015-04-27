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

	public static final String DB_NAME = "evolutus";
	@Inject
	private MongoProvider mongoProvider;

	protected Datastore ds;

	@Override
	public void init() throws ComponentException {
		ds = new Morphia().createDatastore(mongoProvider.getMongoClient(), DB_NAME);
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

