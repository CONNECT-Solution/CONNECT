/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docsubmission.passthru.deferred.response.PassthruDocSubmissionDeferredResponseOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class PassthruDocSubmissionDeferredResponseProxyJavaImpl implements PassthruDocSubmissionDeferredResponseProxy {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(PassthruDocSubmissionDeferredResponseProxyJavaImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Using Java Implementation for Passthru Doc Submission Deferred Response Service");
        return new PassthruDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(request, assertion, targetSystem);
    }

}
