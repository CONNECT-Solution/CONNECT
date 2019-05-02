/**
 *
 */
package gov.hhs.fha.nhinc.docquery.deferred.impl;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDeferredResponseOptionQuerySecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDeferredResponseOptionQueryType;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.DeferredXCARequestDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DeferredXCARequest;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 * @author ptambellini
 *
 */
public class AdapterDeferredResponseOptionImpl {

    public AdapterDeferredResponseOptionQueryType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg,
        AssertionType assertion) {

        DeferredXCARequest deferredXCAReq = new DeferredXCARequest();
        String defEndpoint = assertion.getDeferredResponseEndpoint();
        String newId = new WSAHeaderHelper().generateMessageID();
        String dcAdHocQueryRequestId = msg.getId();
        deferredXCAReq.setAdHocQueryRequestId(newId);
        deferredXCAReq.setDeferredResponseEndpoint(defEndpoint);
        deferredXCAReq.setDcAdHocQueryRequestId(dcAdHocQueryRequestId);
        DeferredXCARequestDao defXCAReqDao = new DeferredXCARequestDao();
        defXCAReqDao.save(deferredXCAReq);
        AdapterDeferredResponseOptionQueryType response = new AdapterDeferredResponseOptionQueryType();
        response.setNewId(newId);
        return response;
    }

    public AdapterDeferredResponseOptionQuerySecuredType respondingGatewayCrossGatewayQuerySecured(
        AdhocQueryRequest msg,
        AssertionType assertion) {

        DeferredXCARequest deferredXCAReq = new DeferredXCARequest();
        String defEndpoint = assertion.getDeferredResponseEndpoint();
        String newId = new WSAHeaderHelper().generateMessageID();
        String dcAdHocQueryRequestId = msg.getId();
        deferredXCAReq.setAdHocQueryRequestId(newId);
        deferredXCAReq.setDeferredResponseEndpoint(defEndpoint);
        deferredXCAReq.setDcAdHocQueryRequestId(dcAdHocQueryRequestId);
        DeferredXCARequestDao defXCAReqDao = new DeferredXCARequestDao();
        defXCAReqDao.save(deferredXCAReq);
        AdapterDeferredResponseOptionQuerySecuredType response = new AdapterDeferredResponseOptionQuerySecuredType();
        response.setNewId(newId);
        return response;
    }
}
