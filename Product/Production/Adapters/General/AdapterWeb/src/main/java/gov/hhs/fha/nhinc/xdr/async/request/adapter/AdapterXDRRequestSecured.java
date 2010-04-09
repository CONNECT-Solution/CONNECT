/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.request.adapter;

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
@WebService(serviceName = "AdapterXDRRequestSecured_Service", portName = "AdapterXDRRequestSecured_Port_Soap12", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequestsecured", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequestSecured/AdapterXDRRequestSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterXDRRequestSecured {

    @Resource
    private WebServiceContext context;
    private static final Log logger = LogFactory.getLog(AdapterXDRRequestSecured.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        AcknowledgementType ack = getAdapterXDRRequestSecuredImpl().provideAndRegisterDocumentSetBRequest(body, context);

        getLogger().debug("Exiting provideAndRegisterDocumentSetBRequest");

        return ack;
    }

    protected AdapterXDRRequestSecuredImpl getAdapterXDRRequestSecuredImpl(){
        return new AdapterXDRRequestSecuredImpl();
    }

    protected Log getLogger(){
        return logger;
    }

}
