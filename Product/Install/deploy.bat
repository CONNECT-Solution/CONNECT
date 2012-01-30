set AS_HOME=C:\Sun\AppServer
set IDE_HOME=C:\Program Files\NetBeans 6.7.1
set ANT_HOME=%IDE_HOME%\java2\ant
set ANT_OPTS=-Xmx872m -XX:MaxPermSize=512m "-Dcom.sun.aas.instanceName=server"
set NHINC_PROPERTIES_DIR=%AS_HOME%\domains\domain1\config\nhin
set JAVA_HOME=C:\Java\jdk1.6.0_16
set PATH=%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem;%JAVA_HOME%\bin;%ANT_HOME%\bin

ant -buildfile deploy.xml %*

exit /B %ERRORLEVEL%