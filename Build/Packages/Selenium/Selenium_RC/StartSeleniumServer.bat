title Selenium Server - Press Ctrl-C to Close
java -jar selenium-server.jar %*

if %ERRORLEVEL%==0 goto end
SET /P variable="Hit Enter to exit."

:end
exit /B 0