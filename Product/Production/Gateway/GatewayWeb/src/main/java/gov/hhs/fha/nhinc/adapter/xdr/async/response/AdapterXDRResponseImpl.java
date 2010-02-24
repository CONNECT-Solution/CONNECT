/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import ihe.iti.xdr._2007.AcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseImpl {
    private static final Log logger = LogFactory.getLog(AdapterXDRResponseImpl.class);

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(AdapterRegistryResponseType body) {
        //TODO implement this method
        logger.debug("In AdapterXDRResponseImpl");

        return new AcknowledgementType();
    }

}
