package gov.hhs.fha.nhinc.gateway.aggregator;

import javax.jws.WebService;
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;

import gov.hhs.fha.nhinc.gateway.aggregator.document.DocQueryAggregator;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocRetrieveAggregator;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincComponentAggregator", portName = "NhincComponentAggregatorPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentaggregator.NhincComponentAggregatorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentaggregator", wsdlLocation = "WEB-INF/wsdl/NhincComponentAggregator/NhincComponentAggregator.wsdl")
public class NhincComponentAggregator
{

    /**
     * This method starts a transaction for doc query messages.  It will record
     * the systems where messages will be sent as part of this transaction and
     * then will return a unique transaction ID that will be associated with
     * all messages in this transaction.
     *
     * @param startTransactionDocQueryRequest The Patient ID, and AssigningAuthority pairs
     *                                        for each NHIN gateway that will be receiving
     *                                        a request message.
     * @return The assigned transaction ID.
     */
    public StartTransactionDocQueryResponseType startTransactionDocQuery(StartTransactionDocQueryRequestType startTransactionDocQueryRequest)
    {
        StartTransactionDocQueryResponseType oResponse = new StartTransactionDocQueryResponseType();
        String sTransactionId = "";

        // Cycle through the list of assigning authorities and get the corresponding
        // home community IDs so that we can use them later.
        //--------------------------------------------------------------------------
        HashMap<String,String> hAssignAuthToHomeCommMap = new HashMap<String, String>();
        if ((startTransactionDocQueryRequest != null) &&
            (startTransactionDocQueryRequest.getQualifiedPatientIdentifiers() != null) &&
            (startTransactionDocQueryRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier() != null) &&
            (startTransactionDocQueryRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().size() > 0))
        {
            AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();

            List<QualifiedSubjectIdentifierType> olQualSubjId = startTransactionDocQueryRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier();
            for (QualifiedSubjectIdentifierType oQualSubjId : olQualSubjId)
            {
                if ((oQualSubjId.getAssigningAuthorityIdentifier() != null) &&
                    (oQualSubjId.getAssigningAuthorityIdentifier().trim().length() > 0) &&
                    (!hAssignAuthToHomeCommMap.containsKey(oQualSubjId.getAssigningAuthorityIdentifier().trim())))
                {
                    String sHomeCommunityId = mappingDao.getHomeCommunityId(oQualSubjId.getAssigningAuthorityIdentifier().trim());
                    if ((sHomeCommunityId != null) &&
                        (sHomeCommunityId.trim().length() > 0))
                    {
                        hAssignAuthToHomeCommMap.put(oQualSubjId.getAssigningAuthorityIdentifier().trim(), sHomeCommunityId);
                    }
                }   // if ((oQualSubjId.getAssigningAuthorityIdentifier() != null) &&
            }   // for (QualifiedSubjectIdentifierType oQualSubjId : olQualSubjId)
        }   // if ((startTransactionDocQueryRequest != null) &&

        DocQueryAggregator oAggregator = new DocQueryAggregator();
        sTransactionId = oAggregator.startTransaction(startTransactionDocQueryRequest, hAssignAuthToHomeCommMap);

        oResponse.setTransactionId(sTransactionId);

        return oResponse;
    }

    /**
     * This method is called to log the response from an NHIN gateway for a
     * specific doc query transaction.  It will save the response and wait for the
     * caller to request an aggregated set of responses.
     *
     * @param setResponseMsgDocQueryRequest The response that was received and is to be logged
     *                                      awaiting for aggregation.
     * @return The status of the call.  'Success' or 'Failure'.
     */
    public SetResponseMsgDocQueryResponseType setResponseMsgDocQuery(SetResponseMsgDocQueryRequestType setResponseMsgDocQueryRequest)
    {
        SetResponseMsgDocQueryResponseType oResponse = new SetResponseMsgDocQueryResponseType();
        DocQueryAggregator oAggregator = new DocQueryAggregator();
        String sStatus = oAggregator.setResponseMsg(setResponseMsgDocQueryRequest);
        oResponse.setStatus(sStatus);
        return oResponse;
    }

    /**
     * This method returns either a status if it is still waiting for results
     * to come in, or the set of aggregated results.  If the caller passes in
     * false for the "timed out" parameter, it will only return the results
     * when all of the expected responses have been recieved.  If they have not
     * all been received, then it will return a status of "Pending" with no
     * reesults.  When all are received, it will send a status of "Complete"
     * along with the aggregated results.   If timedOut is set to true, then
     * it will pass back the set of aggregated results that was received and
     * it will place error information in the aggregated results for the ones
     * that it did not receive.  It will also send back a status of "Incomplete"
     * with the results.  If timedOut is set to true, but everything had been
     * received, then it will send back a status of "Complete" with the
     * aggregated results.
     *
     * @param getAggResultsDocQueryRequest Tells whether we are in a timed out
     *                                     state or not.
     * @return Returns results if all responses have been received or if
     *         timedOut is set to true.  Returns status only if we are not
     *         timedOut and if not all expected results have been received.
     */
    public GetAggResultsDocQueryResponseType getAggResultsDocQuery(GetAggResultsDocQueryRequestType getAggResultsDocQueryRequest)
    {
        GetAggResultsDocQueryResponseType oResponse = new GetAggResultsDocQueryResponseType();
        DocQueryAggregator oAggregator = new DocQueryAggregator();
        oResponse = oAggregator.getAggResults(getAggResultsDocQueryRequest);
        return oResponse;
    }

    /**
     * This method starts a transaction for doc retrieve messages.  It will record
     * the systems where messages will be sent as part of this transaction and
     * then will return a unique transaction ID that will be associated with
     * all messages in this transaction.
     *
     * @param startTransactionDocRetrieveRequest The Home community ID, Repository ID,
     *                                           and DocumentId for each document
     *                                           that will be requested from an
     *                                           NHIN gateway.
     * @return The assigned transaction ID.
     */
    public StartTransactionDocRetrieveResponseType startTransactionDocRetrieve(StartTransactionDocRetrieveRequestType startTransactionDocRetrieveRequest)
    {
        StartTransactionDocRetrieveResponseType oResponse = new StartTransactionDocRetrieveResponseType();
        String sTransactionId = "";

        DocRetrieveAggregator oAggregator = new DocRetrieveAggregator();
        sTransactionId = oAggregator.startTransaction(startTransactionDocRetrieveRequest);

        oResponse.setTransactionId(sTransactionId);

        return oResponse;
    }

    /**
     * This method is called to log the response from an NHIN gateway for a
     * specific doc retrieve transaction.  It will save the response and wait for the
     * caller to request an aggregated set of responses.
     *
     * @param setResponseMsgDocRetrieveRequest The response that was received and is to be logged
     *                                         awaiting for aggregation along with the identification
     *                                         information for this message that was given when the transaction
     *                                         was started.
     * @return The status of the call.  'Success' or 'Failure'.
     */
    public SetResponseMsgDocRetrieveResponseType setResponseMsgDocRetrieve(SetResponseMsgDocRetrieveRequestType setResponseMsgDocRetrieveRequest)
    {
        SetResponseMsgDocRetrieveResponseType oResponse = new SetResponseMsgDocRetrieveResponseType();
        DocRetrieveAggregator oAggregator = new DocRetrieveAggregator();
        String sStatus = oAggregator.setResponseMsg(setResponseMsgDocRetrieveRequest);
        oResponse.setStatus(sStatus);
        return oResponse;
    }

    /**
     * This method returns either a status if it is still waiting for results
     * to come in, or the set of aggregated results.  If the caller passes in
     * false for the "timed out" parameter, it will only return the results
     * when all of the expected responses have been recieved.  If they have not
     * all been received, then it will return a status of "Pending" with no
     * reesults.  When all are received, it will send a status of "Complete"
     * along with the aggregated results.   If timedOut is set to true, then
     * it will pass back the set of aggregated results that was received and
     * it will place error information in the aggregated results for the ones
     * that it did not receive.  It will also send back a status of "Incomplete"
     * with the results.  If timedOut is set to true, but everything had been
     * received, then it will send back a status of "Complete" with the
     * aggregated results.
     *
     * @param getAggResultsDocRetrieveRequest Tells whether we are in a timed out
     *                                        state or not.
     * @return Returns results if all responses have been received or if
     *         timedOut is set to true.  Returns status only if we are not
     *         timedOut and if not all expected results have been received.
     */
    public GetAggResultsDocRetrieveResponseType getAggResultsDocRetrieve(GetAggResultsDocRetrieveRequestType getAggResultsDocRetrieveRequest)
    {
        GetAggResultsDocRetrieveResponseType oResponse = new GetAggResultsDocRetrieveResponseType();
        DocRetrieveAggregator oAggregator = new DocRetrieveAggregator();
        oResponse = oAggregator.getAggResults(getAggResultsDocRetrieveRequest);
        return oResponse;
    }

    public FindAuditEventsResponseType getAggResultsAuditQuery(FindAuditEventsResponseAggregationType getAggResultsAuditQueryRequest) {
        FindAuditEventsResponseType response = null;

        // if we have an aggregate, then use that as the basis.
        //------------------------------------------------------
        if ((getAggResultsAuditQueryRequest != null) &&
            (getAggResultsAuditQueryRequest.getAggregate() != null) &&
            (NullChecker.isNotNullish(getAggResultsAuditQueryRequest.getAggregate().getFindAuditEventsReturn()))) {
            response = getAggResultsAuditQueryRequest.getAggregate();
            if (getAggResultsAuditQueryRequest.getNew() != null &&
                    NullChecker.isNotNullish(getAggResultsAuditQueryRequest.getNew().getFindAuditEventsReturn())) {
                for (AuditMessageType msg : getAggResultsAuditQueryRequest.getNew().getFindAuditEventsReturn()) {
                    response.getFindAuditEventsReturn().add(msg);
                }
            }
        }
        // If the aggregate is empty, then use the new as the basis...
        //--------------------------------------------------------------
        else if ((getAggResultsAuditQueryRequest != null) &&
                    NullChecker.isNotNullish(getAggResultsAuditQueryRequest.getNew().getFindAuditEventsReturn())) {
            response =  getAggResultsAuditQueryRequest.getNew();
        }

        return response;
    }
}
