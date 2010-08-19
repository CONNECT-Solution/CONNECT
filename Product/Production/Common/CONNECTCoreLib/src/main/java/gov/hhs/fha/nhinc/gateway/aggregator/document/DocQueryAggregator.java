package gov.hhs.fha.nhinc.gateway.aggregator.document;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.gateway.aggregator.AggregatorException;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocQueryResponseType;
import gov.hhs.fha.nhinc.gateway.aggregator.model.DocQueryMessageKey;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggMessageResult;

import gov.hhs.fha.nhinc.gateway.aggregator.dao.AggTransactionDao;

import gov.hhs.fha.nhinc.gateway.aggregator.StartTransactionDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.dao.AggMessageResultDao;
import gov.hhs.fha.nhinc.gateway.aggregator.persistence.GarbageCollectorMgr;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;


/**
 * This class is used to handle the tasks surrounding aggregating
 * responses to document query messages.
 *
 * @author Les Westberg
 */
public class DocQueryAggregator
{
    private static Log log = LogFactory.getLog(DocQueryAggregator.class);
    private static String DOC_QUERY_NAME = "documentquery";
    private static String DOC_QUERY_RESPONSE = "AdhocQueryResponse";

    /**
     * Default constructor
     */
    public DocQueryAggregator()
    {
    }

    /**
     * This method marshalls the AdhocQueryResponse into an XML string.
     *
     * @param oAdhocQueryRsponse The object to be marshalled.
     * @return The XML representation of the object.
     */
    public String marshalAdhocQueryResponse(AdhocQueryResponse oAdhocQueryResponse)
    {
        String sXML = "";

        if (oAdhocQueryResponse != null)
        {
            try
            {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                marshaller.marshal(oAdhocQueryResponse, swXML);
                sXML = swXML.toString();
            }
            catch (Exception e)
            {
                log.error("Failed to marshall AdhocQueryResponse to XML: " + e.getMessage());
            }
        }

        return sXML;
    }

    /**
     * This method unmarshalls the XML into an AdhocQueryResponse object.
     *
     * @param sAdhocQueryResponseXML The XML of the AdhocQueryResponse object to be unmarshalled.
     * @return The AdhocQueryResponse object.
     */
    public AdhocQueryResponse unmarshalAdhocQueryResponse(String sAdhocQueryResponseXML)
    {
        AdhocQueryResponse oAdhocQueryResponse = null;

        if (sAdhocQueryResponseXML != null)
        {
            try
            {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.query._3");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                StringReader srAdhocQueryResponseXML = new StringReader(sAdhocQueryResponseXML);
                oAdhocQueryResponse = (AdhocQueryResponse)unmarshaller.unmarshal(srAdhocQueryResponseXML);
            }
            catch (Exception e)
            {
                log.error("Failed to marshall AdhocQueryResponse to XML: " + e.getMessage());
            }
        }

        return oAdhocQueryResponse;
    }

    /**
     * This method is called from the web service to start a transaction.  it
     * prepares the set of DocQueryMessageKey(s) and then calls the
     * other startTransaction to do the work.
     *
     * @param oRequest The message that was sent to the web service.
     * @return The transaction Id.
     */
    public String startTransaction (StartTransactionDocQueryRequestType oRequest,
                                    HashMap<String,String> hAssignAuthHomeCommMap)
    {
        String sTransactionId = "";

        // Based on property settings, spin off the garbage collector thread
        //-------------------------------------------------------------------
        GarbageCollectorMgr.runGarbageCollection();

        if ((oRequest != null) &&
            (oRequest.getQualifiedPatientIdentifiers() != null) &&
            (oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier() != null) &&
            (oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().size() > 0))
        {
            ArrayList<DocQueryMessageKey> olKey = new ArrayList<DocQueryMessageKey>();
            List<QualifiedSubjectIdentifierType> olIds = oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier();

            for (QualifiedSubjectIdentifierType oId : olIds)
            {
                boolean baFound[] = {false, false, false};

                DocQueryMessageKey oKey = new DocQueryMessageKey();
                if (oId.getAssigningAuthorityIdentifier() != null)
                {
                    oKey.setAssigningAuthority(oId.getAssigningAuthorityIdentifier());
                    baFound[0] = true;
                }
                else
                {
                    oKey.setAssigningAuthority("");
                }

                if ((oKey.getAssigningAuthority() != null) &&
                    (oKey.getAssigningAuthority().length() > 0))
                {
                    HomeCommunityType homeCommunityId = null;
                    log.debug("Looking up Home Community Id for Assigning Authority Id: " + oKey.getAssigningAuthority());
                    homeCommunityId = ConnectionManagerCommunityMapping.getHomeCommunityByAssigningAuthority(oKey.getAssigningAuthority());

                        if (homeCommunityId != null &&
                            NullChecker.isNotNullish(homeCommunityId.getHomeCommunityId()))
                        {
                            oKey.setHomeCommunityId(homeCommunityId.getHomeCommunityId());
                        }
                        else
                        {
                            log.warn("There was no mapping for this assigning authority to a home community,  " +
                                     "The home community ID will be treated as the same as the assigning authority.");
                            oKey.setHomeCommunityId("");            // Assume it is the same as the
                        }

                    baFound[1] = true;
                }
                else
                {
                    log.warn("The assigning authority was either null or empty.  " +
                             "No mapping to home community ID can be done.");
                    oKey.setHomeCommunityId("");            // Assume it is the same as the
                }

                if (oId.getSubjectIdentifier() != null)
                {
                    oKey.setPatientId(oId.getSubjectIdentifier());
                    baFound[2] = true;
                }
                else
                {
                    oKey.setPatientId("");
                }

                // We can only build keys when we have all the appropriate identifiers
                //--------------------------------------------------------------------
                if (baFound[0] && baFound[1] && baFound[2])
                {
                    olKey.add(oKey);
                }
            }   // for (QualifiedSubjectIdentifierType oId : olIds)

            if (olKey.size() > 0)
            {
                DocQueryMessageKey oaKey[] = olKey.toArray(new DocQueryMessageKey[0]);
                sTransactionId = startTransaction(oaKey);
            }
        }   // if ((oRequest != null) && ...

        return sTransactionId;
    }

    /**
     * This method sets the response message for the specified message key.
     *
     * @param oRequest The message key and the response message.
     * @return The status of the request, either "SUCCESS" or "FAIL".
     */
    public String setResponseMsg(SetResponseMsgDocQueryRequestType oRequest)
    {
        String sStatus = "";
        String sTransactionId = "";
        String sHomeCommunityId = "";
        DocQueryMessageKey oKey = new DocQueryMessageKey();
        String sAdhocQueryResponseXML = "";

        if (oRequest != null)
        {
            // Transaction Id
            //----------------
            if ((oRequest.getTransactionId() != null) &&
                (oRequest.getTransactionId().length() > 0))
            {
                sTransactionId = oRequest.getTransactionId();
            }
            else
            {
                String sErrorMessage = "DocQuery Aggregator - setResponseMsg called with no Transaction Id - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            // HomeCommunityId
            //-----------------
            // If the home community is not passed, it will be looked up by the DocQueryMessageKey based
            // on assigning authority.
            //-------------------------------------------------------------------------------------------
            if ((oRequest.getHomeCommunityId() != null) &&
                (oRequest.getHomeCommunityId().length() > 0))
            {
                sHomeCommunityId = oRequest.getHomeCommunityId();
            }
            oKey.setHomeCommunityId(sHomeCommunityId);

            // Assigning Authority
            //--------------------
            if ((oRequest.getQualifiedPatientIdentifier() != null) &&
                (oRequest.getQualifiedPatientIdentifier().getAssigningAuthorityIdentifier() != null) &&
                (oRequest.getQualifiedPatientIdentifier().getAssigningAuthorityIdentifier().length() > 0))
            {
                oKey.setAssigningAuthority(oRequest.getQualifiedPatientIdentifier().getAssigningAuthorityIdentifier());
            }
            else
            {
                String sErrorMessage = "DocQuery Aggregator - setResponseMsg called with no assigning authority - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            //Patient Id
            //-----------
            if ((oRequest.getQualifiedPatientIdentifier() != null) &&
                (oRequest.getQualifiedPatientIdentifier().getSubjectIdentifier() != null) &&
                (oRequest.getQualifiedPatientIdentifier().getSubjectIdentifier().length() > 0))
            {
                oKey.setPatientId(oRequest.getQualifiedPatientIdentifier().getSubjectIdentifier());
            }
            else
            {
                String sErrorMessage = "DocQuery Aggregator - setResponseMsg called with no subject identifier - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            // Marshall the AdhocQueryResponse
            //---------------------------------
            if (oRequest.getAdhocQueryResponse() != null)
            {
                sAdhocQueryResponseXML = marshalAdhocQueryResponse(oRequest.getAdhocQueryResponse());
            }

            if (!sStatus.equals(DocumentConstants.FAIL_TEXT))
            {
                sStatus = setResponseMsg(sTransactionId, oKey, sAdhocQueryResponseXML);
            }
        }
        else
        {
            sStatus = DocumentConstants.FAIL_TEXT;
        }

        return sStatus;
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
    public GetAggResultsDocQueryResponseType getAggResults(GetAggResultsDocQueryRequestType oRequest)
    {
        GetAggResultsDocQueryResponseType oResponse = new GetAggResultsDocQueryResponseType();

        String sTransactionId = "";
        boolean bTimedOut = false;

        if ((oRequest != null) &&
            (oRequest.getTransactionId() != null) &&
            (oRequest.getTransactionId().trim().length() > 0))
        {
            sTransactionId = oRequest.getTransactionId();
        }
        else
        {
            String sErrorMessage = "";
            log.error(sErrorMessage);
            oResponse.setStatus(DocumentConstants.FAIL_TEXT);
            return oResponse;
        }

        bTimedOut = oRequest.isTimedOut();
        AdhocQueryResponse oAdhocQueryResponse = null;

        try
        {
            oAdhocQueryResponse = getAggResults(sTransactionId, bTimedOut);
            if (oAdhocQueryResponse == null)
            {
                oResponse.setStatus(DocumentConstants.PENDING_TEXT);
                oResponse.setAdhocQueryResponse(null);
            }
            else
            {
                oResponse.setStatus(DocumentConstants.COMPLETE_TEXT);
                oResponse.setAdhocQueryResponse(oAdhocQueryResponse);
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve transaction.  Message: " + e.getMessage();
            log.error(sErrorMessage, e);
            oResponse.setStatus(DocumentConstants.FAIL_TEXT);
            oResponse.setAdhocQueryResponse(null);
        }

        return oResponse;
    }


    /**
     * This method starts a transaction using the set of message keys passed in.
     * It will create a transaction, with one message for each message key.  The
     * entries will be written to the AGGREGATOR.AGG_TRANSACTION and
     * AGGREGATOR.AGG_MESSAGE_RESULTS tables.
     *
     * @param oaMessageKey The set of message keys.  There will be one row
     *                     written to the AGG_MESSAGE_RESULTS table for each
     *                     array item.
     * @return The transaction Id that was assigned when this transaction was
     *         started.
     */
    public String startTransaction (DocQueryMessageKey[] oaMessageKey)
    {
        String sTransactionId = "";

        if ((oaMessageKey == null) ||
            (oaMessageKey.length <= 0))
        {
            return sTransactionId;          // Nothing in the record to start
                                            // a transaction for.
        }

        Date dtNow = new Date();

        AggTransaction oTrans = new AggTransaction();
        oTrans.setServiceType(DOC_QUERY_NAME);
        oTrans.setTransactionStartTime(dtNow);

        HashSet<AggMessageResult> hMsgResult = new HashSet<AggMessageResult>();

        for (DocQueryMessageKey oMessageKey : oaMessageKey)
        {
            AggMessageResult oMsgResult = new AggMessageResult();
            oMsgResult.setAggTransaction(oTrans);
            oMsgResult.setMessageKey(oMessageKey.createXMLMessageKey());
            oMsgResult.setMessageOutTime(dtNow);
            oMsgResult.setResponseMessageType(DOC_QUERY_RESPONSE);
            oMsgResult.setResponseMessage("");
            oMsgResult.setResponseReceivedTime(null);
            hMsgResult.add(oMsgResult);
        }
        oTrans.setAggMessageResults(hMsgResult);

        AggTransactionDao oTransDao = new AggTransactionDao();
        oTransDao.save(oTrans);

        if ((oTrans != null) &&
            (oTrans.getTransactionId() != null) &&
            (oTrans.getTransactionId().length() > 0))
        {
            sTransactionId = oTrans.getTransactionId();
        }

        return sTransactionId;
    }

    /**
     * This method retrieves the message result entry in the database and fills in the
     * response information for that message.  It locates the entry based on the
     * message key and transaction Id based on the information that was passed in.
     *
     * @param sTransactionId The transaction Id associated with the message.
     * @param oKey The information that is used for the message key.
     * @param sAdhocQueryResponseXML The AdhocQueryResponse in XML form.
     * @return The status.  "SUCCESS" or "FAIL".
     */
    public String setResponseMsg(String sTransactionId, DocQueryMessageKey oKey, String sAdhocQueryResponseXML)
    {
        String sStatus = DocumentConstants.SUCCESS_TEXT;
        String sMessageKey = oKey.createXMLMessageKey();

        AggMessageResultDao oAggMessageResultDao = new AggMessageResultDao();
        AggMessageResult oMsgResult = null;
        try
        {
            oMsgResult = oAggMessageResultDao.findByMessageKey(sTransactionId, sMessageKey);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve AggMessageResult for: TransactionId: " + sTransactionId +
                                   ", MessageKey: " + oKey.createXMLMessageKey() + ".  Message: " + e.getMessage();
            log.error(sErrorMessage, e);
            sStatus = DocumentConstants.FAIL_TEXT;
            return sStatus;         // No reason to proceed....
        }

        if (oMsgResult == null)
        {
            String sErrorMessage = "Failed to find existing AggMessageResult for: TransactionId: " + sTransactionId +
                                   ", MessageKey: " + oKey.createXMLMessageKey() + ".  Message response not recorded.";
            log.error(sErrorMessage);
            sStatus = DocumentConstants.FAIL_TEXT;
            return sStatus;         // No reason to proceed....
        }

        // if we got here - we have the message and we need to fill in the response information...
        //----------------------------------------------------------------------------------------
        oMsgResult.setResponseReceivedTime(new Date());
        oMsgResult.setResponseMessage(sAdhocQueryResponseXML);
        oAggMessageResultDao.save(oMsgResult);

        return DocumentConstants.SUCCESS_TEXT;
    }

    /**
     * This method looks through the results to see if all responses have been
     * received.  If they have, then it returns true.  Otherwise it returns
     * false.
     *
     * @param oTrans The Transaction with all of the message results.
     * @return TRUE if all responses have been received, FALSE if not.
     */
    private boolean areResultsReady(AggTransaction oTrans)
    {
        if (oTrans == null)
        {
            return false;
        }

        for (AggMessageResult oMsgResult : oTrans.getAggMessageResults())
        {
            // If we fall through - we are still waiting for results.
            //-------------------------------------------------------
            if ((oMsgResult != null) &&
                (oMsgResult.getResponseReceivedTime() == null))
            {
                return false;
            }
        }


        return true;
    }

    /**
     * This method will create an empty AdhocQueryResponse.  One that would
     * be returned if there were no results.
     *
     * @return An AdhocQueryResponse that represents an empty result set.
     */
    private AdhocQueryResponse createEmptyResult()
    {
        AdhocQueryResponse oAdhocQueryResponse = new AdhocQueryResponse();

        oAdhocQueryResponse.setStatus(DocumentConstants.XDS_QUERY_RESPONSE_STATUS_SUCCESS);
        oAdhocQueryResponse.setRegistryObjectList(new RegistryObjectListType());

        return oAdhocQueryResponse;
    }

    /**
     * This method is used to combine the results together into a single
     * AdhocQueryResponse.  It will extract the items in the RegistryObjectList
     * from the various systems and place them into the new response.
     *
     * @param olMsgResult List of messages to be combined
     * @return The combined AdhocQueryResponse object.
     */
    private AdhocQueryResponse combineResults(Set<AggMessageResult> olMsgResult)
    {
        AdhocQueryResponse oAdhocQueryResponse = createEmptyResult();
        RegistryObjectListType oRegObjList = oAdhocQueryResponse.getRegistryObjectList();

        for (AggMessageResult oMsgResult : olMsgResult)
        {
            // If the message contains a response received time, then it means that we
            // received the response from the remote system.  We need to aggregate the
            // results.
            //--------------------------------------------------------------------------
            if (oMsgResult.getResponseReceivedTime() != null)
            {
                // The response may have been empty - if so - there is nothing to aggregate from here.
                //-------------------------------------------------------------------------------------
                if ((oMsgResult.getResponseMessage() != null) &&
                    (oMsgResult.getResponseMessage().trim().length() > 0))
                {
                    AdhocQueryResponse oTempResponse = unmarshalAdhocQueryResponse(oMsgResult.getResponseMessage());
                    if ((oTempResponse != null) &&
                        (oTempResponse.getRegistryObjectList() != null) &&
                        (oTempResponse.getRegistryObjectList().getIdentifiable() != null) &&
                        (oTempResponse.getRegistryObjectList().getIdentifiable().size() > 0))
                    {
                        List<JAXBElement<? extends IdentifiableType>> olNewRegObjs = oTempResponse.getRegistryObjectList().getIdentifiable();

                        for (JAXBElement<? extends IdentifiableType> oJAXBElement : olNewRegObjs)
                        {
                            oRegObjList.getIdentifiable().add(oJAXBElement);
                        }   // for (JAXBElement<? extends IdentifiableType> oJAXBElement : olNewRegObjs)
                    }   // if ((oTempResponse != null) &&

                    // It is possible that there may be error information in this message
                    // that we need to pull out too...
                    //--------------------------------------------------------------------
                    if ((oTempResponse != null) &&
                        (oTempResponse.getRegistryErrorList() != null) &&
                        (oTempResponse.getRegistryErrorList().getRegistryError() != null) &&
                        (oTempResponse.getRegistryErrorList().getRegistryError().size() > 0))
                    {
                        RegistryErrorList oRegErrors = null;
                        if (oAdhocQueryResponse.getRegistryErrorList() == null)
                        {
                            oRegErrors = new RegistryErrorList();
                            oAdhocQueryResponse.setRegistryErrorList(oRegErrors);
                        }
                        else
                        {
                            oRegErrors = oAdhocQueryResponse.getRegistryErrorList();
                        }

                        List<RegistryError> olRegError = oRegErrors.getRegistryError();
                        for (RegistryError oRegError : oTempResponse.getRegistryErrorList().getRegistryError())
                        {
                            olRegError.add(oRegError);
                        }
                    }
                }   // if ((oMsgResult.getResponseMessage() != null) &&
            }   // if (oMsgResult.getResponseReceivedTime() != null)
            else    // This means that this result never received a response - log an error that this one timed out.
            {
                RegistryErrorList oRegErrors = null;
                if (oAdhocQueryResponse.getRegistryErrorList() == null)
                {
                    oRegErrors = new RegistryErrorList();
                    oAdhocQueryResponse.setRegistryErrorList(oRegErrors);
                }
                else
                {
                    oRegErrors = oAdhocQueryResponse.getRegistryErrorList();
                }

                List<RegistryError> olRegError = oRegErrors.getRegistryError();
                RegistryError oRegError = new RegistryError();
                olRegError.add(oRegError);
                oRegError.setErrorCode(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REGISTRY_ERROR);
                oRegError.setCodeContext(DocumentConstants.XDS_RETRIEVE_CODECONTEXT_TIMEDOUT_MSG);
                if ((oMsgResult.getMessageKey() != null) &&
                    (oMsgResult.getMessageKey().length() > 0))
                {
                    try
                    {
                        DocQueryMessageKey oMessageKey = new DocQueryMessageKey(oMsgResult.getMessageKey());
                        oRegError.setLocation(oMessageKey.getHomeCommunityId());
                    }
                    catch (Exception e)
                    {
                        String sErrorMessage = "Failed to parse message key.  Message = " + e.getMessage();
                        log.error(sErrorMessage, e);
                        //  do not throw an error - we will log it and move on...
                    }
                }   // if ((oMsgResult.getMessageKey() != null) &&
            }   // else ...
        }   // for (AggMessageResult oMsgResult : olMsgResult)


        return oAdhocQueryResponse;
    }

    /**
     * This method returns either an aggregated AdhocQueryResponse if all results
     * have been retrieved, or if the timed out flag has been passed, or it will
     * return null if the results are not ready and the timedout flag has not
     * been set.
     *
     * @param sTransactionId The transaction ID of the transaction to be aggregated.
     * @param bTimedOut TRUE if we should stop waiting for results and compile what
     *                       is available.  FALSE if we should only return them
     *                       if all expected results have been received.
     * @return The aggregated results.
     */
    public AdhocQueryResponse getAggResults(String sTransactionId, boolean bTimedOut)
        throws AggregatorException
    {
        AdhocQueryResponse oResponse = null;

        // Retrieve the records and see if everything is ready...
        //--------------------------------------------------------
        AggTransactionDao oAggTransactionDao = new AggTransactionDao();
        AggTransaction oTrans = oAggTransactionDao.findById(sTransactionId);
        if (oTrans == null)
        {
            String sErrorMessage = "Failed to find an aggregator transaction for TransactionId: " +
                                   sTransactionId;
            log.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        // Make sure this transaction ID is for the right type of transaction.
        //--------------------------------------------------------------------
        if ((oTrans.getServiceType() == null) ||
            ((oTrans.getServiceType() != null) &&
            (!oTrans.getServiceType().equals(DOC_QUERY_NAME))))
        {
            String sErrorMessage = "The specified TransactionId: " + sTransactionId + " is not associated with " +
                                   " Document Query messages.  It is for: " + oTrans.getServiceType() + ".";
            log.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        // If we have timed out, or if the results are ready then aggregate them
        // and return them.
        //----------------------------------------------------------------------
        if ((bTimedOut) ||
            (areResultsReady(oTrans)))
        {
            if ((oTrans.getAggMessageResults() != null) &&
                (oTrans.getAggMessageResults().size() > 0))
            {
                oResponse = combineResults(oTrans.getAggMessageResults());
            }
            else
            {
                // If we got here - it was a transaction with no expected results
                // create an empty result set and return it.
                //---------------------------------------------------------------
                oResponse = createEmptyResult();
            }

            // Delete the entries out of the database...
            //--------------------------------------------
            oAggTransactionDao.delete(oTrans);
            oTrans = null;
        }

        return oResponse;
    }
}
