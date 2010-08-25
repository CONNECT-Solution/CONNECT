package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 * Spring Proxy interface for deferred processing of Doc Submission request.
 *
 * @author Les Westberg
 */
public interface AdapterComponentDocSubmissionRequestProxy
{

    /**
     * Receive document deferred document submission request.
     *
     * @param body The doc submission message.
     * @param assertion The assertion information.
     * @param url The URL if using LiFT.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String url);
}
