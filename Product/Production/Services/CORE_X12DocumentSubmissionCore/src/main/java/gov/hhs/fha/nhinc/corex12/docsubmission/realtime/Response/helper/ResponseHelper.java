/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.corex12.docsubmission.realtime.Response.helper;

import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmission;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeBatchSubmissionResponse;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeRequest;
import org.caqh.soap.wsdl.corerule2_2_0.COREEnvelopeRealTimeResponse;

/**
 *
 * @author achidamb
 */
public class ResponseHelper {

    public static COREEnvelopeRealTimeResponse createErrorResponse(COREEnvelopeRealTimeRequest msg, String targetHCID) {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("RealTime");
        response.setPayloadID(msg.getPayload());
        response.setCORERuleVersion("2.2.0");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setErrorMessage("No endpoint available for target community HCID: " + targetHCID);
        response.setTimeStamp(msg.getTimeStamp());
        return response;
    }

    public static COREEnvelopeRealTimeResponse createWebServiceErrorResponse(COREEnvelopeRealTimeRequest msg, String message) {
        COREEnvelopeRealTimeResponse response = new COREEnvelopeRealTimeResponse();
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("RealTime");
        response.setPayloadID(msg.getPayload());
        response.setCORERuleVersion("2.2.0");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setErrorMessage(message);
        response.setTimeStamp(msg.getTimeStamp());
        return response;
    }

    public static COREEnvelopeBatchSubmissionResponse createErrorResponse(COREEnvelopeBatchSubmission msg, String targetHCID) {
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setCORERuleVersion("2.2.0");
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("Batch");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setTimeStamp(msg.getTimeStamp());
        response.setPayloadID(msg.getPayloadID());
        response.setPayload(msg.getPayload());
        response.setErrorMessage("No endpoint available for target community HCID: " + targetHCID);
        return response;
    }

    public static COREEnvelopeBatchSubmissionResponse createWebServiceErrorResponse(COREEnvelopeBatchSubmission msg, String message) {
        COREEnvelopeBatchSubmissionResponse response = new COREEnvelopeBatchSubmissionResponse();
        response.setCORERuleVersion("2.2.0");
        response.setPayloadType("CoreEnvelopeError");
        response.setProcessingMode("Batch");
        response.setSenderID(msg.getSenderID());
        response.setReceiverID(msg.getReceiverID());
        response.setTimeStamp(msg.getTimeStamp());
        response.setPayloadID(msg.getPayloadID());
        response.setPayload(msg.getPayload());
        response.setErrorMessage(message);
        return response;
    }

}
