package gov.hhs.fha.nhinc.xdr.async.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhinXDRResponseImpl
{
    private static Log log = LogFactory.getLog(NhinXDRResponseImpl.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body)
    {
        log.debug("In NhinXDRResponseImpl for interface test helper service.");
        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();
        ack.setMessage("Success");
        return ack;
    }

}
