package gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy;

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
public class AdapterXDRResponseNoOpImpl implements AdapterXDRResponseProxy
{
    private static final Log logger = LogFactory.getLog(AdapterXDRResponseNoOpImpl.class);
    private final String SUCCESS_ACK = "SUCCESS";

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion) {
        getLogger().debug("Entering AdapterXDRResponseNoOpImpl");

        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        getLogger().debug("Exiting AdapterXDRResponseNoOpImpl");
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
