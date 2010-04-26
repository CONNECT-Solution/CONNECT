package gov.hhs.fha.nhinc.adapter.xdr.async.response;

import ihe.iti.xdr._2007.AcknowledgementType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
@WebService(serviceName = "AdapterXDRResponse_Service", portName = "AdapterXDRResponse_Port_Soap", endpointInterface = "gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdrresponse", wsdlLocation = "WEB-INF/wsdl/AdapterXDRResponse/AdapterXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterXDRResponse {

    private static final Log logger = LogFactory.getLog(AdapterXDRResponse.class);

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType body) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        AcknowledgementType ack = getAdapterXDRResponseImpl().provideAndRegisterDocumentSetBResponse(body);
        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return ack;
    }

    protected AdapterXDRResponseImpl getAdapterXDRResponseImpl(){
        return new AdapterXDRResponseImpl();
    }

    protected Log getLogger(){
        return logger;
    }

}
