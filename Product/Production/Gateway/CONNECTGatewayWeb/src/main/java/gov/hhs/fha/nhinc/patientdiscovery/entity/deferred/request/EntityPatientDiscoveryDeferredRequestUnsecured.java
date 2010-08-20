package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

@WebService(serviceName = "EntityPatientDiscoveryAsyncReq", portName = "EntityPatientDiscoveryAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreq.EntityPatientDiscoveryAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncreq", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredRequestUnsecured/EntityPatientDiscoveryAsyncReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityPatientDiscoveryDeferredRequestUnsecured {

    @Resource
    private WebServiceContext context;

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        MCCIIN000002UV01 response = null;

        EntityPatientDiscoveryDeferredRequestImpl impl = getEntityPatientDiscoveryDeferredRequestImpl();
        if (impl != null)
        {
            response = impl.processPatientDiscoveryAsyncRequestUnsecured(request, getWebServiceContext());
        }

        return response;
    }

    protected EntityPatientDiscoveryDeferredRequestImpl getEntityPatientDiscoveryDeferredRequestImpl()
    {
        return new EntityPatientDiscoveryDeferredRequestImpl();
    }

    protected WebServiceContext getWebServiceContext()
    {
        return context;
    }
}
