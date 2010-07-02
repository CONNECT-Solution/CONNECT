/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policydteproxy;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveResultEventType;
import gov.hhs.fha.nhinc.common.eventcommon.FindAuditEventsEventType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectAddedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectRevisedEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubscribeEventType;
import gov.hhs.fha.nhinc.common.eventcommon.UnsubscribeEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhincinternalcomponentpolicyenginetransform.NhincComponentInternalPolicyEngineTransformService;
import gov.hhs.fha.nhinc.nhincinternalcomponentpolicyenginetransform.NhincInternalComponentPolicyEngineTransformPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class PolicyDteWebServiceProxy implements IPolicyDteProxy {

    private static Log log = LogFactory.getLog(PolicyDteWebServiceProxy.class);
    static NhincComponentInternalPolicyEngineTransformService policyDteService = new NhincComponentInternalPolicyEngineTransformService();

    public CheckPolicyRequestType transformSubjectAddedToCheckPolicy(SubjectAddedEventType transformSubjectAddedToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformSubjectAddedToCheckPolicy(transformSubjectAddedToCheckPolicyRequest);
        
        return policyReq;
    }

    public CheckPolicyRequestType transformSubjectRevisedToCheckPolicy(SubjectRevisedEventType transformSubjectRevisedToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformSubjectRevisedToCheckPolicy(transformSubjectRevisedToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformAdhocQueryToCheckPolicy(AdhocQueryRequestEventType transformAdhocQueryToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformAdhocQueryToCheckPolicy(transformAdhocQueryToCheckPolicyRequest);
        
        return policyReq;
    }

    public CheckPolicyRequestType transformAdhocQueryResultToCheckPolicy(AdhocQueryResultEventType transformAdhocQueryResultToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformAdhocQueryResultToCheckPolicy(transformAdhocQueryResultToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformDocRetrieveToCheckPolicy(DocRetrieveEventType transformDocRetrieveToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformDocRetrieveToCheckPolicy(transformDocRetrieveToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformDocRetrieveResultToCheckPolicy(DocRetrieveResultEventType transformDocRetrieveResultToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformDocRetrieveResultToCheckPolicy(transformDocRetrieveResultToCheckPolicyRequest);
        
        return policyReq;
    }

    public CheckPolicyRequestType transformFindAuditEventsToCheckPolicy(FindAuditEventsEventType transformFindAuditEventsToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformFindAuditEventsToCheckPolicy(transformFindAuditEventsToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformSubjectReidentificationToCheckPolicy(SubjectReidentificationEventType transformSubjectReidentificationToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformSubjectReidentificationToCheckPolicy(transformSubjectReidentificationToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformSubscribeToCheckPolicy(SubscribeEventType transformSubscribeToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformSubscribeToCheckPolicy(transformSubscribeToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformUnsubscribeToCheckPolicy(UnsubscribeEventType transformUnsubscribeToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformUnsubscribeToCheckPolicy(transformUnsubscribeToCheckPolicyRequest);

        return policyReq;
    }

    public CheckPolicyRequestType transformNotifyToCheckPolicy(NotifyEventType transformNotifyToCheckPolicyRequest) {
        NhincInternalComponentPolicyEngineTransformPortType port = getPort();

        CheckPolicyRequestType policyReq = port.transformNotifyToCheckPolicy(transformNotifyToCheckPolicyRequest);
        
        return policyReq;
    }

    private NhincInternalComponentPolicyEngineTransformPortType getPort() {
        NhincInternalComponentPolicyEngineTransformPortType port = policyDteService.getNhincInternalComponentPolicyEngineTransformPort();

        // Get the local home community id
        String homeCommunityId = null;
        try {
            log.info("Attempting to retrieve property: " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            homeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            log.info("Retrieve local home community id: " + homeCommunityId);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.HOME_COMMUNITY_ID_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        // Get the URL for the Audit Logging Component at this community
        String url = null;
        if (NullChecker.isNotNullish(homeCommunityId)) {
            try {
                url = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunityId, NhincConstants.POLICYENGINE_DTE_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.POLICYENGINE_DTE_SERVICE_NAME + " for community id: " + homeCommunityId);
                log.error(ex.getMessage());
            }
        }

        if (NullChecker.isNotNullish(url)) {
            gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        } else {
            log.error("Error: URL is null");
            port = null;
        }

        return port;
    }
}
