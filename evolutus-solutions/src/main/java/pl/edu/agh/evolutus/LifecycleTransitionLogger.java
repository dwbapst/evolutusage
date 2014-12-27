/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2012-08-22
 * $Id: 3d8ca29edafa1e4a3c9b912dc449fdf5418c2049 $
 */

package pl.edu.agh.evolutus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.jage.annotation.FieldsAreNonnullByDefault;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.jage.platform.fsm.StateChangedEvent;
import org.jage.services.core.LifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.String.format;

/**
 * A sample listener that logs lifecycle events and write them to the DOT file.
 *
 * @author AGH AgE Team
 */
@FieldsAreNonnullByDefault
public class LifecycleTransitionLogger implements IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(LifecycleTransitionLogger.class);

	@Inject
	@Nullable
	private LifecycleManager manager;

	private final File dotFile;

	private final FileWriter fileWriter;

	/**
	 * Creates a new logger.
	 *
	 * @throws IOException when the DOT file cannot be opened.
	 */
	public LifecycleTransitionLogger() throws IOException {
		dotFile = File.createTempFile("lifecycle", ".dot");
		fileWriter = new FileWriter(dotFile);
		log.info("Writing to the file: {}", dotFile.getAbsolutePath());

		fileWriter.write("digraph lifecycle {\n");
	}

	/**
	 * Logs the state change and writes it to the file.
	 *
	 * @param event a {@link StateChangedEvent}
	 * @param <S>   a state type.
	 * @param <E>   an event type.
	 * @throws IOException thrown when logger cannot write to the file.
	 * @see EventBus#register(Object)
	 */
	@Subscribe
	public <S extends Enum<S>, E extends Enum<E>> void logStateChanged(final StateChangedEvent<S, E> event) {
		log.info("Event {}.", event);

		try {
			fileWriter.write(format("\t%s -> %s [label=\"%s\"];%n", event.getPreviousState(), event.getNewState(),
					event.getEvent()));
		} catch (final IOException e) {
			log.error("Could not write to the file.", e);
		}
	}

	@Override
	public void init() throws ComponentException {
		manager.register(this);
	}

	@Override
	public boolean finish() {
		try {
			fileWriter.write("}");
			fileWriter.close();
			log.info("DOT file: {}", dotFile.getAbsolutePath());
			return true;
		} catch (final IOException e) {
			log.error("Could not write to the file.", e);
			return false;
		}
	}
}
