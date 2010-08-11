/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.request;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredReqImpl {
    private Log log = null;

    public AdapterDocRetrieveDeferredReqImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RespondingGatewayCrossGatewayRetrieveRequestType body, WebServiceContext context)
    {
        log.debug("Enter AdapterDocRetrieveDeferredReqImpl.respondingGatewayCrossGatewayRetrieve()");
        DocRetrieveAcknowledgementType response = null;
        RegistryResponseType           responseType;


        response = new DocRetrieveAcknowledgementType();
        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus("Success");

        log.debug("Leaving AdapterDocRetrieveDeferredReqImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

}
