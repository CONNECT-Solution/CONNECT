@ECHO OFF

echo.
SETLOCAL

SET glassfishAntDeployHome=C:\Projects\NHINC\Current\Build\Packages\Glassfish\glassfish-ant-deploy
SET compAppDir=C:\Projects\NHINC\Current\Build\Packages\Glassfish\glassfish-ant-deploy\nhinc

:: Check parameter count
if {%1}=={} goto USAGE
:: Otherwise set some variables.
SET targetAddress=%1

IF NOT EXIST %glassfishAntDeployHome%\NHINC.zip (
 echo.
 echo ERROR: Missing NHINC.zip file.
 GOTO :USAGE
)

ECHO Removing old exploded NHINC.zip artifacts.
RMDIR /S /Q %compAppDir%

ant deploy -f build.xml -DDeployment-Server-IP=%targetAddress% -DglassfishAntDeployHome=%glassfishAntDeployHome% -DcompAppDir=%compAppDir% 
GOTO :PAUSE

:USAGE 
echo.
echo This script deploys all EJB and CA projects to a GlassFish domain using the 
echo same list and actions that the automated deployment under CI Factory uses.
echo.
echo This script expects that NHINC.zip already has been placed in:
echo %glassfishAntDeployHome%
echo.
echo Usage: 
echo 	%0 TARGET.IP.ADD.RESS
echo       --- or ---
echo 	%0 localhost
echo.
GOTO :PAUSE

:PAUSE
@pause
