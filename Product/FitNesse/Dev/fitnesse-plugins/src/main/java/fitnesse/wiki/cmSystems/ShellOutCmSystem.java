package fitnesse.wiki.cmSystems;

import fitnesse.components.CommandRunner;

public class ShellOutCmSystem {
	
	private static String baseCommand = "./CMSystem/cmAction.bat";
	
	public static void cmUpdate(String file, String payload) throws Exception {
		execute(baseCommand + " cmUpdate " + file);
	}

	public static void cmEdit(String file, String payload) throws Exception {
		execute(baseCommand + " cmEdit " + file);
	}

	public static void cmDelete(String file, String payload) throws Exception {
		execute(baseCommand + " cmDelete " + file);
	}

	private static void execute(String command) throws Exception {
		CommandRunner runner = new CommandRunner(command , "");
		runner.run();
		if (runner.getOutput().length() + runner.getError().length() > 0) {
			System.err.println(" command: " + command);
			System.err.println(" exit code: " + runner.getExitCode());
			System.err.println(" out:" + runner.getOutput());
			System.err.println(" err:" + runner.getError());
		}
	}
}
