/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocQueryDeferredResponse", portName = "AdapterDocQueryDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredresponse.AdapterDocQueryDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredResponseUnsecured/AdapterDocQueryDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;
private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryDeferredResponseUnsecured.class);

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryRequest) {
        WebServiceHelper oHelper = new WebServiceHelper();
        AdapterDocQueryDeferredResponseOrchImpl orchImpl = new AdapterDocQueryDeferredResponseOrchImpl();
        DocQueryAcknowledgementType ack = null;
        try
        {
            if (respondingGatewayCrossGatewayQueryRequest != null && orchImpl != null)
            {
                ack = (DocQueryAcknowledgementType) oHelper.invokeDeferredResponseWebService(orchImpl, orchImpl.getClass(), "respondingGatewayCrossGatewayQuery", respondingGatewayCrossGatewayQueryRequest.getAssertion(), respondingGatewayCrossGatewayQueryRequest.getAdhocQueryResponse(), context);
            } else
            {
                log.error("Failed to call the web orchestration (" + orchImpl.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + orchImpl.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }


        return ack;
    }

}
