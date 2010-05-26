package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author patlollav
 */
public class AdapterXDRRequestNoOpImpl implements AdapterXDRRequestProxy
{
    private static final Log logger = LogFactory.getLog(AdapterXDRRequestNoOpImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType body, AssertionType assertion) {
        getLogger().debug("Entering AdapterXDRRequestNoOpImpl");

        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        getLogger().debug("Exiting AdapterXDRRequestNoOpImpl");
        return ack;
    }

    /**
     * 
     * @return
     */
    protected Log getLogger(){
        return logger;
    }
}
