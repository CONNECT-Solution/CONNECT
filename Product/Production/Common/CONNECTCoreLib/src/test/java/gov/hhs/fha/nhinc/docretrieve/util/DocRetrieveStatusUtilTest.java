/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * @author achidambaram
 *
 */
public class DocRetrieveStatusUtilTest {
    
   @Test
   public void setResponseStatusSuccessCase() {
       DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
       String status = "Success";
       String returnedStatus = null;
       returnedStatus = util.setResponseStatus(createFromRequestSuccessCase(), createToRequestSucessCase());
       assertEquals(returnedStatus, status);
   }
   
   @Test
   public void setResponseStatusFailureCase() {
       DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
       String status = "Failure";
       String returnedStatus = null;
       returnedStatus = util.setResponseStatus(createFromRequestFailureCase(), createToRequestFailureCase());
       assertEquals(returnedStatus, status);
   }
   
   @Test
   public void setResponseStatusPartialSuccessCase() {
       DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
       String status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_PARTIALSUCCESS;
       String returnedStatus = null;
       returnedStatus = util.setResponseStatus(createFromRequestSuccessCase(), createToRequestFailureCase());
       assertEquals(returnedStatus, status);
   }
   
   @Test
   public void testIsStatusSuccess() {
       String status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS;
       DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
       assertTrue(util.isStatusSuccess(status));
   }
   
   @Test
   public void testIsStatusFailureOrPartialFailure() {
       String status = DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE;
       DocRetrieveStatusUtil util = new DocRetrieveStatusUtil();
       assertFalse(util.isStatusSuccess(status));
   }
    
    private RetrieveDocumentSetResponseType createFromRequestSuccessCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Success");
        response.setRegistryResponse(res);
        return response;
    }
    
    private RetrieveDocumentSetResponseType createFromRequestFailureCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Failure");
        response.setRegistryResponse(res);
        return response;
    }
    
    private RetrieveDocumentSetResponseType createToRequestFailureCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Failure");
        response.setRegistryResponse(res);
        return response;
    }
    
    private RetrieveDocumentSetResponseType createToRequestSucessCase() {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        RegistryResponseType res = new RegistryResponseType();
        res.setStatus("Success");
        response.setRegistryResponse(res);
        return response;
    }
    
   

}
