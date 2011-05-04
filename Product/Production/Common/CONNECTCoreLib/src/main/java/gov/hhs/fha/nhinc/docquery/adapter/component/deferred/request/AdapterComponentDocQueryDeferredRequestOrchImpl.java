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
package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.bind.JAXBContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredRequestOrchImpl {

    private Log log = null;

    public AdapterComponentDocQueryDeferredRequestOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     *
     * @param msg
     * @param assertion
     * @return <code>DocQueryAcknowledgementType</code>
     */
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();
        String msgText = "Success";

        RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
        respondingGatewayCrossGatewayQueryRequest.setAdhocQueryRequest(msg);
        respondingGatewayCrossGatewayQueryRequest.setAssertion(assertion);

        if (!addEntryToDatabase(respondingGatewayCrossGatewayQueryRequest)) {
            msgText = "Failed to add the async request to async msg repository";
        }

        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        return ack;
    }

    /**
     *
     * @param request
     * @return boolean
     *
     */
    private boolean addEntryToDatabase(RespondingGatewayCrossGatewayQueryRequestType request) {
        log.debug("Begin AdapterComponentDocQueryDeferredRequestOrchImpl.addEntryToDatabase");
        boolean result = false;
        try {
            List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
            AsyncMsgRecord rec = new AsyncMsgRecord();
            AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

            // Replace with message id from the assertion class
            rec.setMessageId(request.getAssertion().getMessageId());
            rec.setCreationTime(new Date());
            rec.setServiceName(NhincConstants.DOC_QUERY_SERVICE_NAME);

            rec.setMsgData(getBlobFromAdhocQueryRequestType(request));
            asyncMsgRecs.add(rec);

            result = instance.insertRecords(asyncMsgRecs);

            if (result == false) {
                log.error("Failed to insert asynchronous record in the database");
            }
            log.debug("End AdapterComponentDocQueryDeferredRequestOrchImpl.addEntryToDatabase");
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
    private Blob getBlobFromAdhocQueryRequestType(RespondingGatewayCrossGatewayQueryRequestType request) {
        Blob asyncMessage = null;
        try {
             log.debug("Begin AdapterComponentDocQueryDeferredRequestOrchImpl.getBlobFromAdhocQueryRequestType");
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayQueryRequestType> oJaxbElement = factory.createRespondingGatewayCrossGatewayQueryRequest(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
             log.debug("End AdapterComponentDocQueryDeferredRequestOrchImpl.getBlobFromAdhocQueryRequestType");
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return asyncMessage;
    }
}
