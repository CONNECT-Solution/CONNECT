/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docrepository.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author rayj
 */
public interface AdapterComponentDocRepositoryProxy {
    public RetrieveDocumentSetResponseType retrieveDocument(RetrieveDocumentSetRequestType retrieveDocumentSetRequestType, AssertionType assertion);

}
