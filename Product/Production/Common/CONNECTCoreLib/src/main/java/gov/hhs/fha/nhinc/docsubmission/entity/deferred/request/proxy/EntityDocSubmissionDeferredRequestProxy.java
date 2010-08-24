/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity.deferred.request.proxy;


import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

/**
 *
 * @author jhoppesc
 */
public interface EntityDocSubmissionDeferredRequestProxy {
    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncRequest(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo);
}
