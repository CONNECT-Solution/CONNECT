package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author patlollav
 */
public interface AdapterDocSubmissionDeferredRequestProxy
{
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, String liftURL, AssertionType assertion);
}
