/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseSecuredImpl {

    private static final Log logger = LogFactory.getLog(AdapterXDRResponseSecuredImpl.class);
    private static final String ACK_SUCCESS_MESSAGE = "SUCCESS";

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        // Log the Registry Response
        getLogger().debug("Registry Response " + body);

        XDRAcknowledgementType ack = new XDRAcknowledgementType();

        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return ack;
    }

    protected Log getLogger(){
        return logger;
    }

}
