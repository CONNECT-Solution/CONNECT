/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.payload.builder;

import gov.hhs.healthit.nhin.ClientDataType;
import gov.hhs.healthit.nhin.LIFTDataElementType;
import gov.hhs.healthit.nhin.LIFTMessageType;
import gov.hhs.healthit.nhin.LIFTRequestElementType;
import gov.hhs.healthit.nhin.ServerProxyDataType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.lift.dao.LiftTransferDataRecordDao;
import gov.hhs.fha.nhinc.lift.model.LiftTransferDataRecord;
import gov.hhs.fha.nhinc.lift.utils.LiFTMessageHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class LiFTPayloadBuilder {

    private static Log log = LogFactory.getLog(LiFTPayloadBuilder.class);

    /**
     * This method will build and insert a LiFT Payload into the request message.  At this time
     * this method will only support the transfer of a single document
     * @param msg  Original Document Submission Request Message
     * @param assertion  Original assertion information associated with this request message
     * @param fileInfo  List of information on the file(s) to be transferred.  At this time only 1 is supported.
     * @return  true if the payload was successfully inserted, otherwise it will return false if an error occurs
     */
    public String buildLiFTPayload(ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion, List<UrlInfoType> urlInfoList) {
        log.debug("Entering LiFTPayloadBuilder.buildLiFTPayload method...");
        String guid = null;
        ExtrinsicObjectType extObj = null;

        // Validate input parameters
        if (msg != null &&
                assertion != null &&
                NullChecker.isNotNullish(urlInfoList) &&
                urlInfoList.get(0) != null &&
                NullChecker.isNotNullish(urlInfoList.get(0).getUrl()) &&
                NullChecker.isNotNullish(urlInfoList.get(0).getId())) {

            // Verify that the Documents Section of the Request message is empty, if not empty it.
            if (NullChecker.isNotNullish(msg.getDocument())) {
                log.warn("Document section of the input message was not null, replacing with the LiFT Payload");
                msg.getDocument().clear();
            }

            if (msg.getSubmitObjectsRequest() != null &&
                    msg.getSubmitObjectsRequest().getRegistryObjectList() != null) {
                RegistryObjectListType regObjList = msg.getSubmitObjectsRequest().getRegistryObjectList();

                // Extract the ExtrinsicObjectType from the Registry List
                extObj = LiFTMessageHelper.extractExtrinsicObject(regObjList);

                if (extObj != null) {
                    // Create the Lift slots and add it to the Extrinsic Object
                    SlotType1 transferServiceSlot = createTransferServiceSlot();
                    SlotType1 transferProtocolSlot = createTransferProtocolSlot();

                    extObj.getSlot().add(transferServiceSlot);
                    extObj.getSlot().add(transferProtocolSlot);

                    // Place the information about this document in the LiFT Database
                    guid = addEntryToLiftDatabase(urlInfoList.get(0).getUrl());

                    // Place the Lift Payload in the XDR Request Message
                    Document liftPayload = createLiftPayload(guid, urlInfoList.get(0).getId(), urlInfoList.get(0).getUrl());
                    if (liftPayload != null) {
                        msg.getDocument().add(liftPayload);
                    }
                }
            } else {
                log.error("No Registry Object List was provided in the input message");
            }
        } else {
            log.error("One or more of the input paramters were invalid");
        }

        log.debug("Exiting LiFTPayloadBuilder.buildLiFTPayload method...");
        return guid;
    }

    public LIFTMessageType extractLiftPayload(Document document) {
        LIFTMessageType payload = null;

        if (document != null &&
                document.getValue() != null) {
            byte[] data = document.getValue();
            ByteArrayInputStream baInStrm = new ByteArrayInputStream(data);

            if (data != null && data.length > 0) {
                try {
                    //InputStream in = new InputStream(data);
                    JAXBContextHandler oHandler = new JAXBContextHandler();
                    JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
                    Unmarshaller unmarshaller = jc.createUnmarshaller();
                    JAXBElement jaxEle = (JAXBElement) unmarshaller.unmarshal(baInStrm);
                    payload = (LIFTMessageType) jaxEle.getValue();
                } catch (JAXBException ex) {
                    log.error("Unable to extract documment " + document.getId() + ". " + ex.getMessage());
                }
            }
        }

        return payload;
    }

    private SlotType1 createTransferServiceSlot() {
        SlotType1 transferServiceSlot = new SlotType1();
        ValueListType valueList = new ValueListType();

        valueList.getValue().add(NhincConstants.LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_VALUE);

        transferServiceSlot.setName(NhincConstants.LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_NAME);

        transferServiceSlot.setValueList(valueList);

        return transferServiceSlot;
    }

    private SlotType1 createTransferProtocolSlot() {
        SlotType1 transferProtocolSlot = new SlotType1();

        ValueListType valueList = new ValueListType();

        valueList.getValue().add(NhincConstants.LIFT_TRANSPORT_SERVICE_SLOT_VALUE);

        transferProtocolSlot.setName(NhincConstants.LIFT_TRANSPORT_SERVICE_SLOT_NAME);

        transferProtocolSlot.setValueList(valueList);

        return transferProtocolSlot;
    }

    private Document createLiftPayload(String guid, String id, String fileUrl) {
        Document payload = new Document();
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        LIFTMessageType liftPayload = new LIFTMessageType();

        LIFTRequestElementType liftReqElem = new LIFTRequestElementType();
        liftReqElem.setRequestGuid(guid);
        liftPayload.setRequestElement(liftReqElem);

        LIFTDataElementType dataElem = new LIFTDataElementType();
        ClientDataType clientData = new ClientDataType();

        // Set the relative path to where the file will be located on the file server
        String filePath = createDestFilePath(fileUrl, guid);
        clientData.setClientData(filePath);
        dataElem.setClientData(clientData);

        ServerProxyDataType serverProxyData = new ServerProxyDataType();

        // Obtain the proxy address and port number from the properties file
        String proxyAddr = getProxyAddressProperty();
        int proxyPort = getProxyAddressPort();

        if (NullChecker.isNotNullish(proxyAddr) &&
                proxyPort > 0) {
            serverProxyData.setServerProxyAddress(proxyAddr);
            serverProxyData.setServerProxyPort(proxyPort);
            dataElem.setServerProxyData(serverProxyData);

            liftPayload.setDataElement(dataElem);

            // Marshall the Lift Payload Element into binary data
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.healthit.nhin");
                Marshaller marshaller = jc.createMarshaller();
                baOutStrm.reset();

                gov.hhs.healthit.nhin.ObjectFactory factory = new gov.hhs.healthit.nhin.ObjectFactory();
                JAXBElement oJaxbElement = factory.createLIFTMessage(liftPayload);
                marshaller.marshal(oJaxbElement, baOutStrm);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }

            payload.setId(id);
            payload.setValue(baOutStrm.toByteArray());
        } else {
            payload = null;
        }

        return payload;
    }

    protected String addEntryToLiftDatabase(String fileUrl) {
        String guid = null;

        // Generate the RequestGUID
        UUID uuid = UUID.randomUUID();

        guid = uuid.toString();

        // Add the entry to the Lift Tranfer Database
        LiftTransferDataRecordDao dbDao = new LiftTransferDataRecordDao();
        LiftTransferDataRecord dbRec = new LiftTransferDataRecord();
        dbRec.setRequestKeyGuid(guid);
        dbRec.setTransferState(NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED);
        dbDao.save(dbRec);


        return guid;
    }

    protected String getProxyAddressProperty() {
        String propVal = null;

        // Check the property file to retreive the property
        try {
            propVal = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_ADDRESS);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.LIFT_PROXY_ADDRESS + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        return propVal;
    }

    protected int getProxyAddressPort() {
        long propVal = -1;

        // Check the property file to retreive the property
        try {
            propVal = PropertyAccessor.getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_PORT);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.LIFT_PROXY_PORT + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
            propVal = -1;
        }

        Long longObj = Long.valueOf(propVal);
        return longObj.intValue();
    }

    private String createDestFilePath(String fileUrl, String guid) {
        String destPath = null;
        String fileName = null;

        String[] splitStrings = fileUrl.split("/");
        int len = splitStrings.length;

        fileName = splitStrings[len - 1];

        destPath = "/" + guid + "/" + fileName;

        return destPath;
    }
}
