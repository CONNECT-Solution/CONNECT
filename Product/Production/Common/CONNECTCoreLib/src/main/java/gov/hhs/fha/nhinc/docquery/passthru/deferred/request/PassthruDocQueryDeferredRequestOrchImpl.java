package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.request.proxy.NhinDocQueryDeferredRequestProxy;
import gov.hhs.fha.nhinc.docquery.nhin.deferred.request.proxy.NhinDocQueryDeferredRequestProxyObjectFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;


/**
 * This implementation class contains the flow
 *
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestOrchImpl {

    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestOrchImpl.class);


    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(AdhocQueryRequest adhocQueryRequest,
                                                    AssertionType assertion, NhinTargetSystemType target) {

        getLogger().debug("Beginning of NhincDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");

        AcknowledgementType ack = null;
        DocQueryAcknowledgementType docQueryAcknowledgement = null;

        if (adhocQueryRequest == null) {
            getLogger().error("adhocQueryRequest is null");
            return createErrorAckResponse("adhocQueryRequest is null");
        }

        if (assertion == null) {
            getLogger().error("assertion is null");
            return createErrorAckResponse("assertion is null");
        }

        if (target == null) {
            getLogger().error("target is null");
            return createErrorAckResponse("target is null");
        }

        // Log the incoming request -- Audit Logging
        ack = getDocQueryAuditLogger().auditDQRequest(adhocQueryRequest, assertion,
                              NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if (ack != null) {
           getLogger().debug("DocQueryDeferred Request Audit Log Acknowledgement " + ack.getMessage());
        }


        //Call Nhin component proxy
        docQueryAcknowledgement = callNhinDocQueryDeferredService(adhocQueryRequest, assertion, target);

        if (docQueryAcknowledgement == null){
            getLogger().error("docQueryAcknowledgement response returned by NhinDocQueryDefferedRequest service is null");
            return createErrorAckResponse("docQueryAcknowledgement response returned by NhinDocQueryDefferedRequest service is null");
        }
        else{
            // Audit log the response
            ack = getDocQueryAuditLogger().logDocQueryAck(docQueryAcknowledgement, assertion,
                              NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        }

        getLogger().debug("End of NhincDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");
        
        return docQueryAcknowledgement;
    }


    /**
     * Returns the static logger for this class
     *
     * @return
     */

    protected Log getLogger(){
        return logger;
    }

    /**
     * Reusing the DocQuery Audit Logger for logging DocQuery Deferred Service messages
     * 
     * @return
     */
    protected DocQueryAuditLog getDocQueryAuditLogger(){
        return new DocQueryAuditLog();
    }

    /**
     *
     * @param errorContext
     * @return DocRetrieveAcknowledgementType
     */
    private DocQueryAcknowledgementType createErrorAckResponse(String errorContext) {
        DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        ack.setMessage(responseType);
        responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryErrorList regErrList = new RegistryErrorList();
        responseType.setRegistryErrorList(regErrList);
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(errorContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return ack;
    }

    /**
     * This method uses Nhin Component proxy to call Nhin service
     *
     * @return
     */
    protected DocQueryAcknowledgementType callNhinDocQueryDeferredService(AdhocQueryRequest adhocQueryRequest,
                                                    AssertionType assertion, NhinTargetSystemType target){

        NhinDocQueryDeferredRequestProxyObjectFactory factory = new NhinDocQueryDeferredRequestProxyObjectFactory();
        NhinDocQueryDeferredRequestProxy proxy = factory.getNhinDocQueryDeferredRequestProxy();
        DocQueryAcknowledgementType respAck = proxy.respondingGatewayCrossGatewayQuery(adhocQueryRequest, assertion, target);

        return respAck;
    }

}
