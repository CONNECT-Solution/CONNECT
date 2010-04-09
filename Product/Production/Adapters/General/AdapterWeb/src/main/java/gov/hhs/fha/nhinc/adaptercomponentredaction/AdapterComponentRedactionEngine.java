package gov.hhs.fha.nhinc.adaptercomponentredaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterComponentRedactionEngineService", portName = "AdapterComponentRedactionEnginePortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction", wsdlLocation = "WEB-INF/wsdl/AdapterComponentRedactionEngine/AdapterComponentRedactionEngine.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterComponentRedactionEngine
{

    public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType filterDocQueryResultsRequest)
    {
        FilterDocQueryResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if(redactionEngineImpl != null)
        {
            response = redactionEngineImpl.filterDocQueryResults(filterDocQueryResultsRequest);
        }

        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest)
    {
        FilterDocRetrieveResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if(redactionEngineImpl != null)
        {
            response = redactionEngineImpl.filterDocRetrieveResults(filterDocRetrieveResultsRequest);
        }

        return response;
    }

    protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
    {
        return new AdapterComponentRedactionEngineImpl();
    }
}
