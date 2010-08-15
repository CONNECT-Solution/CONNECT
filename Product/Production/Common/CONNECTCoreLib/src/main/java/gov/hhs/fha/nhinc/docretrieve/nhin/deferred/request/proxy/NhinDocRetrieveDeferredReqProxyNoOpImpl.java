package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;


/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:56:00 PM
 */
public class NhinDocRetrieveDeferredReqProxyNoOpImpl implements NhinDocRetrieveDeferredReqProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RetrieveDocumentSetRequestType body, AssertionType assertion, NhinTargetSystemType target) {
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG);
        ack.setMessage(resp);
        return ack;
    }
}
