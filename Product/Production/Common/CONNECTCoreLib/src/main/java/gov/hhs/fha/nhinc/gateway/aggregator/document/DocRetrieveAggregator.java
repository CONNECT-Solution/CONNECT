/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.gateway.aggregator.document;

import gov.hhs.fha.nhinc.gateway.aggregator.AggregatorException;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocRetrieveResponseType;
import gov.hhs.fha.nhinc.gateway.aggregator.SetResponseMsgDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.StartTransactionDocRetrieveRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.dao.AggMessageResultDao;
import gov.hhs.fha.nhinc.gateway.aggregator.dao.AggTransactionDao;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggMessageResult;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import gov.hhs.fha.nhinc.gateway.aggregator.model.DocRetrieveMessageKey;
import gov.hhs.fha.nhinc.gateway.aggregator.persistence.GarbageCollectorMgr;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.io.FileNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * This class is used to handle the tasks surrounding aggregating 
 * responses to document retrieve messages.
 * 
 * @author Les Westberg
 */
public class DocRetrieveAggregator {

    private static Log log = LogFactory.getLog(DocRetrieveAggregator.class);
    private static String DOC_RETRIEVE_NAME = "documentretrieve";
    private static String DOC_RETRIEVE_RESPONSE = "RetrieveDocumentSetResponse";

    /**
     * Default constructor
     */
    public DocRetrieveAggregator() {
    }

    /**
     * This method marshalls the RetrieveDocumentSetResponse into an XML string.
     * 
     * @param oRetrieveDocumentSetResponse  The object to be marshalled.
     * @return The XML representation of the object.
     */
    public String marshalRetrieveDocumentSetResponse(RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse) {
        String sXML = "";

        if (oRetrieveDocumentSetResponse != null) {
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
                Marshaller marshaller = jc.createMarshaller();
                StringWriter swXML = new StringWriter();
                ihe.iti.xds_b._2007.ObjectFactory ObjectFactory = new ihe.iti.xds_b._2007.ObjectFactory();
                JAXBElement oElement = ObjectFactory.createRetrieveDocumentSetResponse(oRetrieveDocumentSetResponse);
                marshaller.marshal(oElement, swXML);
                sXML = swXML.toString();
            } catch (Exception e) {
                log.error("Failed to marshall RetrieveDocumentSetResponse to XML: " + e.getMessage());
            }
        }

        return sXML;
    }

    private String writeRespToDisk(String responseText, String transactionId) {
        String url = null;

        String destDirProp = getLargeFileDirProp();
        String destDir = destDirProp + "/" + transactionId;

        if (NullChecker.isNotNullish(destDirProp)) {
            // Test if directory exists on the file system
            File destDirFile = createDestDirectory(destDir);

            if (destDirFile == null) {
                log.error("Could not create destination directory: " + destDir);
            } else {

                // Convert the url to a uri
                URI uri = null;
                url = "file:///" + destDir + "/" + generateFileName();

                try {
                    uri = new URI(url);
                } catch (URISyntaxException e) {
                    log.error("Unexpected form of url: " + url + ". " + e.getMessage());
                    return null;
                }

                File sourceFile = new File(uri);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(sourceFile);

                    log.debug("Opened the output file successfully");
                    try {
                        out.write(responseText.getBytes());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        log.error("Failed to write to output file: " + ex.getMessage());
                        url = null;
                    }

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    log.error("Failed to find output file: " + ex.getMessage());
                    url = null;
                }
            }

            log.debug("Storing this large response at file location: " + url);
        } else {
            log.error("Aggregator Large Response Directory property was not set");

        }

        return url;
    }

    /**
     * This method unmarshalls the XML into an RetrieveDocumentSetResponse object.
     * 
     * @param sAdhocQueryResponseXML The XML of the AdhocQueryResponse object to be unmarshalled.
     * @return The AdhocQueryResponse object.
     */
    public RetrieveDocumentSetResponseType unmarshalAdhocQueryResponse(String sRetrieveDocumentSetResponseXML) {
        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = null;

        if (sRetrieveDocumentSetResponseXML != null) {
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                StringReader srRetrieveDocumentSetResponseXML = new StringReader(sRetrieveDocumentSetResponseXML);
                JAXBElement oElement = (JAXBElement) unmarshaller.unmarshal(srRetrieveDocumentSetResponseXML);
                if ((oElement != null) &&
                        (oElement.getName() != null) &&
                        (oElement.getName().getLocalPart() != null) &&
                        (oElement.getName().getLocalPart().equals(DOC_RETRIEVE_RESPONSE)) &&
                        (oElement.getValue() != null)) {
                    oRetrieveDocumentSetResponse = (RetrieveDocumentSetResponseType) oElement.getValue();
                }
            } catch (Exception e) {
                log.error("Failed to marshall RetrieveDocumentSetResponse to XML: " + e.getMessage());
            }
        }

        return oRetrieveDocumentSetResponse;
    }

    /**
     * This method is called from the web service to start a transaction.  it
     * prepares the set of DocRetrieveMessageKey(s) and then calls the
     * other startTransaction to do the work.
     * 
     * @param oRequest The message that was sent to the web service.
     * @return The transaction Id.
     */
    public String startTransaction(StartTransactionDocRetrieveRequestType oRequest) {
        String sTransactionId = "";

        // Based on property settings, spin off the garbage collector thread
        //-------------------------------------------------------------------
        GarbageCollectorMgr.runGarbageCollection();

        if ((oRequest != null) &&
                (oRequest.getRetrieveDocumentSetRequest() != null) &&
                (oRequest.getRetrieveDocumentSetRequest().getDocumentRequest() != null) &&
                (oRequest.getRetrieveDocumentSetRequest().getDocumentRequest().size() > 0)) {
            ArrayList<DocRetrieveMessageKey> olKey = new ArrayList<DocRetrieveMessageKey>();
            List<DocumentRequest> olDocRequest = oRequest.getRetrieveDocumentSetRequest().getDocumentRequest();

            for (DocumentRequest oDocRequest : olDocRequest) {
                boolean baFound[] = {false, false, false};

                DocRetrieveMessageKey oKey = new DocRetrieveMessageKey();
                if (oDocRequest.getHomeCommunityId() != null) {
                    oKey.setHomeCommunityId(oDocRequest.getHomeCommunityId());
                    baFound[0] = true;
                } else {
                    oKey.setHomeCommunityId("");
                }

                if (oDocRequest.getRepositoryUniqueId() != null) {
                    oKey.setRepositoryId(oDocRequest.getRepositoryUniqueId());
                    baFound[1] = true;
                } else {
                    oKey.setRepositoryId("");
                }

                if (oDocRequest.getDocumentUniqueId() != null) {
                    oKey.setDocumentId(oDocRequest.getDocumentUniqueId());
                    baFound[2] = true;
                } else {
                    oKey.setDocumentId("");
                }

                // We can only build keys when we have all the appropriate identifiers
                //--------------------------------------------------------------------
                if (baFound[0] && baFound[1] && baFound[2]) {
                    olKey.add(oKey);
                }
            }   // for (QualifiedSubjectIdentifierType oId : olIds)

            if (olKey.size() > 0) {
                DocRetrieveMessageKey oaKey[] = olKey.toArray(new DocRetrieveMessageKey[0]);
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
    public String setResponseMsg(SetResponseMsgDocRetrieveRequestType oRequest) {
        String sStatus = "";
        String sTransactionId = "";
        DocRetrieveMessageKey oKey = new DocRetrieveMessageKey();
        String sRetrieveDocumentSetResponseXML = "";

        if (oRequest != null) {
            // Transaction Id
            //----------------
            if ((oRequest.getTransactionId() != null) &&
                    (oRequest.getTransactionId().length() > 0)) {
                sTransactionId = oRequest.getTransactionId();
            } else {
                String sErrorMessage = "DocRetrieve Aggregator - setResponseMsg called with no Transaction Id - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            // HomeCommunityId
            //-----------------
            if ((oRequest.getHomeCommunityId() != null) &&
                    (oRequest.getHomeCommunityId().length() > 0)) {
                oKey.setHomeCommunityId(oRequest.getHomeCommunityId());
            } else {
                String sErrorMessage = "DocQuery Aggregator - setResponseMsg called with no home community ID - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            // Repository ID
            //--------------
            if ((oRequest.getRepositoryUniqueId() != null) &&
                    (oRequest.getRepositoryUniqueId().length() > 0)) {
                oKey.setRepositoryId(oRequest.getRepositoryUniqueId());
            } else {
                String sErrorMessage = "DocQuery Aggregator - setResponseMsg called with no repository ID - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            // Document ID
            //-----------
            if ((oRequest.getDocumentUniqueId() != null) &&
                    (oRequest.getDocumentUniqueId().length() > 0)) {
                oKey.setDocumentId(oRequest.getDocumentUniqueId());
            } else {
                String sErrorMessage = "DocQuery Aggregator - setResponseMsg called with no subject identifier - this is a required data element.";
                log.error(sErrorMessage);
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            // Marshall the RetrieveDocumentSetResponse
            //------------------------------------------
            if (oRequest.getRetrieveDocumentSetResponse() != null) {
                // Check to see if this is a large response
                boolean containsLargeResp = false;
                int size = 0;
                for (DocumentResponse resp : oRequest.getRetrieveDocumentSetResponse().getDocumentResponse()) {
                    if (resp != null &&
                            resp.getDocument() != null &&
                            resp.getDocument().length > 0) {
                        log.debug("Received file of " + resp.getDocument().length + " bytes");
                        size += resp.getDocument().length;
                        log.debug("Received a total of " + resp.getDocument().length + " bytes in files in this response");

                        if (size > getLargeFileSizeProp()) {
                            containsLargeResp = true;
                            break;
                        }
                    }
                }

                String responseText = marshalRetrieveDocumentSetResponse(oRequest.getRetrieveDocumentSetResponse());

                if (containsLargeResp == true) {
                    sRetrieveDocumentSetResponseXML = writeRespToDisk(responseText, oRequest.getTransactionId());
                    log.debug("Storing the following URL: " + sRetrieveDocumentSetResponseXML);
                } else {
                    sRetrieveDocumentSetResponseXML = responseText;

                }
                sStatus = DocumentConstants.SUCCESS_TEXT;
            } else {
                sStatus = DocumentConstants.FAIL_TEXT;
            }

            if (!sStatus.equals(DocumentConstants.FAIL_TEXT)) {
                sStatus = setResponseMsg(sTransactionId, oKey, sRetrieveDocumentSetResponseXML);
            }
        } else {
            sStatus = DocumentConstants.FAIL_TEXT;
        }

        sRetrieveDocumentSetResponseXML = null;
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
     * @param oRequest Tells whether we are in a timed out
     *                                     state or not.
     * @return Returns results if all responses have been received or if
     *         timedOut is set to true.  Returns status only if we are not 
     *         timedOut and if not all expected results have been received.
     */
    public GetAggResultsDocRetrieveResponseType getAggResults(GetAggResultsDocRetrieveRequestType oRequest) {
        GetAggResultsDocRetrieveResponseType oResponse = new GetAggResultsDocRetrieveResponseType();

        String sTransactionId = "";
        boolean bTimedOut = false;

        if ((oRequest != null) &&
                (oRequest.getTransactionId() != null) &&
                (oRequest.getTransactionId().trim().length() > 0)) {
            sTransactionId = oRequest.getTransactionId();
        } else {
            String sErrorMessage = "";
            log.error(sErrorMessage);
            oResponse.setStatus(DocumentConstants.FAIL_TEXT);
            return oResponse;
        }

        bTimedOut = oRequest.isTimedOut();
        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = null;

        try {
            oRetrieveDocumentSetResponse = getAggResults(sTransactionId, bTimedOut);
            if (oRetrieveDocumentSetResponse == null) {
                oResponse.setStatus(DocumentConstants.PENDING_TEXT);
                oResponse.setRetrieveDocumentSetResponse(null);
            } else {
                oResponse.setStatus(DocumentConstants.COMPLETE_TEXT);
                oResponse.setRetrieveDocumentSetResponse(oRetrieveDocumentSetResponse);
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to retrieve transaction.  Message: " + e.getMessage();
            log.error(sErrorMessage, e);
            oResponse.setStatus(DocumentConstants.FAIL_TEXT);
            oResponse.setRetrieveDocumentSetResponse(null);
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
    public String startTransaction(DocRetrieveMessageKey[] oaMessageKey) {
        String sTransactionId = "";

        if ((oaMessageKey == null) ||
                (oaMessageKey.length <= 0)) {
            return sTransactionId;          // Nothing in the record to start
            // a transaction for.
        }

        Date dtNow = new Date();

        AggTransaction oTrans = new AggTransaction();
        oTrans.setServiceType(DOC_RETRIEVE_NAME);
        oTrans.setTransactionStartTime(dtNow);

        HashSet<AggMessageResult> hMsgResult = new HashSet<AggMessageResult>();

        for (DocRetrieveMessageKey oMessageKey : oaMessageKey) {
            AggMessageResult oMsgResult = new AggMessageResult();
            oMsgResult.setAggTransaction(oTrans);
            oMsgResult.setMessageKey(oMessageKey.createXMLMessageKey());
            oMsgResult.setMessageOutTime(dtNow);
            oMsgResult.setResponseMessageType(DOC_RETRIEVE_RESPONSE);
            oMsgResult.setResponseMessage("");
            oMsgResult.setResponseReceivedTime(null);
            hMsgResult.add(oMsgResult);
        }
        oTrans.setAggMessageResults(hMsgResult);

        AggTransactionDao oTransDao = new AggTransactionDao();
        oTransDao.save(oTrans);

        if ((oTrans != null) &&
                (oTrans.getTransactionId() != null) &&
                (oTrans.getTransactionId().length() > 0)) {
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
     * @param sRetrieveDocumentSetResponseXML The RetrieveDocumentSetResponse in XML form.
     * @return The status.  "SUCCESS" or "FAIL".
     */
    public String setResponseMsg(String sTransactionId, DocRetrieveMessageKey oKey, String sRetrieveDocumentSetResponseXML) {
        String sStatus = DocumentConstants.SUCCESS_TEXT;
        String sMessageKey = oKey.createXMLMessageKey();

        AggMessageResultDao oAggMessageResultDao = new AggMessageResultDao();
        AggMessageResult oMsgResult = null;
        try {
            oMsgResult = oAggMessageResultDao.findByMessageKey(sTransactionId, sMessageKey);
        } catch (Exception e) {
            String sErrorMessage = "Failed to retrieve AggMessageResult for: TransactionId: " + sTransactionId +
                    ", MessageKey: " + oKey.createXMLMessageKey() + ".  Message: " + e.getMessage();
            log.error(sErrorMessage, e);
            sStatus = DocumentConstants.FAIL_TEXT;
            return sStatus;         // No reason to proceed....
        }

        if (oMsgResult == null) {
            String sErrorMessage = "Failed to find existing AggMessageResult for: TransactionId: " + sTransactionId +
                    ", MessageKey: " + oKey.createXMLMessageKey() + ".  Message response not recorded.";
            log.error(sErrorMessage);
            sStatus = DocumentConstants.FAIL_TEXT;
            return sStatus;         // No reason to proceed....
        }

        // if we got here - we have the message and we need to fill in the response information...
        //----------------------------------------------------------------------------------------
        oMsgResult.setResponseReceivedTime(new Date());
        oMsgResult.setResponseMessage(sRetrieveDocumentSetResponseXML);
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
    private boolean areResultsReady(AggTransaction oTrans) {
        if (oTrans == null) {
            return false;
        }

        for (AggMessageResult oMsgResult : oTrans.getAggMessageResults()) {
            // If we fall through - we are still waiting for results.
            //-------------------------------------------------------
            if ((oMsgResult != null) &&
                    (oMsgResult.getResponseReceivedTime() == null)) {
                return false;
            }
        }


        return true;
    }

    /**
     * This method will create an empty DocumentSetResponseType.  One that would
     * be returned if there were no results.
     * 
     * @return An AdhocQueryResponse that represents an empty result set.
     */
    private RetrieveDocumentSetResponseType createEmptyResult() {
        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        oRetrieveDocumentSetResponse.setRegistryResponse(new RegistryResponseType());

        oRetrieveDocumentSetResponse.getRegistryResponse().setStatus(DocumentConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);

        return oRetrieveDocumentSetResponse;
    }

    /**
     * This method is used to combine the results together into a single 
     * RetrieveDocumentSetResponse.  It will extract the items in the DocumentResponse
     * from the various systems and place them into the new response.  It also extracts
     * any errors in RegistryErrorList and puts them in the correct location.
     * 
     * @param olMsgResult List of messages to be combined
     * @return The combined RetrieveDocumentSetResponse object.
     */
    private RetrieveDocumentSetResponseType combineResults(Set<AggMessageResult> olMsgResult) {
        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = createEmptyResult();
        List<DocumentResponse> olDocResponse = oRetrieveDocumentSetResponse.getDocumentResponse();

        boolean filesUsed = false;

        for (AggMessageResult oMsgResult : olMsgResult) {
            // If the message contains a response received time, then it means that we
            // received the response from the remote system.  We need to aggregate the
            // results.
            //--------------------------------------------------------------------------
            if (oMsgResult.getResponseReceivedTime() != null) {
                // The response may have been empty - if so - there is nothing to aggregate from here.
                //-------------------------------------------------------------------------------------
                if ((oMsgResult.getResponseMessage() != null) &&
                        (oMsgResult.getResponseMessage().trim().length() > 0)) {
                    // Check to see if the response is the message or a file URL
                    URI uri = null;

                    String resultMsg = null;

                    try {
                        uri = new URI(oMsgResult.getResponseMessage());
                        File sourceFile = new File(uri);

                        try {
                            FileInputStream in = new FileInputStream(sourceFile);
                            log.info("Opened Large File: " + oMsgResult.getResponseMessage());

                            Long longObj = new Long(sourceFile.length());
                            byte[] buffer = new byte[longObj.intValue()];
                            in.read(buffer);

                            String textMsg = new String(buffer);
                            resultMsg = textMsg;

                            // Delete the file after reading the contents
                            log.debug("Trying to delete file..." + sourceFile.getPath());
                            in.close();

                            filesUsed = true;

                        } catch (FileNotFoundException ex) {
                            log.error("File Not Found: " + sourceFile.getName() + ". " + ex.getMessage());
                            resultMsg = null;
                        } catch (IOException ex) {
                            log.error("Failed to read contents of the file : " + sourceFile.getName() + ". " + ex.getMessage());
                            resultMsg = null;
                        }
                    } catch (URISyntaxException e) {
                        resultMsg = oMsgResult.getResponseMessage();
                    }

                    RetrieveDocumentSetResponseType oTempResponse = unmarshalAdhocQueryResponse(resultMsg);
                    if ((oTempResponse != null) &&
                            (oTempResponse.getDocumentResponse() != null) &&
                            (oTempResponse.getDocumentResponse().size() > 0)) {
                        List<DocumentResponse> olNewDocResponse = oTempResponse.getDocumentResponse();

                        for (DocumentResponse oNewDocResponse : olNewDocResponse) {
                            olDocResponse.add(oNewDocResponse);
                        }   // for (DocumentResponse oNewDocResponse : olNewDocResponse)
                    }   // if ((oTempResponse != null) &&

                    // It is possible that there may be error information in this message
                    // that we need to pull out too...
                    //--------------------------------------------------------------------
                    if ((oTempResponse != null) &&
                            (oTempResponse.getRegistryResponse() != null) &&
                            (oTempResponse.getRegistryResponse().getRegistryErrorList() != null) &&
                            (oTempResponse.getRegistryResponse().getRegistryErrorList().getRegistryError().size() > 0)) {
                        RegistryErrorList oRegErrors = null;
                        if (oRetrieveDocumentSetResponse.getRegistryResponse().getRegistryErrorList() == null) {
                            oRegErrors = new RegistryErrorList();
                            oRetrieveDocumentSetResponse.getRegistryResponse().setRegistryErrorList(oRegErrors);
                        } else {
                            oRegErrors = oRetrieveDocumentSetResponse.getRegistryResponse().getRegistryErrorList();
                        }

                        List<RegistryError> olRegError = oRegErrors.getRegistryError();
                        for (RegistryError oRegError : oTempResponse.getRegistryResponse().getRegistryErrorList().getRegistryError()) {
                            olRegError.add(oRegError);
                        }
                    }   // if ((oTempResponse != null) &&
                }   // if ((oMsgResult.getResponseMessage() != null) && ...
            } // if (oMsgResult.getResponseReceivedTime() != null)
            else // This means that this result never received a response - log an error that this one timed out.
            {
                RegistryErrorList oRegErrors = null;
                if (oRetrieveDocumentSetResponse.getRegistryResponse().getRegistryErrorList() == null) {
                    oRegErrors = new RegistryErrorList();
                    oRetrieveDocumentSetResponse.getRegistryResponse().setRegistryErrorList(oRegErrors);
                } else {
                    oRegErrors = oRetrieveDocumentSetResponse.getRegistryResponse().getRegistryErrorList();
                }

                List<RegistryError> olRegError = oRegErrors.getRegistryError();
                RegistryError oRegError = new RegistryError();
                olRegError.add(oRegError);
                oRegError.setErrorCode(DocumentConstants.XDS_RETRIEVE_ERRORCODE_REPOSITORY_ERROR);
                oRegError.setCodeContext(DocumentConstants.XDS_RETRIEVE_CODECONTEXT_TIMEDOUT_MSG);
                if ((oMsgResult.getMessageKey() != null) &&
                        (oMsgResult.getMessageKey().length() > 0)) {
                    try {
                        DocRetrieveMessageKey oMessageKey = new DocRetrieveMessageKey(oMsgResult.getMessageKey());
                        oRegError.setLocation(oMessageKey.getHomeCommunityId());
                    } catch (Exception e) {
                        String sErrorMessage = "Failed to parse message key.  Message = " + e.getMessage();
                        log.error(sErrorMessage, e);
                        //  do not throw an error - we will log it and move on...
                    }
                }   // if ((oMsgResult.getMessageKey() != null) &&
            }   // else ...

            if (filesUsed == true) {
                if (oMsgResult != null &&
                        oMsgResult.getAggTransaction() != null &&
                        NullChecker.isNotNullish(oMsgResult.getAggTransaction().getTransactionId())) {
                    deleteTempFiles(oMsgResult.getAggTransaction().getTransactionId());
                }
                else {

                }
            }
        }   // for (AggMessageResult oMsgResult : olMsgResult)



        return oRetrieveDocumentSetResponse;
    }

    /**
     * This method returns either an aggregated RetrieveDocumentSetResponse if all results
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
    public RetrieveDocumentSetResponseType getAggResults(String sTransactionId, boolean bTimedOut)
            throws AggregatorException {
        RetrieveDocumentSetResponseType oResponse = null;

        // Retrieve the records and see if everything is ready...
        //--------------------------------------------------------
        AggTransactionDao oAggTransactionDao = new AggTransactionDao();
        AggTransaction oTrans = oAggTransactionDao.findById(sTransactionId);
        if (oTrans == null) {
            String sErrorMessage = "Failed to find an aggregator transaction for TransactionId: " +
                    sTransactionId;
            log.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        // Make sure this transaction ID is for the right type of transaction.
        //--------------------------------------------------------------------
        if ((oTrans.getServiceType() == null) ||
                ((oTrans.getServiceType() != null) &&
                (!oTrans.getServiceType().equals(DOC_RETRIEVE_NAME)))) {
            String sErrorMessage = "The specified TransactionId: " + sTransactionId + " is not associated with " +
                    " Document Retrieve messages.  It is for: " + oTrans.getServiceType() + ".";
            log.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        // If we have timed out, or if the results are ready then aggregate them 
        // and return them.
        //----------------------------------------------------------------------
        if ((bTimedOut) ||
                (areResultsReady(oTrans))) {
            if ((oTrans.getAggMessageResults() != null) &&
                    (oTrans.getAggMessageResults().size() > 0)) {
                oResponse = combineResults(oTrans.getAggMessageResults());
            } else {
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

    private File createDestDirectory(String dirName) {
        File destDirectory = new File(dirName);

        if (!destDirectory.exists()) {
            if (!destDirectory.mkdirs()) {
                log.error("Failed to create directory to transfer file to: " + destDirectory);
                return null;
            }
        }

        // Make sure the directory is writable
        if (!(destDirectory.setWritable(true, false))) {
            log.error("Unabled to set " + destDirectory + " to writable");
            return null;
        }

        return destDirectory;
    }

    private String getLargeFileDirProp() {
        String destDir = null;
        try {
            destDir = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AGGREGATOR_LARGE_RESP_DIR_PROP);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AGGREGATOR_LARGE_RESP_DIR_PROP + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return destDir;
    }

    private long getLargeFileSizeProp() {
        long size = 0;
        try {
            size = PropertyAccessor.getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AGGREGATOR_LARGE_RESP_SIZE_PROP);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AGGREGATOR_LARGE_RESP_SIZE_PROP + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return size;
    }

    private String generateFileName() {
        Date timestamp = new Date();
        String fileName = DOC_RETRIEVE_RESPONSE + timestamp.getTime() + ".xml";

        return fileName;
    }

    private void deleteTempFiles(String transactionId) {
        String baseDir = getLargeFileDirProp();
        String dirPath = baseDir + "/" + transactionId;

        File path = new File(dirPath);

        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
        path.delete();

    }
}
