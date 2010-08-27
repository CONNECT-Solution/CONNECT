package gov.hhs.fha.nhinc.docsubmission.adapter.component.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AdapterComponentDocSubmissionProxyNoOpImpl implements AdapterComponentDocSubmissionProxy
{
    private static Log log = LogFactory.getLog(AdapterComponentDocSubmissionProxyNoOpImpl.class);
    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion) {
        log.debug("Using NoOp Implementation for Adapter Doc Submission Service");
        return new RegistryResponseType();
    }
}
