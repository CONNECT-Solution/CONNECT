/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdapterXDRResponseImpl {
    private static final String ACK_SUCCESS_MESSAGE = "SUCCESS";
    private static Log log = null;

    public AdapterXDRResponseImpl()
    {
        log = createLogger();
    }
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType body) {
        log.debug("begin provideAndRegisterDocumentSetBResponse()");
        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();

        ack.setMessage(ACK_SUCCESS_MESSAGE);
        log.debug("end provideAndRegisterDocumentSetBResponse");

        return ack;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
