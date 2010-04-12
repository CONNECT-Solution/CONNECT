/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.error;

import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorRequestType;
import org.hl7.v3.AsyncAdapterPatientDiscoveryErrorSecuredRequestType;
import org.hl7.v3.MCCIIN000002UV01;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoverySecuredAsyncReqErrorImpl {
    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(AsyncAdapterPatientDiscoveryErrorSecuredRequestType request, WebServiceContext context) {
        AsyncAdapterPatientDiscoveryErrorRequestType unsecureRequest = new AsyncAdapterPatientDiscoveryErrorRequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));
        unsecureRequest.setErrorMsg(request.getErrorMsg());
        unsecureRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());

        MCCIIN000002UV01 ack = processPatientDiscoveryAsyncReqError(unsecureRequest);

        return ack;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReqError(AsyncAdapterPatientDiscoveryErrorRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        String msgText = "Success";

        ack = HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), msgText);

        return ack;
    }

}
