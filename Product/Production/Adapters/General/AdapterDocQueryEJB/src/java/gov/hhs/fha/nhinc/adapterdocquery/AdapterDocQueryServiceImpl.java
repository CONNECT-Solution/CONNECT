/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocquery;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxy;
import gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxyObjectFactory;
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
public class AdapterDocQueryServiceImpl
{
    private static Log log = LogFactory.getLog(AdapterDocQueryServiceImpl.class);
    private static final String ERROR_CODE_CONTEXT = AdapterDocQueryServiceImpl.class.getName();
    private static final String ERROR_VALUE = "Input has null value";
    private static final String ERROR_SEVERITY = "Error";
        
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType
            request)
    {
        log.debug("Enter AdapterDocQueryServiceImpl.respondingGatewayCrossGatewayQuery()");
        AdhocQueryResponse response = null;
        try
        {
            if(request != null &&
                    request.getAdhocQueryRequest() != null)
            {
                AdapterDocumentRegistryProxyObjectFactory objFactory = new AdapterDocumentRegistryProxyObjectFactory();
                AdapterDocumentRegistryProxy registryProxy = objFactory.getAdapterDocumentRegistryProxy();
                AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
                adhocQueryRequest.setAdhocQuery(request.getAdhocQueryRequest().getAdhocQuery());
                adhocQueryRequest.setResponseOption(request.getAdhocQueryRequest().getResponseOption());
                adhocQueryRequest.setComment(request.getAdhocQueryRequest().getComment());
                adhocQueryRequest.setFederated(request.getAdhocQueryRequest().isFederated());
                adhocQueryRequest.setFederation(request.getAdhocQueryRequest().getFederation());
                adhocQueryRequest.setId(request.getAdhocQueryRequest().getId());
                adhocQueryRequest.setMaxResults(request.getAdhocQueryRequest().getMaxResults());
                adhocQueryRequest.setRequestSlotList(request.getAdhocQueryRequest().getRequestSlotList());
                adhocQueryRequest.setStartIndex(request.getAdhocQueryRequest().getStartIndex());
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
        log.debug("End AdapterDocQueryServiceImpl.respondingGatewayCrossGatewayQuery()");
        return response;
    }
}
