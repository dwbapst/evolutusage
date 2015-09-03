package pl.edu.agh.evolutus.cmd.parser;

import org.apache.commons.lang3.StringUtils;

import pl.edu.agh.evolutus.cmd.ApplicationException;

public class ArgsParser {

	public static ArgsParserResult parse(String[] args) throws ApplicationException {
		if (args.length == 0) {
			String applicationModes = StringUtils.join(ApplicationMode.values(), ", ");
			throw new ApplicationException("You have to specify application mode: " + applicationModes + ".");
		}

		ApplicationMode applicationMode = ApplicationMode.fromString(args[0]);
		args = subArray(args, 1);
		assertNumberOfParameters(applicationMode, args);
		switch (applicationMode) {
		case SIMULATION:
			return new ArgsParserResult(applicationMode, args);
		case LIST:
			return new ArgsParserResult(applicationMode, args);
		case GENERATION:
			GenerationMode generationMode = GenerationMode.fromString(args[0]);
			args = subArray(args, 1);
			return new ArgsParserResult(applicationMode, generationMode, args);
		default:
			throw new ApplicationException("Unexpected application mode: " + applicationMode);
		}
	}

	/* package */static String[] subArray(String[] array, int from) {
		String[] subArray = new String[array.length - from];
		if (subArray.length > 0) {
			System.arraycopy(array, from, subArray, 0, subArray.length);
		}
		return subArray;
	}

	private static void assertNumberOfParameters(ApplicationMode mode, String[] args) throws ApplicationException {
		int expectedParameters = 0;
		switch (mode) {
		case SIMULATION:
			expectedParameters = 1;
			break;
		case LIST:
			expectedParameters = 1;
			break;
		case GENERATION:
			expectedParameters = 3;
			break;
		}

		if (args.length < expectedParameters) {
			throw new ApplicationException("Wrong number of parameters for application mode: " + mode + "." +
					" Expected: " + expectedParameters + ", got: " + args.length);
		}
	}
}
