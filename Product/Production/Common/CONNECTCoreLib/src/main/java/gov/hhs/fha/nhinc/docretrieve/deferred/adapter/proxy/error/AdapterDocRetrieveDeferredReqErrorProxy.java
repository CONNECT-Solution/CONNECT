package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.error;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:33:52 PM
 */
public interface AdapterDocRetrieveDeferredReqErrorProxy {

    public DocRetrieveAcknowledgementType sendToAdapter(AdapterDocumentRetrieveDeferredRequestErrorSecuredType body,
                                                        AssertionType assertion);
}
