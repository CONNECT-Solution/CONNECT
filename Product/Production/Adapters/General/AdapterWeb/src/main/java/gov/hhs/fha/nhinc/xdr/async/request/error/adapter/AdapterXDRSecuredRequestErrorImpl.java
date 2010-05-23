/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request.error.adapter;

import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 *
 * @author JHOPPESC
 */
public class AdapterXDRSecuredRequestErrorImpl {

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(AdapterProvideAndRegisterDocumentSetRequestErrorSecuredType body) {
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        return ack;
    }

}
