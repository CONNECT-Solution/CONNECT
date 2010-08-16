/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Ralph Saunders
 */
public class AdapterDocRetrieveDeferredRespOrchImpl {
    private Log log = null;

    public AdapterDocRetrieveDeferredRespOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetResponseType body, AssertionType assertion)
    {
        log.debug("Enter AdapterDocRetrieveDeferredRespImpl.respondingGatewayCrossGatewayRetrieve()");
        DocRetrieveAcknowledgementType response = null;
        RegistryResponseType           responseType;

        response = new DocRetrieveAcknowledgementType();
        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        responseType.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_RESP_ACK_STATUS_MSG);

        log.debug("Leaving AdapterDocRetrieveDeferredRespImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

}
