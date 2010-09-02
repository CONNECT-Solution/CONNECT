/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.nhindocretrieve;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrieve;
import gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.util.AssertionCreator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Neil Webb
 */
public class DocRetrieveClient
{
    private static final String ENDPOINT_NHIN_PROXY = "http://localhost:8080/CONNECTMsgProxyWeb/NhincProxyDocRetrieve";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocRetrieveClient.class);
    private static NhincProxyDocRetrieve service = null;

    public DocRetrieveClient()
    {
        if(service == null)
        {
            service = createService();
        }
    }

    protected NhincProxyDocRetrieve createService()
    {
        return new NhincProxyDocRetrieve();
    }

    public String retrieveDocument(String url, String homeCommunityId, String repositoryId, String documentId)
    {
        log.info("Attempting document retrieve for id (" + documentId + ") using URL: " + url);
        String status = null;
        try
        {
            NhincProxyDocRetrievePortType port = getPort(ENDPOINT_NHIN_PROXY);
            RespondingGatewayCrossGatewayRetrieveRequestType request = createRequest(url, homeCommunityId, repositoryId, documentId);
            long lBefore = System.currentTimeMillis();
            RetrieveDocumentSetResponseType result = port.respondingGatewayCrossGatewayRetrieve(request);
            long lAfter = System.currentTimeMillis();
            if((result != null) && (result.getDocumentResponse() != null) && (!result.getDocumentResponse().isEmpty()))
            {
                String retrievedDocId = result.getDocumentResponse().get(0).getDocumentUniqueId();
                if(documentId.equals(retrievedDocId))
                {
                    status = "Success";
                    int bytes = result.getDocumentResponse().get(0).getDocument().length;
                    double mb = 0;
                    double bytesPerMB = 1048576d;
                    if(bytes > 0)
                    {
                        mb = ((double)bytes)/bytesPerMB;
                    }
                    log.info("Time to retrieve " + documentId + " (" + mb + " MB) : " + (lAfter - lBefore) + " MS");
                }
                else
                {
                    log.warn("The document id retrieved was not " + documentId + ", was: " + retrievedDocId);
                    status = "Incorrect document id";
                }
            }
            else
            {
                log.warn("Result was invalid for " + documentId);
                status = "Invalid result";
            }
        }
        catch (Throwable t)
        {
            log.error("Failed to retrieve document (" + documentId + ") using URL: " + url + ". Error: " + t.getMessage(), t);
            status = "Exception";
        }
        return status;
    }

    protected NhincProxyDocRetrievePortType getPort(String url)
    {
        NhincProxyDocRetrievePortType port = service.getNhincProxyDocRetrievePortSoap();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        return port;
    }

    protected RespondingGatewayCrossGatewayRetrieveRequestType createRequest(String url, String homeCommunityId, String repositoryId, String documentId)
    {
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();

        // Assertion
        AssertionType assertion = new AssertionCreator().createAssertion();
        request.setAssertion(assertion);

        // Target System
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setUrl(url);
        request.setNhinTargetSystem(targetSystem);

        // Doc Retrieve
        RetrieveDocumentSetRequestType retrieveDocSetRequest = new RetrieveDocumentSetRequestType();
        DocumentRequest docRequest = new DocumentRequest();
        docRequest.setDocumentUniqueId(documentId);
        docRequest.setRepositoryUniqueId(repositoryId);
        docRequest.setHomeCommunityId(homeCommunityId);
        retrieveDocSetRequest.getDocumentRequest().add(docRequest);
        request.setRetrieveDocumentSetRequest(retrieveDocSetRequest);

        return request;
    }

    public static void main(String[] args)
    {
        try
        {
            String url = "https://localhost:8181/CONNECTNhinServicesWeb/NhinService/RespondingGateway_Retrieve_Service/DocRetrieve";
            String homeCommunityId = "2.2";
            String repositoryId = "1";
            String documentId = "19MBFile";
            DocRetrieveClient docRetrieve = new DocRetrieveClient();
            String status = docRetrieve.retrieveDocument(url, homeCommunityId, repositoryId, documentId);
            System.out.println("Status: " + status);
        }
        catch(Throwable t)
        {
            System.out.println("Exception: " + t.getMessage());
            t.printStackTrace();
        }
    }
}
