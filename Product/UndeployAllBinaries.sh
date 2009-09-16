#
# Stop the Gateway Composite Applications
#
$AS_HOME/bin/asadmin shut-down-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhinCA
$AS_HOME/bin/asadmin shut-down-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityCA
#
# Undeploy the Gateway Composite Applications
#
$AS_HOME/bin/asadmin undeploy-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhinCA
$AS_HOME/bin/asadmin undeploy-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityCA
#
# Stop the Adapter Composite Application
#
$AS_HOME/bin/asadmin shut-down-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterCA
#
# Undeploy the Adapter Composite Application
#
$AS_HOME/bin/asadmin undeploy-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterCA
#
# Undeploy the Gateway EJB Modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AggregatorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AuditRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AuditLogEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ConnectionManagerEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile DocumentTransformEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityAuditLogQueryEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile GatewaySubscriptionRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile GatewayPolicyEngineFacadeEJB.jar
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile GatewayPolicyEngineTransformationEjb
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincAuditLogDteEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincAuditQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincDocRetrieveEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincDocQueryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincHiemSubscriptionEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincSubjectDiscoveryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhincSubDiscDataTransformsEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PatientCorrelationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PatientCorrelationFacadeDteEjb
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile PropAccessorEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile SubscriptionDteEjb
#$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile UDDIUpdateManagerEJB
#
# Undeploy the Adapter EJB modules
#
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterMpiEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPolicyEngineTransformEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterReidentificationEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile MpiEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile MpiManagerEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile DocumentRepositoryEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPIPEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPEPEJB
$AS_HOME/bin/asadmin undeploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterPolicyEngineOrchestratorEJB
