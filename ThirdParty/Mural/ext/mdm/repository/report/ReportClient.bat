@echo off
java -classpath lib\index-core.jar;lib\master-index-report.jar;lib\ejb.jar;lib\j2ee.jar com.sun.mdm.index.report.ReportClient %1 %2 %3 %4
