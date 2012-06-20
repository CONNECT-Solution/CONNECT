#!/bin/sh
clear
if [ $# -eq 0 ] ; then 
    echo "Usage is reset_interop_node.sh 'mode' where mode is either 'verify' or 'trust'"
    echo "."
    exit 
fi
echo "Press Enter to stop the Application Server (ignore error if App Server is not running)... Ctrl+C to exit"
echo "Stopping Server. Please wait."
$AS_HOME/bin/asadmin stop-domain domain1
rm $AS_HOME/domains/domain1/logs/*.log
if [ $1 = "verify" ] ; then
  echo "Verify Mode"
  echo "Press Enter to reset this node to GW2.2 in Verify Mode. Press Ctrl+C to exit."
  echo "Copying gateway.verify.properties. Please wait."
  cp /nhin/interop_setup/gateway.verify.properties $NHINC_PROPERTIES_DIR/gateway.properties
  cp /nhin/interop_setup/gateway.verify.properties $NHINC_PROPERTIES_DIR/master/gateway.properties
elif [ $1 = "trust" ] ; then
  echo "Trust Mode"
  echo "Press Enter to reset this node to GW2.2 in Trust Mode. Press Ctrl+c to exit."
  echo "Copying gateway.trust.properties. Please wait." 
  cp /nhin/interop_setup/gateway.trust.properties $NHINC_PROPERTIES_DIR/gateway.properties
  cp /nhin/interop_setup/gateway.trust.properties $NHINC_PROPERTIES_DIR/master/gateway.properties
else
  echo "Usage is testme.sh 'mode' where 'mode' is either 'verify' or 'trust' éÌ¬ One must be specified"
  echo "." 
  exit
fi
echo "Press Enter to copy the MPI.xml, adapter.properties and internalConnectionInfo.xml files.  Ctrl+c to exit."
echo "Copying files. Please wait."
cp /nhin/interop_setup/mpi.xml $NHINC_PROPERTIES_DIR/mpi.xml
cp /nhin/interop_setup/adapter.properties $NHINC_PROPERTIES_DIR/adapter.properties
cp /nhin/interop_setup/adapter.properties $NHINC_PROPERTIES_DIR/master/adapter.properties
cp /nhin/interop_setup/internalConnectionInfo.xml $NHINC_PROPERTIES_DIR/internalConnectionInfo.xml
cp /nhin/interop_setup/internalConnectionInfo.xml $NHINC_PROPERTIES_DIR/master/internalConnectionInfo.xml
cp /nhin/interop_setup/uddiConnectionInfo.xml $NHINC_PROPERTIES_DIR/master/uddiConnectionInfo.xml
cp /nhin/interop_setup/uddiConnectionInfo.xml $NHINC_PROPERTIES_DIR/uddiConnectionInfo.xml

cp /nhin/interop_setup/*.jks $AS_HOME/domains/domain1/config
cp /nhin/interop_setup/domain.xml $AS_HOME/domains/domain1/config/domain.xml
echo "Press Enter to copy the SoapUI EndToEndSelfTest xml and properties files."
echo "Copying the SoapUI End to End Test files."
cp /nhin/interop_setup/2-EndToEndSelfTest-soapui-project.xml /nhin/NHINC/ThirdParty/ValidationSuite/2-EndToEndSelfTest-soapui-project.xml
cp /nhin/interop_setup/2-EndToEndSelfTest-soapui-project.properties /nhin/NHINC/ThirdParty/ValidationSuite/2-EndToEndSelfTest-soapui-project.properties

echo "Press Enter to update the Universal Client MySQL tables for correlations. Ctrl+c to exit"
echo "Running MySQL scripts. Please wait."
mysql -uroot -pNHIE-Gateway </nhin/interop_setup/reset_uc_tables.sql
echo "Press Enter to restart the Application Server. Ctrl+c to exit."
echo "Starting Application Server. Please wait."
$AS_HOME/bin/asadmin start-domain domain1
