package gov.hhs.fha.nhinc.xdr.async.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRRequestImpl
{
    private static Log log = LogFactory.getLog(NhinXDRRequestImpl.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body)
    {
        log.debug("In NhinXDRRequestImpl for interface test helper service.");
        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();
        ack.setMessage("Success");
        return ack;
    }

}
