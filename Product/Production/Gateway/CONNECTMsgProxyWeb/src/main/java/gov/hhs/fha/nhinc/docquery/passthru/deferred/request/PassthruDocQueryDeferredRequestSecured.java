/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
@WebService(serviceName = "NhincProxyDocQueryDeferredRequestSecured", portName = "NhincProxyDocQueryDeferredRequestSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerydeferredrequestsecured.NhincProxyDocQueryDeferredRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequestsecured", wsdlLocation = "WEB-INF/wsdl/PassthruDocQueryDeferredRequestSecured/NhincProxyDocQueryDeferredRequestSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PassthruDocQueryDeferredRequestSecured {

    @Resource
    private WebServiceContext context;

    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestSecured.class);


    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType crossGatewayQueryRequest) {
        getLogger().debug("Beginning of PassthruDocQueryDeferredRequestSecured.crossGatewayQueryRequest()");

        AssertionType assertion = extractAssertion();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null){
            assertion.setMessageId(getAsyncMessageIdExtractor().GetAsyncMessageId(context));
        }

        // Fwd request to orchestrator
        DocQueryAcknowledgementType ackResponse = getPassthruDocQueryDeferredRequestOrchImpl().crossGatewayQueryRequest(crossGatewayQueryRequest.getAdhocQueryRequest(),
                                                                            assertion, crossGatewayQueryRequest.getNhinTargetSystem());

        getLogger().debug("End of PassthruDocQueryDeferredRequestSecured.crossGatewayQueryRequest()");

        return ackResponse;
    }

    /**
     * Returns the static logger for this class
     * @return
     */
    protected Log getLogger(){
        return logger;
    }

    /**
     * Implementation class for
     *
     * @return
     */
    protected PassthruDocQueryDeferredRequestOrchImpl getPassthruDocQueryDeferredRequestOrchImpl(){
        return new PassthruDocQueryDeferredRequestOrchImpl();
    }


    protected AssertionType extractAssertion(){
        return SamlTokenExtractor.GetAssertion(context);
    }


    protected AsyncMessageIdExtractor getAsyncMessageIdExtractor(){
        return new AsyncMessageIdExtractor();
    }
}
