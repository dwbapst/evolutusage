package pl.edu.agh.evolutus;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.jage.platform.cli.CliNodeBootstrapper;

public class Application {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage:");
			System.out.println("\tevolutus.sh output-dir comma-separated-genes-list [config-files]...");
			System.exit(127);
		}

		System.setProperty("evolutus.output.dir", args[0]);
		System.setProperty("evolutus.genes.list", args[1]);
		if (args.length > 2) {
			LinkedList<String> configFiles = new LinkedList<>(Arrays.asList(args));
			configFiles.removeFirst();
			configFiles.removeFirst();
			System.setProperty("evolutus.config", StringUtils.join(configFiles, '\u0000'));
		}

		CliNodeBootstrapper bootstrapper = new CliNodeBootstrapper(
				new String[] { "-Dage.node.conf=classpath:evolutus_base.xml" });
		bootstrapper.start();
	}
}
