package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author JHOPPESC
 */
public interface AdapterDocSubmissionDeferredRequestErrorProxy
{
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(ProvideAndRegisterDocumentSetRequestType request, String errorMessage, AssertionType assertion);
}
