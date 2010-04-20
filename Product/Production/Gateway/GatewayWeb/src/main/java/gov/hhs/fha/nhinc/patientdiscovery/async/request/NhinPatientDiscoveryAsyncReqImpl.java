/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.async.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

/**
 *
 * @author JHOPPESC
 */
public class NhinPatientDiscoveryAsyncReqImpl {
    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, WebServiceContext context) {
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        return respondingGatewayPRPAIN201305UV02(request, assertion);
    }

    public MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // TODO:  Replace with real logic
        resp = HL7AckTransforms.createAckFrom201305(request, "Success");

        return resp;
    }

}
