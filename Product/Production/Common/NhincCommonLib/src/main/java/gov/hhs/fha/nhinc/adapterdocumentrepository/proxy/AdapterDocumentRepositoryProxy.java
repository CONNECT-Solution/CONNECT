/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocumentrepository.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 *
 * @author rayj
 */
public interface AdapterDocumentRepositoryProxy {
    public RetrieveDocumentSetResponseType retrieveDocument(RetrieveDocumentSetRequestType retrieveDocumentSetRequestType, AssertionType assertion, NhinTargetSystemType target) throws Exception ;

}
