package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.proxy.NhincProxyDocRetrieveSecuredImpl;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocRetrieveAggregator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;

/**
 *
 *
 * @author Neil Webb
 */
public class DocRetrieveSender extends Thread
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocRetrieveSender.class);

    String transactionId = null;
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType request = null;
            WebServiceContext context = null;

    public DocRetrieveSender(String transactionId, RespondingGatewayCrossGatewayRetrieveSecuredRequestType request, WebServiceContext context)
    {
        this.transactionId = transactionId;
        this.request = request;
        this.context = context;
    }

    @Override
    public void run()
    {
        log.debug("Begin DocRetrieveSender.run");
        NhincProxyDocRetrieveSecuredImpl docRetrieveProxy = new NhincProxyDocRetrieveSecuredImpl();

        // Collect identifying attributes
        DocumentRequest docRequest = null;
        String documentUniqueId = null;
        String repositoryUniqueId = null;
        String homeCommunityId = null;
        if((request != null) && (request.getRetrieveDocumentSetRequest() != null) && (!request.getRetrieveDocumentSetRequest().getDocumentRequest().isEmpty()))
        {
            docRequest = request.getRetrieveDocumentSetRequest().getDocumentRequest().get(0);
            if(docRequest != null)
            {
                documentUniqueId = docRequest.getDocumentUniqueId();
                repositoryUniqueId = docRequest.getRepositoryUniqueId();
                homeCommunityId = docRequest.getHomeCommunityId();
            }
        }

        // Call NHIN proxy
        RetrieveDocumentSetResponseType nhinResponse = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(request, context);

        // Add response to aggregator
        if(nhinResponse != null)
        {
            SetResponseMsgDocRetrieveRequestType setResponseMsgDocRetrieveRequest = new SetResponseMsgDocRetrieveRequestType();
            setResponseMsgDocRetrieveRequest.setTransactionId(transactionId);
            setResponseMsgDocRetrieveRequest.setRetrieveDocumentSetResponse(nhinResponse);
            setResponseMsgDocRetrieveRequest.setDocumentUniqueId(documentUniqueId);
            setResponseMsgDocRetrieveRequest.setHomeCommunityId(homeCommunityId);
            setResponseMsgDocRetrieveRequest.setRepositoryUniqueId(repositoryUniqueId);
            DocRetrieveAggregator oAggregator = new DocRetrieveAggregator();
            String status = oAggregator.setResponseMsg(setResponseMsgDocRetrieveRequest);
            log.debug("Response added to aggregator. Status: " + status);
        }

        log.debug("End DocRetrieveSender.run");
    }
}
