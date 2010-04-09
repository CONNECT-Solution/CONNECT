/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import ihe.iti.xdr._2007.AcknowledgementType;
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
@WebService(serviceName = "AdapterXDRResponseSecured_Service", portName = "AdapterXDRResponseSecured_Port_Soap12", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrresponsesecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponseSecured/AdapterXDRResponseSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRResponseSecured {

    @Resource
    private WebServiceContext context;
    private static final Log logger = LogFactory.getLog(AdapterXDRResponseSecured.class);


    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        AcknowledgementType ack = getAdapterXDRResponseSecuredImpl().provideAndRegisterDocumentSetBResponse(body, context);
        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");
        return ack;
    }

    protected AdapterXDRResponseSecuredImpl getAdapterXDRResponseSecuredImpl(){
        return new AdapterXDRResponseSecuredImpl();
    }

    protected Log getLogger(){
        return logger;
    }
}
