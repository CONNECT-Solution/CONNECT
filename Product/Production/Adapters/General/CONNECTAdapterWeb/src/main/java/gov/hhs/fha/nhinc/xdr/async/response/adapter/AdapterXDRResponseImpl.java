/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterXDRResponseImpl {
    private static Log log = null;

    public AdapterXDRResponseImpl()
    {
        log = createLogger();
    }
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType body) {
        log.debug("begin provideAndRegisterDocumentSetBResponse()");
        XDRAcknowledgementType ack = new XDRAcknowledgementType();

        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        log.debug("end provideAndRegisterDocumentSetBResponse");

        return ack;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
