/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery.adapter.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterDocQueryDeferredResponseSecured", portName = "AdapterDocQueryDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredresponsesecured.AdapterDocQueryDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterDocQueryDeferredResponseSecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredResponseSecured/AdapterDocQueryDeferredResponseSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredResponseSecured
{

    @Resource
    private WebServiceContext context;
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocQueryDeferredResponseSecured.class);

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQuerySecureResponseType respondingGatewayCrossGatewayQueryRequest)
    {
        WebServiceHelper oHelper = new WebServiceHelper();
        AdapterDocQueryDeferredResponseOrchImpl orchImpl = new AdapterDocQueryDeferredResponseOrchImpl();
        DocQueryAcknowledgementType response = null;

        try
        {
            if (respondingGatewayCrossGatewayQueryRequest != null && orchImpl != null)
            {
                response = (DocQueryAcknowledgementType) oHelper.invokeSecureDeferredResponseWebService(orchImpl,orchImpl.getClass(), "respondingGatewayCrossGatewayQuery", respondingGatewayCrossGatewayQueryRequest, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + orchImpl.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + orchImpl.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }
}
