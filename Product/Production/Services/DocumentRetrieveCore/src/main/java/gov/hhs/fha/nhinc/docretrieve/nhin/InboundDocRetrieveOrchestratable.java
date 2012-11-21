/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.InboundOrchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * 
 * @author mweaver
 */
public interface InboundDocRetrieveOrchestratable extends InboundOrchestratable {
   
    /**
     *  Get the request.
     * @return RetrieveDocumentSetRequestType
     */
    public RetrieveDocumentSetRequestType getRequest();

    /**
     * Get the response.
     * @return RetrieveDocumentSetResponseType
     */
    public RetrieveDocumentSetResponseType getResponse();

    public void setResponse(RetrieveDocumentSetResponseType response);

    public void setRequest(RetrieveDocumentSetRequestType request);
  
    public void setAssertion(AssertionType assertion);
     
}
