#this will crap out because of timeout on first run after startup.  ignore failure
#it will fail and scuttle the script, so add || true to force success and keep trucking
/nhin/soapui-3.5.1/bin/testrunner.sh -Djava.awt.headless=true -f /dev/null /nhin/ValidationSuite/1-InternalSelfTest-patched.xml || true
/nhin/soapui-3.5.1/bin/testrunner.sh -Djava.awt.headless=true -f /dev/null /nhin/ValidationSuite/2-EndToEndSelfTest-patched.xml || true


#refresh all the files after the validaiton suite possibly shanks them
cp /nhin/ValidationSuite/mpi.xml $AS_HOME/domains/domain1/config/nhin
cp /nhin/NHINC/Dev/adapter.properties $AS_HOME/domains/domain1/config/nhin
cp /nhin/NHINC/Dev/gateway.properties $AS_HOME/domains/domain1/config/nhin
cp /nhin/NHINC/Dev/repository.properties $AS_HOME/domains/domain1/config/nhin
cp /nhin/NHINC/Dev/internalConnectionInfo.xml $AS_HOME/domains/domain1/config/nhin
echo foo: cp /nhin/NHINC/Dev/internalConnectionInfo.xml $AS_HOME/domains/domain1/config/nhin

/nhin/mysql/bin/mysql -uroot -pNHIE-Gateway < /nhin/NHINC/DBScripts/nhincdb/dropall.sql
/nhin/mysql/bin/mysql -uroot -pNHIE-Gateway < /nhin/NHINC/DBScripts/nhincdb/nhincdb.sql
/nhin/mysql/bin/mysql --verbose -uroot -pNHIE-Gateway -Ddocrepository < /nhin/ValidationSuite/populateTestData.sql

