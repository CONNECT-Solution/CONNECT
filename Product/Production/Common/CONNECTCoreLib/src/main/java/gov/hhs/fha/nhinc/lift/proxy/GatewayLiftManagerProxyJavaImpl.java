/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.proxy;

import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;

import gov.hhs.fha.nhinc.lift.common.util.ClientDataToken;
import gov.hhs.fha.nhinc.lift.dao.GatewayLiftMessageDao;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;

import gov.hhs.fha.nhinc.lift.common.util.LiftMessage;
import gov.hhs.fha.nhinc.lift.common.util.DataToken;
import gov.hhs.fha.nhinc.lift.common.util.InterProcessSocketProtocol;
import gov.hhs.fha.nhinc.lift.common.util.JaxbUtil;
import gov.hhs.fha.nhinc.lift.common.util.RequestToken;
import gov.hhs.fha.nhinc.lift.common.util.ServerProxyDataToken;

import gov.hhs.fha.nhinc.lift.common.util.cleanup.GatewayLiFTRecordMonitor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Blob;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


/**
 * This is the no-op implementation of the GatewayLiftManager.
 *
 * @author Les Westberg
 */
public class GatewayLiftManagerProxyJavaImpl implements GatewayLiftManagerProxy
{

    private Log log = null;
    private static final String MESSAGE_STATE_ENTERED = "ENTERED";
    private static final String MESSAGE_STATE_PROCESSING = "PROCESSING";

    public GatewayLiftManagerProxyJavaImpl()
    {
        log = createLogger();
    }

    /**
     * Create a logger object.
     *
     * @return The logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     * Returns a handle to the GatewayLiftMessageDao object.
     * 
     * @return The handle to the GatewayLiftMessageDao object.
     */
    protected GatewayLiftMessageDao getGatewayLiftMessageDao()
    {
        return new GatewayLiftMessageDao();
    }

    /**
     * This method reads the record specified by the requestKeyGuid from the
     * database.
     * 
     * @param sRequestKeyGuid The request key GUID for the record to be read.
     * @return The record to be read.
     */
    protected GatewayLiftMsgRecord readRecord(String sRequestKeyGuid)
    {
        GatewayLiftMsgRecord oRecord = null;

        if ((sRequestKeyGuid != null) && (sRequestKeyGuid.length() > 0))
        {
            GatewayLiftMessageDao oDao = getGatewayLiftMessageDao();
            oRecord = oDao.queryByRequestKeyGuid(sRequestKeyGuid);
        }

        return oRecord;
    }

    /**
     * This method updates the record in the GATEWAY_LIFT_MESSAGE table
     * to set the state to processing and to add the processing start timestamp.
     *
     * @param oRecord The record to be updated.
     */
    protected void setRecordToProcessing(GatewayLiftMsgRecord oRecord)
    {
        oRecord.setMessageState(MESSAGE_STATE_PROCESSING);
        oRecord.setProcessingStartTimestamp(new Date());

        GatewayLiftMessageDao oDao = getGatewayLiftMessageDao();
        oDao.updateRecord(oRecord);
    }

    /**
     * This method deletes the specified record from the GATEWAY_LIFT_MESSAGE
     * table.
     *
     * @param oRecord The record to be deleted.
     */
    protected void deleteRecord(GatewayLiftMsgRecord oRecord)
    {
        GatewayLiftMessageDao oDao = getGatewayLiftMessageDao();
        oDao.deleteRecord(oRecord);
    }

    /**
     * Checks the values of the record to make sure the record is in the correct
     * state.  If it is then it returns true.  It returns false if it is not.
     * The correct state is that it must be in the "ENTERED' state with a non null
     * request key GUID.
     *
     * @param oRecord The record that is being checked.
     * @return true if the record is in the correct state.
     */
    protected boolean recordInCorrectState(GatewayLiftMsgRecord oRecord)
    {
        if ((oRecord != null) &&
            (oRecord.getMessageState() != null) &&
            (oRecord.getMessageState().equals(MESSAGE_STATE_ENTERED)) &&
            (oRecord.getRequestKeyGuid() != null) &&
            (oRecord.getRequestKeyGuid().length() > 0) &&
            (oRecord.getFileNameToRetrieve() != null) &&
            (oRecord.getFileNameToRetrieve().length() > 0) &&
            (oRecord.getProducerProxyAddress() != null) &&
            (oRecord.getProducerProxyAddress().length() > 0) &&
            (oRecord.getProducerProxyPort() != null) &&
            (oRecord.getProducerProxyPort().longValue() > 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This method sends a message to the given output stream.
     *
     * @param sMessage The message to be sent.
     * @param oSocket The socket which contains the output stream to receive the message.
     * @throws java.io.IOException The exception that is thrown if the message cannot
     *                             be delivered.
     */
    protected void sendMessageToSocket(String sMessage, Socket oSocket)
        throws java.io.IOException
    {
        InterProcessSocketProtocol protocol = new InterProcessSocketProtocol();
        protocol.sendData(sMessage, oSocket.getOutputStream());
    }

    /**
     * Retrieve the client IP address from the gateway properties file.
     *
     * @return The client IP address.
     */
    protected String getClientIP()
        throws PropertyAccessException
    {
        return PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_IP);
    }

    /**
     * Retrieve the client port from the gateway properties file.
     *
     * @return the client port.
     */
    protected String getClientPort()
        throws PropertyAccessException
    {
        return PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_CLIENT_PORT);
    }

    /**
     * This method creates a socket for the specified address.
     *
     * @param oSocketAddr The address for the socket.
     * @param iPort The port for the socket.
     * @return The socket that was created.
     * @throws java.net.UnknownHostException
     * @throws java.io.IOException
     */
    protected Socket getSocket(InetAddress oSocketAddr, int iPort)
        throws java.net.UnknownHostException, java.io.IOException
    {
        return new Socket(oSocketAddr, iPort);
    }

    /**
     * This method starts an Http transaction with the Lift Client Manager
     * Controller.
     *
     * @param oRecord The information needed to start the Lift transaction.
     */
    protected void startHttpTransaction(GatewayLiftMsgRecord oRecord)
        throws java.io.IOException, java.lang.Exception
    {
        InetSocketAddress caddr = null;
        Socket clientSocket = null;

        try
        {
            // client socket should be started
            //--------------------------------
            String clientIP = getClientIP();
            String clientPort = getClientPort();
            caddr = new InetSocketAddress(clientIP, Integer.parseInt(clientPort));
            clientSocket = getSocket(caddr.getAddress(), caddr.getPort());

            // client expects a LiftMessage type so we create one
            //----------------------------------------------------
            LiftMessage message = new LiftMessage();

            RequestToken request = new RequestToken(oRecord.getRequestKeyGuid());
            message.setRequest(request);

            DataToken data = new DataToken();
            ClientDataToken cdata = new ClientDataToken();
            cdata.setData(oRecord.getFileNameToRetrieve());
            data.setClientData(cdata);

            ServerProxyDataToken sdata = new ServerProxyDataToken();
            String serverIP = oRecord.getProducerProxyAddress();
            String serverPort = oRecord.getProducerProxyPort().toString();
            InetSocketAddress saddr = new InetSocketAddress(serverIP, Integer.parseInt(serverPort));
            sdata.setServerProxyAddress(saddr.getAddress().getHostAddress());
            sdata.setServerProxyPort(saddr.getPort());
            data.setServerProxyData(sdata);
            message.setData(data);

            String content = JaxbUtil.marshalToString(message);

            // Send the message to server.
            //-----------------------------
            System.out.println("Attempt to send: " + content);
            sendMessageToSocket(content, clientSocket);
        }
        catch (IOException eIO)
        {
            String sErrorMessage = "Failed to connect to client socket.  Error: " + eIO.getMessage();
            log.error(sErrorMessage);
            throw new Exception(sErrorMessage, eIO);
        }
        catch (PropertyAccessException ePA)
        {
            String sErrorMessage = "Failed to retrieve client socket IP and port properties from gateway.properties file.  Error: " + ePA.getMessage();
            log.error(sErrorMessage);
            throw new Exception(sErrorMessage, ePA);
        }
        catch (Throwable t)
        {
            String sErrorMessage = "An unexpected error occurred while sending message to LiFT Client Manager Controller.  Error: " + t.getMessage();
            log.error(sErrorMessage);
            throw new Exception(sErrorMessage, t);
        }
        finally
        {
            if (clientSocket != null)
            {
                clientSocket.close();
                clientSocket = null;
            }
        }
    }

    /**
     * This method starts a lift transaction.
     *
     * @param startLiftTransactionRequest The information needed to start a lift transaction.
     * @return The status of the request to start the transaction.  The really only valid answer is "SUCCESS".  Any other situation
     *         should throw an exception.
     */
    @Override
    public StartLiftTransactionResponseType startLiftTransaction(StartLiftTransactionRequestType startLiftTransactionRequest)
    {
        StartLiftTransactionResponseType oResponse = new StartLiftTransactionResponseType();

        if ((startLiftTransactionRequest != null) &&
                (startLiftTransactionRequest.getRequestKeyGuid() != null) &&
                (startLiftTransactionRequest.getRequestKeyGuid().length() > 0))
        {
            String sRequestKeyGuid = startLiftTransactionRequest.getRequestKeyGuid();
            GatewayLiftMsgRecord oRecord = readRecord(sRequestKeyGuid);
            if (oRecord != null)
            {
                if (recordInCorrectState(oRecord))
                {
                    try
                    {
                        startHttpTransaction(oRecord);
                        setRecordToProcessing(oRecord);
                        oResponse.setStatus("SUCCESS");
                    }
                    catch (Exception e)
                    {
                        String sErrorMessage = "Failed to send message for RequestKeyGuid: " + sRequestKeyGuid +
                                               "to LiFT Client Manager Controller.  Error: " + e.getMessage();
                        log.error(sErrorMessage, e);
                        oResponse.setStatus("FAILED: " + sErrorMessage);
                    }
                }
                else
                {
                    // The record was not in the correct state to be started.
                    //---------------------------------------------------------
                    String sErrorMessage = "Incorrect message state for GATEWAY_LIFT_MESSAGE Record with RequestKeyGuid: " + sRequestKeyGuid +
                            " It should have been '" + MESSAGE_STATE_ENTERED + "'.";
                    log.error(sErrorMessage);
                    oResponse.setStatus("FAILED: " + sErrorMessage);
                }
            }
            else
            {
                // There was no record for this transfer.  Log a message and return "FAILED"
                //---------------------------------------------------------------------------
                String sErrorMessage = "Tried to retrieve RequestKeyGuid: " + sRequestKeyGuid + ".  It did not exist in the database.";
                log.error(sErrorMessage);
                oResponse.setStatus("FAILED: " + sErrorMessage);
            }
        }
        else
        {
            // There was no record for this transfer.  Log a message and return "FAILED"
            //---------------------------------------------------------------------------
            String sErrorMessage = "Tried to retrieve RequestKeyGuid: null.  It did not exist in the database.";
            log.error(sErrorMessage);
            oResponse.setStatus("FAILED: " + sErrorMessage);
        }

        startCleanupMonitorService();

        return oResponse;
    }

    /**
     * This verifies the CompleteLiftTransactionRequestType to make sure it has
     * all the data it is supposed to have.
     *
     * @param oRequest The request parameter to be verified.
     * @return TRUE if it is all in order.
     */
    protected boolean completionRequestContainsValidData(CompleteLiftTransactionRequestType oRequest)
    {
        if ((oRequest != null) &&
            (oRequest.getRequestKeyGuid() != null) &&
            (oRequest.getRequestKeyGuid().length() > 0) &&
            (oRequest.getFileURI() != null) &&
            (oRequest.getFileURI().length() > 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This verifies the failedLiftTransactionRequestType to make sure it has
     * all the data it is supposed to have.
     *
     * @param oRequest The request parameter to be verified.
     * @return TRUE if it is all in order.
     */
    protected boolean failedRequestContainsValidData(FailedLiftTransactionRequestType oRequest)
    {
        if ((oRequest != null) &&
            (oRequest.getRequestKeyGuid() != null) &&
            (oRequest.getRequestKeyGuid().length() > 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * Return the message blob from the GatewayLiftMsgRecord.  This is a helper class
     * so that we can mock the Blob in unit tests.
     *
     * @param oRecord The record containing the message blob.
     * @return The message blob.
     */
    protected Blob getMessageBlob(GatewayLiftMsgRecord oRecord)
    {
        return (oRecord != null) ? oRecord.getMessage() : null;
    }

    /**
     * Return the length of a blob.  This is a helper class
     * so that we can mock the Blob in unit tests.
     *
     * @param oRecord The record containing the message blob.
     * @return The message blob.
     */
    protected long getBlobLength(Blob oBlob)
        throws java.sql.SQLException
    {
        return (oBlob != null) ? oBlob.length() : 0;
    }

    /**
     * Return the assertion blob from the GatewayLiftMsgRecord.  This is a helper class
     * so that we can mock the Blob in unit tests.
     *
     * @param oRecord The record containing the assertion blob.
     * @return The assertion blob.
     */
    protected Blob getAssertionBlob(GatewayLiftMsgRecord oRecord)
    {
        return (oRecord != null) ? oRecord.getMessage() : null;
    }

    /**
     * This extracts the binary input stream from a blob.
     *
     * @param oBlob The blob containing the binary input stream.
     * @return The InputStream object from the blob.
     */
    protected InputStream getBlobBinaryInputStream(Blob oBlob)
        throws java.sql.SQLException
    {
        return (oBlob != null) ? oBlob.getBinaryStream() : null;
    }

    /**
     * This method checks to see if the record is valid for completion.
     *
     * @param oRecord The gateway lift message record that is being checked.
     * @return TRUE if it is valid.
     */
    protected boolean recordValidForCompletion(GatewayLiftMsgRecord oRecord)
        throws java.sql.SQLException
    {
        if ((oRecord != null) &&
            (getMessageBlob(oRecord) != null) &&
            (getBlobLength(getMessageBlob(oRecord)) > 0) &&
            (getAssertionBlob(oRecord) != null) &&
            (getBlobLength(getAssertionBlob(oRecord)) > 0))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This method extracts a string from a blob.
     *
     * @param oBlob The blob containing the string.
     * @return The string from the blob.
     */
    protected String extractStringFromBlob(Blob oBlob)
        throws java.sql.SQLException, java.io.IOException
    {
        String sResult = "";

        if ((oBlob != null) &&
            (getBlobLength(oBlob) > 0))
        {
            int iLen = (int) getBlobLength(oBlob);
            byte[] byteResult = new byte[iLen];
            InputStream oInputStream = getBlobBinaryInputStream(oBlob);
            oInputStream.read(byteResult);
            sResult = new String(byteResult);
        }

        return sResult;
    }

    /**
     * This returns a handle to the AdapterXDRRequest proxy object.
     * 
     * @return The handle to the AdapterXDRRequest proxy object.
     */
    protected AdapterDocSubmissionDeferredRequestProxy getAdapterDocSubmissionDeferredRequestProxyObject()
    {
        AdapterDocSubmissionDeferredRequestProxyObjectFactory oFactory = new AdapterDocSubmissionDeferredRequestProxyObjectFactory();
        AdapterDocSubmissionDeferredRequestProxy oAdapterXDRRequestService = oFactory.getAdapterDocSubmissionDeferredRequestProxy();
        return oAdapterXDRRequestService;
    }

    /**
     * This returns a handle to the AdapterXDRRequest proxy object.
     *
     * @return The handle to the AdapterXDRRequest proxy object.
     */
    protected AdapterDocSubmissionDeferredRequestErrorProxy getAdapterDocSubmissionDeferredRequestErrorProxyObject()
    {
        AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory oFactory = new AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory();
        AdapterDocSubmissionDeferredRequestErrorProxy oAdapterXDRRequestErrorService = oFactory.getAdapterDocSubmissionDeferredRequestErrorProxy();
        return oAdapterXDRRequestErrorService;
    }


    /**
     * This method takes the assertion and deserializes the XML into an
     * Assertion object.
     *
     * @param sAssertion The string version of the assertion class.
     * @return The object form of the Assertion.
     */
    protected AssertionType deserializeAssertion(String sAssertion)
        throws JAXBException
    {
        AssertionType oAssertion = null;

        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
        Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

        StringReader srXML = new StringReader(sAssertion);

        JAXBElement oJAXBElementConsent = (JAXBElement) oUnmarshaller.unmarshal(srXML);
        if (oJAXBElementConsent.getValue() instanceof AssertionType)
        {
            oAssertion = (AssertionType) oJAXBElementConsent.getValue();
        }

        return oAssertion;
    }

    /**
     * This method takes the XML version of the request message and
     * deserializes it into an object verison of the data.
     *
     * @param sMessage The message in XML form.
     * @return The message in object form.
     */
    protected ProvideAndRegisterDocumentSetRequestType deserializeMessage(String sMessage)
        throws JAXBException
    {
        ProvideAndRegisterDocumentSetRequestType oRequest = null;

        JAXBContextHandler oHandler = new JAXBContextHandler();
        JAXBContext oJaxbContext = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
        Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

        StringReader srXML = new StringReader(sMessage);

        JAXBElement oJAXBElementConsent = (JAXBElement) oUnmarshaller.unmarshal(srXML);
        if (oJAXBElementConsent.getValue() instanceof ProvideAndRegisterDocumentSetRequestType)
        {
            oRequest = (ProvideAndRegisterDocumentSetRequestType) oJAXBElementConsent.getValue();
        }

        return oRequest;
    }

    /**
     * This method sends the message to the adapter.
     *
     * @param oMessageBlob The message to be sent to the adapter.
     * @param oAssertionBlob The assertion to be sent to the adapter.
     * @param sFileURI The file URI to be sent to the adapter.
     */
    protected void sendMessageToAdapter(Blob oMessageBlob, Blob oAssertionBlob, String sFileURI)
        throws java.sql.SQLException, java.io.IOException, JAXBException
    {
        String sMessage = extractStringFromBlob(oMessageBlob);
        String sAssertion = extractStringFromBlob(oAssertionBlob);
        AssertionType oAssertion = null;
        ProvideAndRegisterDocumentSetRequestType oMessage = null;

        try
        {
            oAssertion = deserializeAssertion(sAssertion);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the assertion string to an AssertionType object.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new JAXBException(sErrorMessage, e);
        }

        try
        {
            oMessage = deserializeMessage(sMessage);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the message string to a ProvideAndRegisterDocumentSetRequestType object.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new JAXBException(sErrorMessage, e);
        }

        AdapterDocSubmissionDeferredRequestProxy oAdapterXDRRequestService = getAdapterDocSubmissionDeferredRequestProxyObject();
        XDRAcknowledgementType oAck = oAdapterXDRRequestService.provideAndRegisterDocumentSetBRequest(oMessage, sFileURI, oAssertion);

        // not sure what to do with the ACK at this point.  The message already went back to the initiating gateway when we
        // queued the request.  So at this point we will do nothing with it.
        //-------------------------------------------------------------------------------------------------------------------
    }

    /**
     * This method sends the error message to the adapter.
     *
     * @param oMessageBlob The message to be sent to the adapter.
     * @param oAssertionBlob The assertion to be sent to the adapter.
     * @param sLiftErrorMessage The file URI to be sent to the adapter.
     */
    protected void sendErrorToAdapter(Blob oMessageBlob, Blob oAssertionBlob, String sLiftErrorMessage)
        throws java.sql.SQLException, java.io.IOException, JAXBException
    {
        String sMessage = extractStringFromBlob(oMessageBlob);
        String sAssertion = extractStringFromBlob(oAssertionBlob);
        AssertionType oAssertion = null;
        ProvideAndRegisterDocumentSetRequestType oMessage = null;

        try
        {
            oAssertion = deserializeAssertion(sAssertion);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the assertion string to an AssertionType object.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new JAXBException(sErrorMessage, e);
        }

        try
        {
            oMessage = deserializeMessage(sMessage);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the message string to a ProvideAndRegisterDocumentSetRequestType object.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new JAXBException(sErrorMessage, e);
        }

        AdapterDocSubmissionDeferredRequestErrorProxy oAdapterXDRRequestErrorService = getAdapterDocSubmissionDeferredRequestErrorProxyObject();
        XDRAcknowledgementType oAck = oAdapterXDRRequestErrorService.provideAndRegisterDocumentSetBRequestError(oMessage, sLiftErrorMessage, oAssertion);

        // not sure what to do with the ACK at this point.  The message already went back to the initiating gateway when we
        // queued the request.  So at this point we will do nothing with it.
        //-------------------------------------------------------------------------------------------------------------------
    }


    /**
     * This method tells the Gateway Lift Manager that a lift transaction has been completed successfully.
     *
     * @param completeLiftTransactionRequest The information needed to start a lift transaction.
     * @return The status of the request to start the transaction.  The really only valid answer is "SUCCESS".  Any other situation
     *         would be either an exception or the failedLiftTransaction method should be called.
     */
    public CompleteLiftTransactionResponseType completeLiftTransaction(CompleteLiftTransactionRequestType completeLiftTransactionRequest)
    {
        CompleteLiftTransactionResponseType oResponse = new CompleteLiftTransactionResponseType();

        if (completionRequestContainsValidData(completeLiftTransactionRequest))
        {
            try
            {
                String sRequestKeyGuid = completeLiftTransactionRequest.getRequestKeyGuid();
                GatewayLiftMsgRecord oRecord = readRecord(sRequestKeyGuid);
                if (recordValidForCompletion(oRecord))
                {
                    sendMessageToAdapter(oRecord.getMessage(), oRecord.getAssertion(), completeLiftTransactionRequest.getFileURI());
                    deleteRecord(oRecord);
                    oResponse.setStatus("SUCCESS");
                }
                else
                {
                    // The record did not have the required data: Message and/or Assertion.
                    //---------------------------------------------------------
                    String sErrorMessage = "The record in GATEWAY_LIFT_MESSAGE Record with RequestKeyGuid: " + sRequestKeyGuid +
                            " did not contain both a message and assertion blob.'";
                    log.error(sErrorMessage);
                    oResponse.setStatus("FAILED: " + sErrorMessage);
                }
            }
            catch (Exception e)
            {
                // An unexpected exception has occurred.
                //--------------------------------------
                String sErrorMessage = "An unexpected exception has occurred.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                oResponse.setStatus("FAILED: " + sErrorMessage);
            }
        }
        else
        {
            // The information passed in was insufficient to complete this.
            //-------------------------------------------------------------
            String sErrorMessage = "Failed to complete LiftTransaction, either the RequestKeyGuid or fileURI was not passed.";
            log.error(sErrorMessage);
            oResponse.setStatus("FAILED: " + sErrorMessage);
        }

        return oResponse;
    }

    /**
     * This method tells the Gateway Lift Manager that a lift transaction has failed.
     *
     * @param failedLiftTransactionRequest The information needed to start a lift transaction.
     * @return The status of the request to start the transaction.  The really only valid answer is "SUCCESS".  Any other situation
     *         would be either an exception.
     */
    public FailedLiftTransactionResponseType failedLiftTransaction(FailedLiftTransactionRequestType failedLiftTransactionRequest)
    {
        FailedLiftTransactionResponseType oResponse = new FailedLiftTransactionResponseType();
        
        if (failedRequestContainsValidData(failedLiftTransactionRequest))
        {
            try
            {
                String sRequestKeyGuid = failedLiftTransactionRequest.getRequestKeyGuid();
                GatewayLiftMsgRecord oRecord = readRecord(sRequestKeyGuid);
                if (recordValidForCompletion(oRecord))
                {
                    sendErrorToAdapter(oRecord.getMessage(), oRecord.getAssertion(), failedLiftTransactionRequest.getErrorMessage());
                    deleteRecord(oRecord);
                    oResponse.setStatus("SUCCESS");
                }
                else
                {
                    // The record did not have the required data: Message and/or Assertion.
                    //---------------------------------------------------------
                    String sErrorMessage = "The record in GATEWAY_LIFT_MESSAGE Record with RequestKeyGuid: " + sRequestKeyGuid +
                            " did not contain both a message and assertion blob.'";
                    log.error(sErrorMessage);
                    oResponse.setStatus("FAILED: " + sErrorMessage);
                }
            }
            catch (Exception e)
            {
                // An unexpected exception has occurred.
                //--------------------------------------
                String sErrorMessage = "An unexpected exception has occurred.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                oResponse.setStatus("FAILED: " + sErrorMessage);
            }
        }
        else
        {
            // The information passed in was insufficient to complete this.
            //-------------------------------------------------------------
            String sErrorMessage = "Failed to handle failedLiftTransaction, the RequestKeyGuid was not passed.";
            log.error(sErrorMessage);
            oResponse.setStatus("FAILED: " + sErrorMessage);
        }

        return oResponse;
    }

    /**
     * Start the cleanup monitor service thread. This will clean up stale transactions that have not completed
     * processing for some reason.
     */
    protected void startCleanupMonitorService()
    {
        GatewayLiFTRecordMonitor monitor = getGatewayLiFTRecordMonitor();
        if(monitor != null)
        {
            log.debug("Starting the GatewayLiFTRecordMonitor");
            monitor.start();
        }
        else
        {
            log.warn("GatewayLiFTRecordMonitor was null.");
        }
    }

    /**
     * Get an instance o the cleanup monitor thread.
     *
     * @return Cleanup monitor thread instance
     */
    protected GatewayLiFTRecordMonitor getGatewayLiFTRecordMonitor()
    {
        return new GatewayLiFTRecordMonitor();
    }

}
