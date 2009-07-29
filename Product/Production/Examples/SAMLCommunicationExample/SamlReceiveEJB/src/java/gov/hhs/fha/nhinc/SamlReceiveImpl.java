/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.RequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author vvickers
 */
class SamlReceiveImpl {

    AcknowledgementType extractSaml(RequestType body, WebServiceContext context) {
        System.out.println("Enter SamlReceiveImpl.extractSaml");
        AcknowledgementType ack = new AcknowledgementType();
        ack.setMessage("Received " + body.getMessage());
        // echo out assertion information
        AssertionType nhincAssert = SamlTokenExtractor.GetAssertion(context);
        System.out.println("Exit SamlReceiveImpl.extractSaml");
        return ack;
    }

}
