/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.adapter.patientdiscovery.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.adapter.patientdiscovery.proxy.AdapterPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryAdapterSender {

    public PRPAIN201306UV02 send201301ToAgency(PRPAIN201305UV02 request, AssertionType assertion) {
        RespondingGatewayPRPAIN201305UV02RequestType adapterReq = new RespondingGatewayPRPAIN201305UV02RequestType();

        AdapterPatientDiscoveryProxyObjectFactory factory = new AdapterPatientDiscoveryProxyObjectFactory();
        AdapterPatientDiscoveryProxy proxy = factory.getAdapterPatientDiscoveryProxy();

        adapterReq.setAssertion(assertion);
        adapterReq.setPRPAIN201305UV02(request);
        PRPAIN201306UV02 adapterResp = proxy.respondingGatewayPRPAIN201305UV02(adapterReq);

        return adapterResp;
    }

}
