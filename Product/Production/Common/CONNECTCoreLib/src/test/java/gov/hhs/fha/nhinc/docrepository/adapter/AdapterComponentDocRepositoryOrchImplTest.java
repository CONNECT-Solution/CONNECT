/**
 * 
 */
package gov.hhs.fha.nhinc.docrepository.adapter;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.junit.Test;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

/**
 * @author achidamb
 *
 */
public class AdapterComponentDocRepositoryOrchImplTest {
    
    @Test
    public void documentRepositoryRetrieveDocumentSetTest_Success()
    {
        AdapterComponentDocRepositoryOrchImpl docrepo= new AdapterComponentDocRepositoryOrchImpl(){
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String>documentUniqueIds,ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
                    String homeCommunityId, RegistryErrorList regerrList){
            }
           
        };
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_Success());
        assertSame(response.getRegistryResponse().getStatus(), "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
      
    }
    
    @Test
    public void documentRepositoryRetrieveDocumentSetTest_Failure()
    {
        AdapterComponentDocRepositoryOrchImpl docrepo= new AdapterComponentDocRepositoryOrchImpl(){
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String>documentUniqueIds,ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
                    String homeCommunityId, RegistryErrorList regerrList){
            }
           
        };
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_Failure());
        assertSame(response.getRegistryResponse().getStatus(), "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
      
    }
    
    @Test
    public void documentRepositoryRetrieveDocumentSetTest_PartialSuccess()
    {
        AdapterComponentDocRepositoryOrchImpl docrepo= new AdapterComponentDocRepositoryOrchImpl(){
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String>documentUniqueIds,ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
                    String homeCommunityId, RegistryErrorList regerrList){
                Document doc1 = new Document();
                doc1.setDocumentUniqueId("2.531.777");
                Document doc2 = new Document();
                doc2.setDocumentUniqueId("");
                ArrayList<Document> docs = new ArrayList<Document>();
                docs.add(doc1);
                docs.add(doc2);
                loadDocumentResponses(response, docs, homeCommunityId,documentUniqueIds,regerrList);
            }
            @Override
            protected boolean setDocumentResponse( Document doc, DocumentResponse oDocResponse){
                return true;
            }
           
        };
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_PartialSuccess());
        assertSame(response.getRegistryResponse().getStatus(), "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:PartialSuccess");
      
    }
    
    
    protected ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType createDocumentRequest_Success()
    {
        
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType requestBody = new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("1.546.678");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        DocumentRequest req2 = new DocumentRequest();
        req2.setDocumentUniqueId("2.531.777");
        req2.setHomeCommunityId("1.1");
        req2.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req2);
        return requestBody;
        
    }
    
    protected ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType createDocumentRequest_Failure(){
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType requestBody = new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        return requestBody;
    }
    
    protected ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType createDocumentRequest_PartialSuccess(){
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType requestBody = new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        DocumentRequest req2 = new DocumentRequest();
        req2.setDocumentUniqueId("2.531.777");
        req2.setHomeCommunityId("1.1");
        req2.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req2);
        return requestBody;
    }

}
