/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request;

import gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy.AdapterXDRRequestProxy;
import gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy.AdapterXDRRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.XDRPolicyChecker;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType;
import gov.hhs.fha.nhinc.adapter.xdr.async.request.error.proxy.AdapterXDRRequestErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.adapter.xdr.async.request.error.proxy.AdapterXDRRequestErrorProxy;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.lift.dao.GatewayLiftMessageDao;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.lift.payload.builder.LiFTPayloadBuilder;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxy;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import gov.hhs.fha.nhinc.lift.utils.LiFTMessageHelper;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.LIFTMessageType;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import org.hibernate.Hibernate;

/**
 *
 * @author patlollav
 */
public class NhinXDRRequestImpl {

    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed";
    private static final Log logger = LogFactory.getLog(NhinXDRRequestImpl.class);

    /**
     *
     * @return
     */
    protected Log getLogger() {
        return logger;
    }

    /**
     *
     * @param body
     * @param context
     * @return
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {

        XDRAcknowledgementType result = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        result.setMessage(regResp);

        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        AssertionType assertion = createAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setAsyncMessageId(extractMessageId(context));
        }

        AcknowledgementType ack = getXDRAuditLogger().auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        getLogger().debug("Audit Log Ack Message:" + ack.getMessage());

        String localHCID = retrieveHomeCommunityID();

        getLogger().debug("Local Home Community ID: " + localHCID);

        if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID)) {
            getLogger().debug("Policy Check Succeeded");

            // Check to see if this message contains a LiFT Payload and that we support LiFT
            if (isLiftMessage(body) && checkLiftProperty()) {
                result = processLiftMessage(body, assertion);
            } else {
                result = forwardToAgency(body, assertion);
            }
        } else {
            getLogger().error("Policy Check Failed");
            result = sendErrorToAgency(body, assertion, "Policy Check Failed");
        }

        ack = getXDRAuditLogger().auditAcknowledgement(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION);

        getLogger().debug("Audit Log Ack Message for Outbound Acknowledgement:" + ack.getMessage());

        logger.debug("Exiting provideAndRegisterDocumentSetBRequest");

        return result;

    }

    /**
     *
     * @param context
     * @return
     */
    protected AssertionType createAssertion(WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        return assertion;
    }

    /**
     *
     * @return
     */
    protected String retrieveHomeCommunityID() {
        String localHCID = null;
        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            logger.error("Exception while retrieving home community ID", ex);
        }

        return localHCID;
    }

    /**
     *
     * @return
     */
    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    /**
     *
     * @param body
     * @param context
     * @return
     */
    protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        getLogger().debug("Entering forwardToAgency");

        AdapterXDRRequestProxyObjectFactory factory = new AdapterXDRRequestProxyObjectFactory();

        AdapterXDRRequestProxy proxy = factory.getAdapterXDRRequestProxy();

        AdapterProvideAndRegisterDocumentSetSecuredRequestType oRequest = new AdapterProvideAndRegisterDocumentSetSecuredRequestType();
        oRequest.setProvideAndRegisterDocumentSetRequest(body);

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(oRequest, assertion);

        getLogger().debug("Exiting forwardToAgency");

        return response;
    }

    protected XDRAcknowledgementType sendErrorToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String errMsg) {
        AdapterProvideAndRegisterDocumentSetRequestErrorType adapterReq = new AdapterProvideAndRegisterDocumentSetRequestErrorType();

        AdapterXDRRequestErrorProxyObjectFactory factory = new AdapterXDRRequestErrorProxyObjectFactory();
        AdapterXDRRequestErrorProxy proxy = factory.getAdapterXDRRequestErrorProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setProvideAndRegisterDocumentSetRequest(body);
        adapterReq.setErrorMsg(errMsg);


        XDRAcknowledgementType adapterResp = proxy.provideAndRegisterDocumentSetBRequestError(adapterReq);

        return adapterResp;
    }

    /**
     *
     * @param newRequest
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @return
     */
    protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean isPolicyOk = false;

        getLogger().debug("Check policy");

        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        isPolicyOk = policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        getLogger().debug("Response from policy engine: " + isPolicyOk);

        return isPolicyOk;

    }

    protected String extractMessageId(WebServiceContext context) {
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        return msgIdExtractor.GetAsyncMessageId(context);
    }

    /**
     * The method will determine if the input message contains a LiFT request
     * @param request  The input message
     * @return  true if the url field of the request message is specified, otherwise will return false
     */
    protected boolean isLiftMessage(ProvideAndRegisterDocumentSetRequestType request) {
        boolean result = false;
        ExtrinsicObjectType extObj = null;

        // Check to see if a url was provided in the message and if LiFT is supported
        if (request != null &&
                request.getSubmitObjectsRequest() != null &&
                request.getSubmitObjectsRequest().getRegistryObjectList() != null) {
            RegistryObjectListType regObjList = request.getSubmitObjectsRequest().getRegistryObjectList();

            // Extract the ExtrinsicObjectType from the Registry List
            extObj = LiFTMessageHelper.extractExtrinsicObject(regObjList);

            if (extObj != null) {

                for (SlotType1 slot : extObj.getSlot()) {
                    if (NhincConstants.LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_NAME.equalsIgnoreCase(slot.getName()) ||
                            NhincConstants.LIFT_TRANSPORT_SERVICE_SLOT_NAME.equalsIgnoreCase(slot.getName())) {
                        result = true;
                        break;
                    }
                }
            }
        }

        logger.debug("isLiftMessage returning: " + result);
        return result;
    }

    /**
     * This method returns the value of the property that determines whether LiFT transfers are enabled or not
     * @return  true if LiFT Transforms are enabled, false if they are not
     */
    protected boolean checkLiftProperty() {
        boolean result = false;

        // Check the property file to see if LiFT is supported
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_ENABLED_PROPERTY_NAME);
        } catch (PropertyAccessException ex) {
            logger.error("Error: Failed to retrieve " + NhincConstants.LIFT_ENABLED_PROPERTY_NAME + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            logger.error(ex.getMessage());
        }

        logger.debug("Obtained value of the " + NhincConstants.LIFT_ENABLED_PROPERTY_NAME + "property: " + result);
        return result;
    }

    protected XDRAcknowledgementType processLiftMessage(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        String guid = null;

        // Add an entry to the Gateway Lift Database
        guid = addEntryToDatabase(request, assertion);

        // Send a notification to the Gateway Lift Manager that an entry is ready to be processed
        sendNotificationToLiftManager(guid);

        // Retrun the ack to the Initiating Gateway
        return ack;
    }

    private String addEntryToDatabase(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion) {
        String guid = null;
        GatewayLiftMessageDao dbDao = new GatewayLiftMessageDao();
        List<GatewayLiftMsgRecord> dbRecList = new ArrayList<GatewayLiftMsgRecord>();
        GatewayLiftMsgRecord dbRec = new GatewayLiftMsgRecord();

        if (request != null &&
                assertion != null &&
                NullChecker.isNotNullish(request.getDocument()) &&
                request.getDocument().get(0) != null) {
            LiFTPayloadBuilder payloadBuilder = new LiFTPayloadBuilder();
            LIFTMessageType liftMsg = payloadBuilder.extractLiftPayload(request.getDocument().get(0));

            // Create the database record
            if (liftMsg != null &&
                    liftMsg.getDataElement() != null &&
                    liftMsg.getDataElement().getClientData() != null &&
                    NullChecker.isNotNullish(liftMsg.getDataElement().getClientData().getClientData()) &&
                    liftMsg.getRequestElement() != null &&
                    NullChecker.isNotNullish(liftMsg.getRequestElement().getRequestGuid()) &&
                    liftMsg.getDataElement().getServerProxyData() != null &&
                    NullChecker.isNotNullish(liftMsg.getDataElement().getServerProxyData().getServerProxyAddress())) {
                dbRec.setAssertion(createAssertionBlob(assertion));
                dbRec.setFileNameToRetrieve(liftMsg.getDataElement().getClientData().getClientData());
                dbRec.setInitialEntryTimestamp(new Date());
                dbRec.setMessage(createMsgBlob(request));
                dbRec.setMessageState(NhincConstants.LIFT_GATEWAY_MESSAGE_DB_STATE_ENTERED);
                dbRec.setMessageType(NhincConstants.LIFT_GATEWAY_MESSAGE_DB_TYPE_DOC_SUB);
                dbRec.setProducerProxyAddress(liftMsg.getDataElement().getServerProxyData().getServerProxyAddress());
                Long longObj = new Long(liftMsg.getDataElement().getServerProxyData().getServerProxyPort());
                dbRec.setProducerProxyPort(longObj);

                guid = liftMsg.getRequestElement().getRequestGuid();
                dbRec.setRequestKeyGuid(guid);
                dbRecList.add(dbRec);

                if (dbDao.insertRecords(dbRecList) == false) {
                    logger.error("Failed to insert LifT record in the database");
                    guid = null;
                }
            }
        }

        return guid;
    }

    private Blob createAssertionBlob (AssertionType assertion) {
        Blob data = null;
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (assertion != null) {
            baOutStrm.reset();

            // Marshall the Assertion Element into binary data
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
                Marshaller marshaller = jc.createMarshaller();
                baOutStrm.reset();

                gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
                JAXBElement oJaxbElement = factory.createAssertion(assertion);
                marshaller.marshal(oJaxbElement, baOutStrm);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            logger.debug("Byte Array: " + baOutStrm.toString());

            data = Hibernate.createBlob(buffer);
        }

        return data;
    }

    private Blob createMsgBlob (ProvideAndRegisterDocumentSetRequestType msg) {
        Blob data = null;
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (msg != null) {
            baOutStrm.reset();

            // Marshall the Message Element into binary data
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("ihe.iti.xds_b._2007");
                Marshaller marshaller = jc.createMarshaller();
                baOutStrm.reset();

                ihe.iti.xds_b._2007.ObjectFactory factory = new ihe.iti.xds_b._2007.ObjectFactory();
                JAXBElement oJaxbElement = factory.createProvideAndRegisterDocumentSetRequest(msg);
                marshaller.marshal(oJaxbElement, baOutStrm);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            logger.debug("Byte Array: " + baOutStrm.toString());

            data = Hibernate.createBlob(buffer);
        }

        return data;
    }


    private StartLiftTransactionResponseType sendNotificationToLiftManager(String guid) {
        GatewayLiftManagerProxyObjectFactory factory = new GatewayLiftManagerProxyObjectFactory();
        GatewayLiftManagerProxy proxy = factory.getGatewayLiftManagerProxy();

        StartLiftTransactionRequestType startRequest = new StartLiftTransactionRequestType();
        startRequest.setRequestKeyGuid(guid);

        StartLiftTransactionResponseType resp = proxy.startLiftTransaction(startRequest);

        return resp;
    }
}
