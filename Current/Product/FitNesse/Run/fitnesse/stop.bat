@echo off
java -cp fitnesse.jar fitnesse.Shutdown %1 %2 %3 %4 %5

if %ERRORLEVEL%==0 goto end
SET /P variable="Hit Enter to exit."

:end
exit /B 0

