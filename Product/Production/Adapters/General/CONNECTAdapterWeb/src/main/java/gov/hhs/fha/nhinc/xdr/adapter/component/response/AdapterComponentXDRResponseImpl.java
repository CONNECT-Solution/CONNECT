/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter.component.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterComponentXDRResponseImpl {

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(AdapterRegistryResponseType body, WebServiceContext context) {
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        response.setMessage(regResp);

        return response;
    }

}
