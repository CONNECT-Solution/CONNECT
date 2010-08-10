package gov.hhs.fha.nhinc.mpi.adapter.component;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterComponentMpiService", portName = "AdapterComponentMpiPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi", wsdlLocation = "WEB-INF/wsdl/AdapterComponentMpi/AdapterComponentMpi.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentMpi
{
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.PRPAIN201306UV02 findCandidates(org.hl7.v3.PRPAIN201305UV02 findCandidatesRequest)
    {
        AdapterComponentMpiImpl oImpl = new AdapterComponentMpiImpl();
        return oImpl.findCandidates(false, findCandidatesRequest, context);
    }
}
