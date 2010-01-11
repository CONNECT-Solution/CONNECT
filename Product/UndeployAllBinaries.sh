#
# undeploy the Example EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile NhinInterfaceTestHelperEJB
#
# undeploy the Adapter EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityHiemSubscriptionProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterAuthenticationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityDocQueryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterMpiEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterPIPEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterPEPEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterPolicyEngineEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterPolicyEngineOrchestratorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterPolicyEngineTransformEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterReidentificationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile DocumentRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityAuditQueryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile MpiEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile MpiManagerEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterDocRegistryAndRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterDocRepository2Soap12EJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntitySubjectDiscoveryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityDocRetrieveProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile PatientCorrelationProxy
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterMPIProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterAuditQueryEJB
#
# undeploy the Gateway EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterPolicyEngineProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterDocQueryProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterDocRetrieveProxyEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AggregatorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AuditRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityAuditLogQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityDocQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile GatewaySubscriptionRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile GatewayPolicyEngineFacadeEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile GatewayPolicyEngineTransformationEjb
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile NhincAuditQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile NhincDocQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile NhincDocRetrieveEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile NhincSubjectDiscoveryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntitySubjectDiscoveryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile PatientCorrelationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile PatientCorrelationFacadeDteEjb
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityHiemSubscriptionEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile NhincHiemSubscriptionEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile HiemSubscriptionRepositoryUtilEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile EntityDocRetrieveEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile AdapterSubjectDiscoveryProxyEJB
#
# undeploy the Commom EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile ConnectionManagerEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile DocumentTransformEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile PropAccessorEJB
#$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC/nhinc/passfile UDDIUpdateManagerEJB




