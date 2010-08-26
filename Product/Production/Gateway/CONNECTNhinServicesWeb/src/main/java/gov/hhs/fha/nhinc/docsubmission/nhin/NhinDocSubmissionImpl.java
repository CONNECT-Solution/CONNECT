package gov.hhs.fha.nhinc.docsubmission.nhin;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author dunnek
 */
public class NhinDocSubmissionImpl
{
    
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body,WebServiceContext context ) {
       AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

       if (assertion != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }
       return new NhinDocSubmissionOrchImpl().documentRepositoryProvideAndRegisterDocumentSetB(body, assertion);

    }

    
}
