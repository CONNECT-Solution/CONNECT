package gov.hhs.fha.nhinc.xdr.response.entity;

import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseObjectFactory;
import gov.hhs.fha.nhinc.xdr.async.response.proxy.NhinXDRResponseProxy;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRResponseSecuredImpl
{
    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest, WebServiceContext context)
    {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        return provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredResponseRequest, assertion);
    }

    private AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType provideAndRegisterDocumentSetSecuredResponseRequest, AssertionType assertion)
    {
        //TODO Correct business logic
        NhinXDRResponseObjectFactory factory = new NhinXDRResponseObjectFactory();
        NhinXDRResponseProxy proxy = factory.getNhinXDRResponseProxy();

        return proxy.provideAndRegisterDocumentSetBResponse(provideAndRegisterDocumentSetSecuredResponseRequest.getRegistryResponse(), assertion, provideAndRegisterDocumentSetSecuredResponseRequest.getNhinTargetSystem());
    }
}
