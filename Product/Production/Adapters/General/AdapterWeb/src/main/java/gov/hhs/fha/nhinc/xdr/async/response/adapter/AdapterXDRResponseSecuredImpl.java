/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

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

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        // Log the Registry Response
        getLogger().debug("Registry Response " + body);

        ihe.iti.xdr._2007.AcknowledgementType ack = new ihe.iti.xdr._2007.AcknowledgementType();
        ack.setMessage("SUCCESS");

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return ack;
    }

    protected Log getLogger(){
        return logger;
    }

}
