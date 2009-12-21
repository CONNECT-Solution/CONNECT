FOR /F "TOKENS=1 DELIMS=," %%A IN ('cd') DO set BaseDir=%%A

IF EXIST ..\..\Build\Packages\Ant\Ant\bin\ant.bat. (
    cd ..\..
    FOR /F "TOKENS=1 DELIMS=," %%A IN ('cd') DO set CodeLineDir=%%A
    set ANT_HOME=%CodeLineDir%\Build\Packages\Ant\Ant
) ELSE (
    set ANT_HOME=%BaseDir%\Ant
)

cd %BaseDir%
set ANT_OPTS=-Xmx512m -XX:MaxPermSize=256m
set JAVA_HOME=C:\Java\jdk1.6.0_16
set PATH=%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem;%JAVA_HOME%\bin;%ANT_HOME%\bin;

ant -buildfile deploy.xml %*

exit /B %ERRORLEVEL%