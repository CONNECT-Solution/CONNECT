package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseObjectFactory;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseProxy;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRResponseSecuredImpl
{
    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, WebServiceContext context)
    {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        return provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest, assertion);
    }

    private AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterResponseRequest, AssertionType assertion)
    {
        NhinXDRResponseObjectFactory factory = new NhinXDRResponseObjectFactory();
        NhinXDRResponseProxy proxy = factory.getNhinXDRResponseProxy();

        return proxy.provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest.getRegistryResponse(), assertion, provideAndRegisterResponseRequest.getNhinTargetSystem());
    }
}
