/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocrepository.test;

import gov.hhs.fha.nhinc.adapterdocrepository.AdapterDocRepository2Soap12Client;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shawc
 */
public class AdapterDocRepository2Soap12ClientTest
{
    public AdapterDocRepository2Soap12ClientTest()
    {

    }

        @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of AdapterDocRepository2Soap12Client.retrieveDocument method.
     */

    @Test
    public void testAdapterDocRepository2Soap12Client()
    {
        System.out.println("Begin testAdapterDocRepository2Soap12Client");

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = null;
        AdapterDocRepository2Soap12Client oClient = new AdapterDocRepository2Soap12Client();
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType oRetrieveRequest =
                new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();

        DocumentRequest oDocRequest = new DocumentRequest();

        String sHomeCommunityId = "urn:oid:2.16.840.1.113883.3.200";
        String sRepositoryId = "1";
        String sDocumentId = "129.6.58.92.2029492";
//        String sHL7PatientId = "";

        oDocRequest.setHomeCommunityId(sHomeCommunityId);
        oDocRequest.setRepositoryUniqueId(sRepositoryId);
        oDocRequest.setDocumentUniqueId(sDocumentId);

        oRetrieveRequest.getDocumentRequest().add(oDocRequest);

        response = oClient.retrieveDocument(oRetrieveRequest);
        assertNotNull(response);
        if("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure".equalsIgnoreCase(response.getRegistryResponse().getStatus()))
        {
            System.out.println("failed to retrieve document. Failure reason: " +
                    response.getRegistryResponse().getRegistryErrorList().getRegistryError().get(0).getCodeContext());
            System.out.println("failed to retrieve document. Failure reason: " +
                    response.getRegistryResponse().getRegistryErrorList().getRegistryError().get(0).getErrorCode());
            System.out.println("failed to retrieve document. Failure reason: " +
                    response.getRegistryResponse().getRegistryErrorList().getRegistryError().get(0).getLocation());
            System.out.println("failed to retrieve document. Failure reason: " +
                    response.getRegistryResponse().getRegistryErrorList().getRegistryError().get(0).getSeverity());
            System.out.println("failed to retrieve document. Failure reason: " +
                    response.getRegistryResponse().getRegistryErrorList().getRegistryError().get(0).getValue());
        }
        assertNotNull(response.getDocumentResponse());
        if (response.getDocumentResponse().get(0) != null)
        {
            System.out.println("Response document uniqueId: " + response.getDocumentResponse().get(0).getRepositoryUniqueId());
            System.out.println("Response homeCommunityId: " + response.getDocumentResponse().get(0).getHomeCommunityId());
            System.out.println("Response document mimetype: " + response.getDocumentResponse().get(0).getMimeType());
            System.out.println("Response document byte array: " + response.getDocumentResponse().get(0).getDocument());
        }
        assertEquals(null, sDocumentId, response.getDocumentResponse().get(0).getDocumentUniqueId());

        System.out.println("End testAdapterDocRepository2Soap12Client");
    }
}
