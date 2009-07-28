@echo off
title FitNesse Server - Press Ctrl-C to Close
set CLASSPATH=fitnesse.jar;wikiplugins.jar;activation-1.1.jar;avalon-framework-4.1.3.jar;bcprov-jdk15-133.jar;commandlinefixture.jar;commons-attributes-api-2.1.jar;commons-beanutils-1.7.0.jar;commons-codec-1.3.jar;commons-discovery-0.2.jar;commons-httpclient-3.0.jar;commons-lang-2.3.jar;commons-logging-1.1.jar;jaxb-api-2.0.jar;jaxb-impl-2.0.1.jar;jaxb-xjc-2.0.1.jar;jaxen-1.1-beta-9.jar;jaxws-api-2.0.jar;jdom-1.0.jar;jsr173_api-1.0.jar;log4j-1.2.14.jar;logkit-1.0.1.jar;mail-1.4.jar;opensaml-1.0.1.jar;qdox-1.5.jar;servlet-api-2.3.jar;stax-api-1.0.1.jar;stax-utils-20040917.jar;wsdl4j-1.6.1.jar;wss4j-1.5.1.jar;wstx-asl-3.2.0.jar;xercesImpl-2.7.1.jar;xfire-aegis-1.2.6.jar;xfire-annotations-1.2.6.jar;xfire-core-1.2.6.jar;xfire-java5-1.2.6.jar;xfire-jaxb2-1.2.6.jar;xfire-jsr181-api-1.0-M1.jar;xfire-ws-security-1.2.6.jar;xml-apis-1.0.b2.jar;xmlParserAPIs-2.6.2.jar;XmlSchema-1.1.jar;xmlsec-1.3.0.jar;ojdbc14.jar;commons-net-1.4.0.jar;AgilexFitNesseFixtures.jar
java -DProjectName=%ProjectName% -DTP.UserName=%TP.UserName% -DTP.Password=%TP.Password% -DTP.Url=%TP.Url% -DCM_SYSTEM=fitnesse.wiki.cmSystems.ShellOutCmSystem fitnesse.FitNesse %1 %2 %3 %4 %5

if %ERRORLEVEL%==0 goto end
SET /P variable="Hit Enter to exit."

:end
exit /B 0
