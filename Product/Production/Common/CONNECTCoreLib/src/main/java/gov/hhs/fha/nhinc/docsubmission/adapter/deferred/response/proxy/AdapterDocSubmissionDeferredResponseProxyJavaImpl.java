package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.AdapterDocSubmissionDeferredResponseOrchImpl;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionDeferredResponseProxyJavaImpl implements AdapterDocSubmissionDeferredResponseProxy
{
    private Log log = null;

    public AdapterDocSubmissionDeferredResponseProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType regResponse, AssertionType assertion)
    {
        log.debug("Begin AdapterDocSubmissionDeferredResponseProxyJavaImpl.provideAndRegisterDocumentSetBResponse");
        XDRAcknowledgementType ack = new AdapterDocSubmissionDeferredResponseOrchImpl().provideAndRegisterDocumentSetBResponse(regResponse, assertion);
        log.debug("End AdapterDocSubmissionDeferredResponseProxyJavaImpl.provideAndRegisterDocumentSetBResponse");
        return ack;
    }
}
