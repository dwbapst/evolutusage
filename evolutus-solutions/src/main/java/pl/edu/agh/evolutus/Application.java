package pl.edu.agh.evolutus;

import org.jage.platform.cli.CliNodeBootstrapper;

public class Application {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage:");
			System.out.println("\tevolutus.sh output-dir [config-file]");
			System.exit(127);
		}

		System.setProperty("evolutus.output.dir", args[0]);
		if (args.length > 1) {
			System.setProperty("evolutus.config", args[1]);
		}

		CliNodeBootstrapper bootstrapper = new CliNodeBootstrapper(
				new String[] { "-Dage.node.conf=classpath:evolutus_base.xml" });
		bootstrapper.start();
	}
}
