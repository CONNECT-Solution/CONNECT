/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request;

import ihe.iti.xdr._2007.AcknowledgementType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "XDRDeferredRequest_Service", portName = "XDRDeferredRequest_Port_Soap", endpointInterface = "ihe.iti.xdr._2007.XDRDeferredRequestPortType", targetNamespace = "urn:ihe:iti:xdr:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRRequest/NhinXDRDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinXDRRequest {
    @Resource
    private WebServiceContext context;

    private static final Log logger = LogFactory.getLog(NhinXDRRequest.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBDeferredRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        getLogger().debug("Entering NhinXDRRequest");

        AcknowledgementType ack = getNhinXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body, context);

        getLogger().debug("Exiting NhinXDRRequest");

        return ack;
    }

    /**
     *
     * @return
     */
    protected NhinXDRRequestImpl getNhinXDRRequestImpl(){
        return new NhinXDRRequestImpl();
    }

    protected Log getLogger(){
        return logger;
    }

}
