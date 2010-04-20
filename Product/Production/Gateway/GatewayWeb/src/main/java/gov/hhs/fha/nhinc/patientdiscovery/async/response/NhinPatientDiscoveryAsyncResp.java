/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.async.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "RespondingGatewayAsyncResp_Service", portName = "RespondingGatewayAsyncResp_Port", endpointInterface = "ihe.iti.xcpd._2009.RespondingGatewayAsyncRespPortType", targetNamespace = "urn:ihe:iti:xcpd:2009", wsdlLocation = "WEB-INF/wsdl/NhinPatientDiscoveryAsyncResp/NhinPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class NhinPatientDiscoveryAsyncResp {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02(org.hl7.v3.PRPAIN201306UV02 body) {
        return new NhinPatientDiscoveryAsyncRespImpl().respondingGatewayPRPAIN201306UV02(body, context);
    }

}
