/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.response;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredRespSecuredImpl {
    private Log log = null;

    public AdapterDocRetrieveDeferredRespSecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, WebServiceContext context)
    {
        log.debug("Enter AdapterDocRetrieveDeferredRespSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        DocRetrieveAcknowledgementType response = null;
        RegistryResponseType           responseType;


        response = new DocRetrieveAcknowledgementType();
        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus("Success");

        log.debug("Leaving AdapterDocRetrieveDeferredRespSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

}
