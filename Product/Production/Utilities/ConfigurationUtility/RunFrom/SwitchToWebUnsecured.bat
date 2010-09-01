if not "%JAVA_HOME%" == "" goto javaHomeAlreadySet
SET JAVA_HOME="C:\Java\jdk1.6.0_16"
SET PATH=%PATH%;%JAVA_HOME%\bin
:javaHomeAlreadySet

java -Dconfig.util.log.dir=C:\Projects\NHINC\Current\Product\Production\Utilities\ConfigurationUtility\RunFrom -Dnhinc.properties.dir=C:/Sun/AppServer/domains/domain1/config/nhin -jar ConfigurationUtility.jar wsunsecured
pause

