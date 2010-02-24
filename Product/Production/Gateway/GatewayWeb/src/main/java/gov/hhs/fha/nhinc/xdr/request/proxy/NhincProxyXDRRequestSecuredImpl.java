package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestObjectFactory;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestProxy;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xdr._2007.AcknowledgementType;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestSecuredImpl
{
    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context)
    {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        return provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, assertion);
    }

    private AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        NhinXDRRequestObjectFactory factory = new NhinXDRRequestObjectFactory();
        NhinXDRRequestProxy proxy = factory.getNhinXDRRequestProxy();

        return proxy.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());
    }
}
