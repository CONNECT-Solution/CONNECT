package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:41:47 PM
 */
public interface AdapterDocRetrieveDeferredRespProxy {

    public DocRetrieveAcknowledgementType sendToAdapter(RetrieveDocumentSetResponseType body, AssertionType assertion);
}
