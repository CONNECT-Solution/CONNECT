package dbfit.environment;

public class DbEnvironmentFactory {
	private static DBEnvironment environment;
	public static DBEnvironment getDefaultEnvironment(){
		return environment;
	}
	public static void setDefaultEnvironment( DBEnvironment newDefaultEnvironment){
		environment=newDefaultEnvironment;
	}
}
