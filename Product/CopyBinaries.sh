cp $GATEWAY_DIR/AggregatorEJB/dist/AggregatorEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/AuditRepositoryEJB/dist/AuditRepositoryEJB.jar ~/NHINC_Binaries/.
cp $COMMON_DIR/AuditLogEJB/dist/AuditLogEJB.jar ~/NHINC_Binaries/.
cp $COMMON_DIR/ConnectionManagerEJB/dist/ConnectionManagerEJB.jar ~/NHINC_Binaries/.
cp $COMMON_DIR/DocumentTransformEJB/dist/DocumentTransformEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/EntityAuditLogQueryEJB/dist/EntityAuditLogQueryEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/EntityHiemSubscriptionEJB/dist/EntityHiemSubscriptionEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/GatewaySubscriptionRepositoryEJB/dist/GatewaySubscriptionRepositoryEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/GatewayPolicyEngineFacadeEJB/dist/GatewayPolicyEngineFacadeEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/GatewayPolicyEngineTransformationEjb/dist/GatewayPolicyEngineTransformationEjb.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincAuditLogDteEJB/dist/NhincAuditLogDteEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincAuditQueryEJB/dist/NhincAuditQueryEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincDocQueryEJB/dist/NhincDocQueryEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincDocRetrieveEJB/dist/NhincDocRetrieveEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincHiemSubscriptionEJB/dist/NhincHiemSubscriptionEJB.jar ~/NHINC_Binaries/.
cp $COMMON_DIR/NhincSubDiscDataTransformsEJB/dist/NhincSubDiscDataTransformsEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincSubjectDiscoveryEJB/dist/NhincSubjectDiscoveryEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/PatientCorrelationEJB/dist/PatientCorrelationEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/PatientCorrelationFacadeDteEjb/dist/PatientCorrelationFacadeDteEjb.jar ~/NHINC_Binaries/.
cp $COMMON_DIR/PropAccessorEJB/dist/PropAccessorEJB.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/SubscriptionDteEjb/dist/SubscriptionDteEjb.jar ~/NHINC_Binaries/.
cp $COMMON_DIR/UDDIUpdateManagerEJB/dist/UDDIUpdateManagerEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/AdapterMpiEJB/dist/AdapterMpiEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/AdapterPIPEJB/dist/AdapterPIPEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/AdapterPEPEJB/dist/AdapterPEPEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/AdapterPolicyEngineOrchestratorEJB/dist/AdapterPolicyEngineOrchestratorEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/AdapterPolicyEngineTransformEJB/dist/AdapterPolicyEngineTransformEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/AdapterReidentificationEJB/dist/AdapterReidentificationEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/DocumentRepositoryEJB/dist/DocumentRepositoryEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/MpiEJB/dist/MpiEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/MpiManagerEJB/dist/MpiManagerEJB.jar ~/NHINC_Binaries/.
cp $ADAPTER_DIR/General/ConsumerPreferencesProfileGUI/dist/ConsumerPreferencesProfileGUI.war ~/NHINC_Binaries/.

cp $COMMON_DIR/NhincHL7JaxbLib/dist/NhincHL7JaxbLib.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincSAMLCallbackLib/dist/NhincSAMLCallbackLib.jar ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhincSamlTokenLib/dist/NhincSamlTokenLib.jar ~/NHINC_Binaries/.

cp $ADAPTER_DIR/General/AdapterCA/dist/AdapterCA.zip ~/NHINC_Binaries/.
cp $GATEWAY_DIR/EntityCA/dist/EntityCA.zip ~/NHINC_Binaries/.
cp $GATEWAY_DIR/NhinCA/dist/NhinCA.zip ~/NHINC_Binaries/.

cp passfile ~/NHINC_Binaries/.
cp DeployAllBinaries.sh ~/NHINC_Binaries/.
chmod +x ~/NHINC_Binaries/DeployAllBinaries.sh
cp UndeployAllBinaries.sh ~/NHINC_Binaries/.
chmod +x ~/NHINC_Binaries/UndeployAllBinaries.sh




