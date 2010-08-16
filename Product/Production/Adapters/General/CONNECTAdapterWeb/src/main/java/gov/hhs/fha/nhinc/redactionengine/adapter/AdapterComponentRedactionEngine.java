package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocQueryResultsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FilterDocRetrieveResultsResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterComponentRedactionEngineService", portName = "AdapterComponentRedactionEnginePortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentredaction.AdapterComponentRedactionEnginePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentredaction", wsdlLocation = "WEB-INF/wsdl/AdapterComponentRedactionEngine/AdapterComponentRedactionEngine.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterComponentRedactionEngine
{
    @Resource
    private WebServiceContext context;

    public FilterDocQueryResultsResponseType filterDocQueryResults(FilterDocQueryResultsRequestType filterDocQueryResultsRequest)
    {
        FilterDocQueryResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if(redactionEngineImpl != null)
        {
            response = redactionEngineImpl.filterDocQueryResults(filterDocQueryResultsRequest, context);
        }

        return response;
    }

    public FilterDocRetrieveResultsResponseType filterDocRetrieveResults(FilterDocRetrieveResultsRequestType filterDocRetrieveResultsRequest)
    {
        FilterDocRetrieveResultsResponseType response = null;

        AdapterComponentRedactionEngineImpl redactionEngineImpl = getAdapterComponentRedactionEngineImpl();
        if(redactionEngineImpl != null)
        {
            response = redactionEngineImpl.filterDocRetrieveResults(filterDocRetrieveResultsRequest, context);
        }

        return response;
    }

    protected AdapterComponentRedactionEngineImpl getAdapterComponentRedactionEngineImpl()
    {
        return new AdapterComponentRedactionEngineImpl();
    }
}
