#
# Deploy the Gateway Composite Applications
#
$AS_HOME/bin/asadmin deploy-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhinCA.zip
$AS_HOME/bin/asadmin deploy-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityCA.zip
#
# Start the Gateway Composite Applications
#
$AS_HOME/bin/asadmin start-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile NhinCA
$AS_HOME/bin/asadmin start-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile EntityCA
#
# Deploy the Adapter Composite Application
#
$AS_HOME/bin/asadmin deploy-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterCA.zip
#
# Start the Adapter Composite Application
#
$AS_HOME/bin/asadmin start-jbi-service-assembly --user admin --passwordfile $HOME/NHINC_Binaries/passfile AdapterCA
#
# Deploy the Gateway EJB Modules
#
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AggregatorEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AuditRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AuditLogEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/ConnectionManagerEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/DocumentTransformEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityAuditLogQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/EntityHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/GatewaySubscriptionRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/GatewayPolicyEngineFacadeEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/GatewayPolicyEngineTransformationEjb.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincAuditLogDteEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincAuditQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincDocRetrieveEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincDocQueryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincHiemSubscriptionEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincSubjectDiscoveryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/NhincSubDiscDataTransformsEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PatientCorrelationEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PatientCorrelationFacadeDteEjb.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/PropAccessorEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/SubscriptionDteEjb.jar
#$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/UDDIUpdateManagerEJB.jar
#
# Deploy the Adapter EJB modules
#
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterMpiEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPolicyEngineTransformEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterReidentificationEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/MpiEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/MpiManagerEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/DocumentRepositoryEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPIPEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPEPEJB.jar
$AS_HOME/bin/asadmin deploy --user admin --passwordfile $HOME/NHINC_Binaries/passfile ~/NHINC_Binaries/AdapterPolicyEngineOrchestratorEJB.jar
