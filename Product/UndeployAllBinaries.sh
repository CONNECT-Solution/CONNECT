#
# undeploy the GUI modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile UniversalClientGUI.war
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ConsumerPreferencesProfileGUI.war
#
# undeploy the Example EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhinInterfaceTestHelperEJB
#
# undeploy the Adapter EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityHiemSubscriptionProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterAuthenticationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityDocQueryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterMpiEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPIPEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPEPEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPolicyEngineEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPolicyEngineOrchestratorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPolicyEngineTransformEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterReidentificationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile DocumentRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityAuditQueryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile MpiEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile MpiManagerEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterDocRegistryAndRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterDocRepository2Soap12EJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntitySubjectDiscoveryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityDocRetrieveProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PatientCorrelationProxy
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterMpiProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterAuditQueryEJB
#
# undeploy the Gateway EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPolicyEngineProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterDocQueryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterDocRetrieveProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AggregatorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AuditRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityAuditLogQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityDocQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile GatewaySubscriptionRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile GatewayPolicyEngineFacadeEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile GatewayPolicyEngineTransformationEjb
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincAuditQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincDocQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincDocRetrieveEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincSubjectDiscoveryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntitySubjectDiscoveryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PatientCorrelationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PatientCorrelationFacadeDteEjb
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityHiemSubscriptionEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincHiemSubscriptionEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile HiemSubscriptionRepositoryUtilEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityDocRetrieveEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterSubjectDiscoveryProxyEJB
#
# undeploy the Commom EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ConnectionManagerEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile DocumentTransformEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PropAccessorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile UDDIUpdateManagerEJB




