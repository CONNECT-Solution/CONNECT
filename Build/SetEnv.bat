
SET ProjectName=NHINC
SET ProjectCodeLineName=3.1
SET Common.Directory.ProjectsRoot.Path=C:\Projects

SET Common.Directory.Product.Name=Product
SET Common.Directory.ThirdParty.Name=ThirdParty
SET Common.Directory.Production.Name=Production
SET Common.Directory.Build.Name=Build
SET Common.Directory.ArtifactRoot.Name=Artifacts
SET Common.Directory.Packages.Name=Packages
SET Common.Directory.UnitTest.Name=UnitTest
SET Common.Directory.Install.Name=Install
SET Common.Directory.DBScripts.Name=DBScripts
SET Common.Directory.Transient.Name=Transient
SET Common.Directory.Tools.Name=Tools

SET Common.Directory.ProjectRoot.Path=C:\Projects\NHINC
SET Common.Directory.CodeLine.Path=C:\Projects\NHINC\3.1
SET Common.Directory.Product.Path=C:\Projects\NHINC\3.1\Product
SET Common.Directory.ThirdParty.Path=C:\Projects\NHINC\3.1\ThirdParty
SET Common.Directory.Production.Path=C:\Projects\NHINC\3.1\Product\Production
SET Common.Directory.Build.Path=C:\Projects\NHINC\3.1\Build
SET Common.Directory.ArtifactRoot.Path=C:\Projects\NHINC\3.1\Build\Artifacts
SET Common.Directory.Packages.Path=C:\Projects\NHINC\3.1\Build\Packages
SET Common.Directory.UnitTest.Path=C:\Projects\NHINC\3.1\Product\UnitTest
SET Common.Directory.Install.Path=C:\Projects\NHINC\3.1\Product\Install
SET Common.Directory.DBScripts.Path=C:\Projects\NHINC\3.1\Product\DBScripts
SET Common.Directory.Transient.Path=C:\Projects\NHINC\3.1\Build\Transient
SET Common.Directory.Tools.Path=C:\Projects\NHINC\3.1\Build\Tools

IF DEFINED ProgramFiles(x86) goto Processor.Type.64
IF NOT DEFINED ProgramFiles(x86) goto Processor.Type.32

:Processor.Type.64

set Processor.Type=64
set ProgramFiles.Current.Processor.Type.Path=%ProgramFiles%
set ProgramFiles.64.Path=%ProgramFiles%
set ProgramFiles.32.Path=%ProgramFiles(x86)%
set ProgramFiles.Default.Path=%ProgramFiles(x86)%

goto TheRest

:Processor.Type.32

set Processor.Type=32
set ProgramFiles.Current.Processor.Type.Path=%ProgramFiles%
set ProgramFiles.32.Path=%ProgramFiles%
set ProgramFiles.Default.Path=%ProgramFiles%

:TheRest


SET JAVA_HOME=C:\Java\jdk1.6.0_16

SET ANT_HOME=C:\Projects\NHINC\3.1\Build\Packages\Ant\Ant

SET ANT_OPTS=-Xmx872m -XX:MaxPermSize=640m "-Dcom.sun.aas.instanceName=server" "-Dlibs.CopyLibs.classpath=C:\PROGRA~1\NETBEA~1.1\java2\ant\extra\org-netbeans-modules-java-j2seproject-copylibstask.jar" "-Dj2ee.platform.classpath=C:\Sun\AppServer\lib\appserv-ws.jar:C:\Sun\AppServer\lib\webservices-rt.jar:C:\Sun\AppServer\lib\webservices-tools.jar:C:\Sun\AppServer\lib\appserv-jstl.jar:C:\Sun\AppServer\lib\mail.jar:C:\Sun\AppServer\lib\appserv-tags.jar:C:\Sun\AppServer\lib\activation.jar:C:\Sun\AppServer\lib\javaee.jar:"

SET JAVAEXE=C:\Java\jdk1.6.0_16\bin\javaw.exe

SET CORBERTURA_BIN=C:\Projects\NHINC\3.1\Build\Packages\Corbertura\bin

SET AS_HOME=C:\Sun\AppServer

SET IDE_HOME=C:\PROGRA~1\NETBEA~1.1

SET NHINC_PROPERTIES_DIR=%AS_HOME%\domains\domain1\config\nhin

SET Common.Directory.Product.Path=C:\Projects\NHINC\3.1\Product

SET SOAPUI_HOME=C:\Program Files (x86)\eviware\soapUI-Pro-3.5.1

SET PATH=C:\Java\jdk1.6.0_16\bin;C:\Java\jdk1.6.0_16;C:\Projects\NHINC\3.1\Build\Packages\Ant\Ant\bin;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Projects\NHINC\3.1\Build\Tools\Sysinternals;C:\Projects\NHINC\3.1\Build\Tools\Sysinternals;C:\Projects\NHINC\3.1\Build\Tools\maven\bin;C:\Projects\NHINC\3.1\Build\Tools\nxslt;C:\Projects\NHINC\3.1\Build\Tools\curl;C:\Projects\NHINC\3.1\Build\Tools\nAnt\bin;C:\Projects\NHINC\3.1\Build\Tools\Sysinternals;C:\Projects\NHINC\3.1\Build\Tools\7-Zip;C:\Program Files\Subversion\bin;C:\Sun\AppServer\bin;C:\Projects\NHINC\3.1\ThirdParty\OpenSSO\ssoAdminTools\opensso\bin;C:\Program Files (x86)\Klocwork\Klocwork 9.1 Server\bin;
        