package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author patlollav
 */
public interface AdapterDocSubmissionDeferredResponseProxy
{
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType regResponse, AssertionType assertion);
}
