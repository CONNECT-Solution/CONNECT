/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.error;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredReqErrorSecuredImpl {
    private Log log = null;

    public AdapterDocRetrieveDeferredReqErrorSecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(AdapterDocumentRetrieveDeferredRequestErrorSecuredType body, WebServiceContext context)
    {
        log.debug("Enter AdapterDocRetrieveDeferredReqErrorSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        DocRetrieveAcknowledgementType response = null;
        RegistryResponseType           responseType;


        response = new DocRetrieveAcknowledgementType();
        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus("Success");

        log.debug("Leaving AdapterDocRetrieveDeferredReqErrorSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

}
