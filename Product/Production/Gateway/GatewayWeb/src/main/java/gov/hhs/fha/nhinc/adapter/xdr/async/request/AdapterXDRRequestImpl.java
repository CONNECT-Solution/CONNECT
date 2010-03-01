/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xdr._2007.AcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRRequestImpl.class);

    public AcknowledgementType provideAndRegisterDocumentSetBRequest(AdapterProvideAndRegisterDocumentSetRequestType body) {
        logger.debug("In AdapterXDRRequestImpl");
        //TODO implement this method
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("SUCCESS");

        return ack;
    }
}
