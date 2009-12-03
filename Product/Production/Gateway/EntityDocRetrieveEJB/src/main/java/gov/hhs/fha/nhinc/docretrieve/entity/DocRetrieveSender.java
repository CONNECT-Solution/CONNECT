package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.proxy.NhincProxyDocRetrieveSecuredImpl;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocRetrieveAggregator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

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
    AssertionType assertion = null;

    public DocRetrieveSender(String transactionId, RespondingGatewayCrossGatewayRetrieveSecuredRequestType request, AssertionType assertion)
    {
        this.transactionId = transactionId;
        this.request = request;
        this.assertion = assertion;
    }

    @Override
    public void run()
    {
        log.debug("Begin DocRetrieveSender.run");
        try
        {
            NhincProxyDocRetrieveSecuredImpl docRetrieveProxy = new NhincProxyDocRetrieveSecuredImpl();

            // Collect identifying attributes
            DocumentRequest docRequest = null;
            String documentUniqueId = null;
            String repositoryUniqueId = null;
            String homeCommunityId = null;
            if((request != null) && (request.getRetrieveDocumentSetRequest() != null) && (!request.getRetrieveDocumentSetRequest().getDocumentRequest().isEmpty()))
            {
                log.debug("Doc retrieve request had sufficient information - creating request");
                docRequest = request.getRetrieveDocumentSetRequest().getDocumentRequest().get(0);
                if(docRequest != null)
                {
                    documentUniqueId = docRequest.getDocumentUniqueId();
                    repositoryUniqueId = docRequest.getRepositoryUniqueId();
                    homeCommunityId = docRequest.getHomeCommunityId();
                }
                else
                {
                    log.warn("DocRetrieveSender - DocRequest was null.");
                }
            }
            else
            {
                log.warn("DocRetrieveSender - request did not contain sufficient inormation to create a doc retrieve request.");
            }

            // Call NHIN proxy
            RetrieveDocumentSetResponseType nhinResponse = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(request, assertion);

            // Add response to aggregator
            if(nhinResponse != null)
            {
                log.debug("DocRetrieveSender - NHIN response was not null - processing result.");
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
            else
            {
                log.warn("DocRetrieveSender - NHIN response was null");
            }
        }
        catch(Throwable t)
        {
            log.error("Error sending doc retrieve message: " + t.getMessage(), t);
        }

        log.debug("End DocRetrieveSender.run");
    }
}
