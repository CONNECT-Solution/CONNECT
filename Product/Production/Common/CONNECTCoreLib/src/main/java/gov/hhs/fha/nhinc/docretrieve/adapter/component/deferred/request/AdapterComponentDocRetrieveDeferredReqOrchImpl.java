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
package gov.hhs.fha.nhinc.docretrieve.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.Hibernate;

/**
 *
 * @author Ralph Saunders
 */
public class AdapterComponentDocRetrieveDeferredReqOrchImpl {

    private Log log = null;

    public AdapterComponentDocRetrieveDeferredReqOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion) {
        log.debug("Enter AdapterComponentDocRetrieveDeferredReqImpl.respondingGatewayCrossGatewayRetrieve()");
        DocRetrieveAcknowledgementType response = null;
        RegistryResponseType responseType;
        response = new DocRetrieveAcknowledgementType();

        String msgText = "Success";

        RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
        respondingGatewayCrossGatewayRetrieveRequest.setRetrieveDocumentSetRequest(body);
        respondingGatewayCrossGatewayRetrieveRequest.setAssertion(assertion);

        if (!addEntryToDatabase(respondingGatewayCrossGatewayRetrieveRequest)) {
            msgText = "Failed to add the async request to async msg repository";
            log.error(msgText);
        }

        responseType = new RegistryResponseType();
        response.setMessage(responseType);
        //responseType.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG);
        responseType.setStatus("Responder");

        log.debug("Leaving AdapterComponentDocRetrieveDeferredReqImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

    /**
     *
     * @param request
     * @return boolean
     *
     */
    private boolean addEntryToDatabase(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        log.debug("Begin AdapterComponentDocRetrieveDeferredReqImpl.addEntryToDatabase");
        boolean result = false;
        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.DOC_RETRIEVE_SERVICE_NAME);

            rec.setMsgData(getBlobFromAsyncDB(request));
            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
            log.debug("End AdapterComponentDocRetrieveDeferredReqImpl.addEntryToDatabase");
        } catch (Exception e) {
            log.error("ERROR: Failed to add the async request to async msg repository.", e);
        }

        return result;
    }

    /**
     *
     * @param request
     * @return Blob
     *
     */
    private Blob getBlobFromAsyncDB(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        Blob asyncMessage = null;
        try {
            log.debug("Begin AdapterComponentDocRetrieveDeferredReqImpl.getBlobFromAsyncDB");
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayRetrieveRequestType> oJaxbElement = factory.createRespondingGatewayCrossGatewayRetrieveRequest(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
            log.debug("End AdapterComponentDocRetrieveDeferredReqImpl.getBlobFromAsyncDB");
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return asyncMessage;
    }
}
