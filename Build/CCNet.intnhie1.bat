FOR /F "TOKENS=4 DELIMS=\," %%A IN ('cd') DO set CodeLineName=%%A
FOR /F "TOKENS=3 DELIMS=\," %%A IN ('cd') DO set ProjectName=%%A

title NHINC %CodeLineName% Integration Test Build Server
set PATH=%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem

cd server
CCNET.exe -config:"..\CCNet.intnhie1.xml" -remoting:on