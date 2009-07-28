@ECHO OFF

echo.
SETLOCAL

:: Check parameter count
if {%2}=={} goto USAGE
:: Otherwise set some variables.
SET targetAddress=%1
SET anttarget=%2

ant %anttarget% -f build.xml -DTARGET_IP=%targetAddress%
GOTO :PAUSE

:USAGE 
echo.
echo This script ^(un^)deploys all EJB and CA projects to a GlassFish domain using the 
echo same list and actions that the automated deployment under then CI build system uses.
echo.
echo Usage: 
echo 	%0 TARGET.IP.ADD.RESS ANTTARGET
echo       --- or ---
echo 	%0 localhost ANTTARGET
echo.
echo Where:
echo    TARGET.IP.ADD.RESS is either a dotted decimal network address or hostname, including localhost
echo    ANTTARGET is one of [showvars^|deploy^|undeploy^|unzip]
echo        ^(the deploy target will unzip the necessary files automatically so you don't have to unzip first^)
echo.
GOTO :PAUSE

:PAUSE
@pause
