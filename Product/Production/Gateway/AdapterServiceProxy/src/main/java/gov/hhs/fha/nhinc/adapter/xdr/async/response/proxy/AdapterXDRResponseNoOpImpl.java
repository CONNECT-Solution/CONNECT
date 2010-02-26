package gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
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

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion) {
        getLogger().debug("Entering AdapterXDRResponseNoOpImpl");

        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();
        ack.setMessage(SUCCESS_ACK);

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
