#
# Shared libraries
#
#cp ~/NHINC/nhinc/NhincCommonTypesLib.jar $AS_HOME/lib/.
#cp ~/NHINC/nhinc/NhincSAMLCallbackLib.jar $AS_HOME/lib/.
#
# Deploy the Example EJB modules
#
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/NhinInterfaceTestHelperEJB.jar
#
# Deploy the Adapter EJB modules
#
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityHiemSubscriptionProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterAuthenticationEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityDocQueryProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterMpiEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterPIPEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterPEPEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterPolicyEngineEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterPolicyEngineOrchestratorEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterPolicyEngineTransformEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterReidentificationEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/DocumentRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityAuditQueryProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/MpiEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/MpiManagerEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterDocRegistryAndRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterDocRepository2Soap12EJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntitySubjectDiscoveryProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityDocRetrieveProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/PatientCorrelationProxy.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterMPIProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterAuditQueryEJB.jar
#
# Deploy the Gateway EJB modules
#
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterPolicyEngineProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterDocQueryProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterDocRetrieveProxyEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AggregatorEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AuditRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityAuditLogQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityDocQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/GatewaySubscriptionRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/GatewayPolicyEngineFacadeEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/GatewayPolicyEngineTransformationEjb.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/NhincAuditQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/NhincDocQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/NhincDocRetrieveEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/NhincSubjectDiscoveryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntitySubjectDiscoveryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/PatientCorrelationEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/PatientCorrelationFacadeDteEjb.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/NhincHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/HiemSubscriptionRepositoryUtilEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/EntityDocRetrieveEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/AdapterSubjectDiscoveryProxyEJB.jar
#
# Deploy the Commom EJB modules
#
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/ConnectionManagerEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/DocumentTransformEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/PropAccessorEJB.jar
#$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ~/NHINC/nhinc/UDDIUpdateManagerEJB.jar




