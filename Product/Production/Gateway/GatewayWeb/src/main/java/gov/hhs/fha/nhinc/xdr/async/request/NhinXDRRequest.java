/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request;


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
@WebService(serviceName = "XDRRequest_Service", portName = "XDRRequest_Port_Soap12", endpointInterface = "ihe.iti.xdr.async.request._2007.XDRRequestPortType", targetNamespace = "urn:ihe:iti:xdr:async:request:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRRequest/NhinXDRRequest.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class NhinXDRRequest {
    @Resource
    private WebServiceContext context;
    
    private static final Log logger = LogFactory.getLog(NhinXDRRequest.class);

    /**
     * 
     * @param body
     * @return
     */
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method
        //throw new UnsupportedOperationException("Not implemented yet.");
        logger.debug("In NhinXDRRequest");

        return getNhinXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);
    }

    /**
     * 
     * @return
     */
    private NhinXDRRequestImpl getNhinXDRRequestImpl(){
        return new NhinXDRRequestImpl();
    }
    

}


