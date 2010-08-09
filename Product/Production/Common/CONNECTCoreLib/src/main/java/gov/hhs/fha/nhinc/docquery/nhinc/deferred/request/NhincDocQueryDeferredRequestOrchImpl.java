package gov.hhs.fha.nhinc.docquery.nhinc.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;


/**
 * This implementation class contains the flow
 *
 * @author patlollav
 */
public class NhincDocQueryDeferredRequestOrchImpl {

    //Logger
    private static final Log logger = LogFactory.getLog(NhincDocQueryDeferredRequestOrchImpl.class);


    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest,
                                            WebServiceContext context) {

        if (getLogger().isDebugEnabled()){
            getLogger().debug("Beginning of NhincDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");
        }

        AssertionType assertion = null;
        AcknowledgementType ack = null;
        DocQueryAcknowledgementType docQueryAcknowledgement = null;

        if (crossGatewayQueryRequest == null) {
            getLogger().error("RespondingGatewayCrossGatewayQueryRequestType is null");
            return createErrorAckResponse("RespondingGatewayCrossGatewayQueryRequestType is null");
        }

        if (context == null) {
            getLogger().error("WebServiceContext is null");
            return createErrorAckResponse("WebServiceContext is null");
        }

        assertion = crossGatewayQueryRequest.getAssertion();

        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
        }else{
            getLogger().error("Assertion is null");
            return createErrorAckResponse("Assertion is null");
        }

        // Log the incoming request -- Audit Logging
        ack = getDocQueryAuditLogger().auditDQRequest(crossGatewayQueryRequest.getAdhocQueryRequest(), assertion,
                              NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        if (ack != null) {
           getLogger().debug("DocQueryDeferred Request Audit Log Acknowledgement " + ack.getMessage());
        }

        //Call Nhin component proxy
        docQueryAcknowledgement = callNhinDocQueryDeferredService(crossGatewayQueryRequest.getAdhocQueryRequest(),
                                                                   assertion, crossGatewayQueryRequest.getNhinTargetSystem());

        if (getLogger().isDebugEnabled()){
            getLogger().debug("End of NhincDocQueryDeferredRequestOrchImpl.crossGatewayQueryRequest");
        }
        
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
        return new DocQueryAcknowledgementType();
    }

}
