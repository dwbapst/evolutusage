package pl.edu.agh.evolutus;

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.jage.platform.cli.CliNodeBootstrapper;

public class Application {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("\tevolutus.sh output-dir [config-files]...");
			System.exit(127);
		}

		System.setProperty("evolutus.output.dir", args[0]);
		if (args.length > 1) {
			LinkedList<String> configFiles = new LinkedList<>(Arrays.asList(args));
			configFiles.removeFirst();
			System.setProperty("evolutus.config", StringUtils.join(configFiles, '\u0000'));
		}

		CliNodeBootstrapper bootstrapper = new CliNodeBootstrapper(
				new String[] { "-Dage.node.conf=classpath:evolutus_base.xml" });
		bootstrapper.start();
	}
}
