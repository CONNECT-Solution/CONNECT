/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response;

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
@WebService(serviceName = "XDRDeferredResponse_Service", portName = "XDRDeferredResponse_Port_Soap", endpointInterface = "ihe.iti.xdr._2007.XDRDeferredResponsePortType", targetNamespace = "urn:ihe:iti:xdr:2007", wsdlLocation = "WEB-INF/wsdl/NhinXDRResponse/NhinXDRDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinXDRResponse {
    @Resource
    private WebServiceContext context;

    private static final Log logger = LogFactory.getLog(NhinXDRResponse.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBDeferredResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body) {
        getLogger().debug("Entering NhinXDRResponse");

        AcknowledgementType ack = this.getNhinXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body, context);

        getLogger().debug("Exiting NhinXDRResponse");
        return ack;
    }

    /**
     *
     * @return
     */
    private NhinXDRResponseImpl getNhinXDRResponseImpl(){
        return new NhinXDRResponseImpl();
    }

    protected Log getLogger(){
        return logger;
    }

}
