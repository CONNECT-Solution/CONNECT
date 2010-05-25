package gov.hhs.fha.nhinc.lift.proxy;

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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        InterProcessSocketProtocol.sendData(sMessage, oSocket.getOutputStream());
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

        return oResponse;
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
        oResponse.setStatus("SUCCESS");
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
        oResponse.setStatus("SUCCESS");
        return oResponse;
    }
}
