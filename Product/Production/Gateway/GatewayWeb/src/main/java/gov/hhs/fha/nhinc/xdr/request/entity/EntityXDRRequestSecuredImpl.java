package gov.hhs.fha.nhinc.xdr.request.entity;

import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestObjectFactory;
import gov.hhs.fha.nhinc.xdr.async.request.proxy.NhinXDRRequestProxy;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestSecuredImpl
{
    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, WebServiceContext context)
    {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        return provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest, assertion);
    }

    private AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        AcknowledgementType response = null;
        // TODO: Log input message

        if(checkPolicy(provideAndRegisterRequestRequest, assertion))
        {
            NhinXDRRequestObjectFactory factory = new NhinXDRRequestObjectFactory();
            NhinXDRRequestProxy proxy = factory.getNhinXDRRequestProxy();

            response = proxy.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest(), assertion, provideAndRegisterRequestRequest.getNhinTargetSystem());
        }
        else
        {
            response = new AcknowledgementType();
            response.setMessage("Policy rejection");
        }
        
        return response;
    }

    private boolean checkPolicy(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestRequest, AssertionType assertion)
    {
        // TODO: Policy check
        return true;
    }

}
