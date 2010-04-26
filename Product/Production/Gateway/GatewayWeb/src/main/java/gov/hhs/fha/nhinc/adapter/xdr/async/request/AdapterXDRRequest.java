package gov.hhs.fha.nhinc.adapter.xdr.async.request;

import ihe.iti.xdr._2007.AcknowledgementType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
@WebService(serviceName = "AdapterXDRRequest_Service", portName = "AdapterXDRRequest_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrrequest.AdapterXDRRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrrequest", wsdlLocation = "WEB-INF/wsdl/AdapterXDRRequest/AdapterXDRRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterXDRRequest {

    private static final Log logger = LogFactory.getLog(AdapterXDRRequest.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        AcknowledgementType ack = getAdapterXDRRequestImpl().provideAndRegisterDocumentSetBRequest(body);

        getLogger().debug("Exiting provideAndRegisterDocumentSetBRequest");

        return ack;
    }

    protected AdapterXDRRequestImpl getAdapterXDRRequestImpl(){
        return new AdapterXDRRequestImpl();
    }

    protected Log getLogger(){
        return logger;
    }

}
