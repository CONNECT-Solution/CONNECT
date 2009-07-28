echo off 
rem *****************************************************
rem    THIS SCRIPT WILL LAUNCH THE TESTING UI FOR HAPI
rem YOU MUST FIRST SET THE ENVIRONMENT VARIABLE HAPI_HOME
rem                TO THIS DIRECTORY 
rem ***************************************************** 

if NOT EXIST %HAPI_HOME% ( echo Warning: The HAPI_HOME environment variable is not set or not valid. )

set PORT=8888

echo HAPI_HOME is set to %HAPI_HOME%
echo Running ca.uhn.hl7v2.app.TestPanel with HL7Service on port %PORT%

java -Dlog4j.configuration=file:///%HAPI_HOME%/log4j.xml -Dhapi.home=%HAPI_HOME% -jar hapi-0.5beta.jar %PORT%
