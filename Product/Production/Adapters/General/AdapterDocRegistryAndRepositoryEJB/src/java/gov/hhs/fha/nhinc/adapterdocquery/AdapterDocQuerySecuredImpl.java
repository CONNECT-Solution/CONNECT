/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocquery;

import gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxy;
import gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxyObjectFactory;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocQuerySecuredImpl {
    private static Log log = LogFactory.getLog(AdapterDocQuerySecuredImpl.class);
    private static final String ERROR_CODE_CONTEXT = AdapterDocQuerySecuredImpl.class.getName();
    private static final String ERROR_VALUE = "Input has null value";
    private static final String ERROR_SEVERITY = "Error";

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest request, WebServiceContext context)
    {
        log.debug("Enter AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        AdhocQueryResponse response = null;
        try
        {
            if(request != null)
            {
                AdapterDocumentRegistryProxyObjectFactory objFactory = new AdapterDocumentRegistryProxyObjectFactory();
                AdapterDocumentRegistryProxy registryProxy = objFactory.getAdapterDocumentRegistryProxy();
                AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
                adhocQueryRequest.setAdhocQuery(request.getAdhocQuery());
                adhocQueryRequest.setResponseOption(request.getResponseOption());
                adhocQueryRequest.setComment(request.getComment());
                adhocQueryRequest.setFederated(request.isFederated());
                adhocQueryRequest.setFederation(request.getFederation());
                adhocQueryRequest.setId(request.getId());
                adhocQueryRequest.setMaxResults(request.getMaxResults());
                adhocQueryRequest.setRequestSlotList(request.getRequestSlotList());
                adhocQueryRequest.setStartIndex(request.getStartIndex());
                response = registryProxy.registryStoredQuery(adhocQueryRequest);
            }
            else
            {
                RegistryErrorList errorList = new RegistryErrorList();
                response = new AdhocQueryResponse();
                RegistryError e = new RegistryError();
                errorList.getRegistryError().add(e);
                response.setRegistryErrorList(errorList);
                e.setValue(ERROR_VALUE);
                e.setSeverity(ERROR_SEVERITY);
                e.setCodeContext(ERROR_CODE_CONTEXT);
            }
        }catch(Exception exp)
        {
            log.error(exp.getMessage());
            exp.printStackTrace();
        }
        log.debug("End AdapterDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery()");
        return response;
    }
}
