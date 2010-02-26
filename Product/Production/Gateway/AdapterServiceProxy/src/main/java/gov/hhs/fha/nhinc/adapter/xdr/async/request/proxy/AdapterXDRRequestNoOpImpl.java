package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author patlollav
 */
public class AdapterXDRRequestNoOpImpl implements AdapterXDRRequestProxy
{
    private static final Log logger = LogFactory.getLog(AdapterXDRRequestNoOpImpl.class);
    private final String SUCCESS_ACK = "SUCCESS";

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        getLogger().debug("Entering AdapterXDRRequestNoOpImpl");

        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();
        ack.setMessage(SUCCESS_ACK);

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
