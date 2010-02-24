/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
@WebService(serviceName = "XDRResponse_Service", portName = "XDRResponse_Port_Soap12", endpointInterface = "ihe.iti.xdr.async.response._2007.XDRResponsePortType", targetNamespace = "urn:ihe:iti:xdr:async:response:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRResponse/NhinXDRResponse.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class NhinXDRResponse {

    @Resource
    private WebServiceContext context;

    private static final Log logger = LogFactory.getLog(NhinXDRResponse.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body) {
        //TODO implement this method
        //throw new UnsupportedOperationException("Not implemented yet.");
        logger.debug("In NhinXDRResponse");
        return this.getNhinXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);
    }

    /**
     *
     * @return
     */
    private NhinXDRResponseImpl getNhinXDRResponseImpl(){
        return new NhinXDRResponseImpl();
    }

}
