#
# undeploy the GUI modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/UniversalClientGUI.war
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/ConsumerPreferencesProfileGUI.war
#
# undeploy the Example EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhinInterfaceTestHelperEJB.jar
#
# undeploy the Adapter EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityHiemSubscriptionProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterAuthenticationEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityDocQueryProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterMpiEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPIPEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPEPEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPolicyEngineEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPolicyEngineOrchestratorEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPolicyEngineTransformEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterReidentificationEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/DocumentRepositoryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityAuditQueryProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/MpiEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/MpiManagerEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterDocRegistryAndRepositoryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterDocRepository2Soap12EJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntitySubjectDiscoveryProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityDocRetrieveProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PatientCorrelationProxy.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterMPIProxyEJB.jar
#
# undeploy the Gateway EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPolicyEngineProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterDocQueryProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterDocRetrieveProxyEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AggregatorEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AuditRepositoryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityAuditLogQueryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityDocQueryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/GatewaySubscriptionRepositoryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/GatewayPolicyEngineFacadeEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/GatewayPolicyEngineTransformationEjb.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincAuditQueryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincDocQueryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincDocRetrieveEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincSubjectDiscoveryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntitySubjectDiscoveryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PatientCorrelationEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PatientCorrelationFacadeDteEjb.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/HiemSubscriptionRepositoryUtilEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityDocRetrieveEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterSubjectDiscoveryProxyEJB.jar
#
# undeploy the Commom EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/ConnectionManagerEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/DocumentTransformEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PropAccessorEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/UDDIUpdateManagerEJB.jar




