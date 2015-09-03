package pl.edu.agh.evolutus.cmd.generation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class TemplateRenderer {

	private final VelocityEngine ve;

	public TemplateRenderer() {
		Properties properties = new Properties();
		// Tells velocity to look for template files on classpath.
		properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		// Tells velocity not to create velocity.log file in working directory.
		properties.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		ve = new VelocityEngine(properties);
	}

	public void render(String templateResource, File outputFile, Map<?, ?> parameters) throws IOException {
		VelocityContext context = new VelocityContext(new HashMap<>(parameters));

		Template template = ve.getTemplate(templateResource);
		try (FileWriter writer = new FileWriter(outputFile)) {
			template.merge(context, writer);
		}
	}

}
