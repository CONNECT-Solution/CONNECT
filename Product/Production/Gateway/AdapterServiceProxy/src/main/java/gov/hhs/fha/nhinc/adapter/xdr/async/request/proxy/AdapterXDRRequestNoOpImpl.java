package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AdapterXDRRequestNoOpImpl
{
    private static final Log logger = LogFactory.getLog(AdapterXDRRequestNoOpImpl.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        logger.debug("Entering AdapterXDRRequestNoOpImpl");

        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();

        logger.debug("Exiting AdapterXDRRequestNoOpImpl");
        return ack;
    }
}
