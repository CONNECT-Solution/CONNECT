######################################################
## Comonent Proxy Configuration Utility Application ##
######################################################

This application has been created to provide the ability to switch the 
bean implementation types that are used in component proxy configuration
files. The ability to switch between no-op, Java, secured web services, 
and unsecured web services are available. 

***** Prerequisites *****
Before the application can be launched, the following steps must be performed.
* Compile the ConfigurationUtility source using "ant package.create"
* Copy libraries to the "RunFrom" directory by running CopyLibs.bat which
is located in that directory.
* Update batch files if necessary
** Each SwitchTo*.bat file in the RunFrom directory has some properties that 
may need to be modified to match your system. These include the locations for 
JAVA_HOME and nhinc.properties.dir

***** Running the Application *****
Four batch files are provided for updating the configuration files. These are
located in the RunFrom directory and all begin with "SwitchTo" followed by the 
target that is used. The implementation type is provided as a command line argument.

***** Overriding the configuration file directory *****
The configuration file directory defaults to the value of the environment 
variable: nhinc.properties.dir. It is provided on the command line in each batch file
that is used to launch the application. An alternative option to specify the directory
location is to provide the path to the directory as a second command line argument as
shown here.

java -Dconfig.util.log.dir=C:\Temp -jar ConfigurationUtility.jar java C:/Sun/AppServer/domains/domain1/config/nhin

