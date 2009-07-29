/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import org.netbeans.j2ee.wsdl.interfaces.samlreceive.SamlReceivePortType;

/**
 *
 * @author vvickers
 */
@WebService(serviceName = "SamlReceiveService", portName = "SamlReceivePort", endpointInterface = "org.netbeans.j2ee.wsdl.interfaces.samlreceive.SamlReceivePortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/Interfaces/SamlReceive", wsdlLocation = "META-INF/wsdl/SamlProcessWS/SamlReceive.wsdl")
@Stateless
public class SamlProcessWS implements SamlReceivePortType {
    @Resource
    private WebServiceContext context;
    
    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType samlReceiveOperation(gov.hhs.fha.nhinc.common.nhinccommon.RequestType body) {
         SamlReceiveImpl impl = new SamlReceiveImpl();
        return impl.extractSaml(body, context);
    }

}
