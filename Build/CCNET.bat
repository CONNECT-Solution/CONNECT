FOR /F "TOKENS=4 DELIMS=\," %%A IN ('cd') DO set CodeLineName=%%A
FOR /F "TOKENS=3 DELIMS=\," %%A IN ('cd') DO set ProjectName=%%A
FOR /F %%A IN ('hostname') DO SET HostName=%%A

title %ProjectName% %CodeLineName% %HostName% Build Server
set PATH=%SystemRoot%\system32;%SystemRoot%;%SystemRoot%\System32\Wbem

cd server
CCNET.exe -config:"..\CCNet.%HostName%.xml" -remoting:on