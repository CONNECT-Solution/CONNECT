/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class SamlTokenExtractorHelper {

    private static Log log = LogFactory.getLog(SamlTokenExtractorHelper.class);
    public static final String INTERNAL_SUBJECT_DISCOVERY = "nhincsubjectdiscovery";
    public static final String INTERNAL_DOC_QUERY = "nhincdocumentquery";
    public static final String INTERNAL_DOC_RETRIEVE = "nhincdocumentretrieve";
    public static final String INTERNAL_AUDIT_QUERY = "nhincauditquery";
    public static final String INTERNAL_HIEM_SUBSCRIBE = "nhincnotificationproducer";
    public static final String INTERNAL_HIEM_UNSUBSCRIBE = "nhincsubscriptionmanager";
    public static final String INTERNAL_HIEM_NOTIFY = "nhincnotificationconsumer";

    public static String getHomeCommunityId(){
        log.debug("Entering SamlTokenExtractorHelper.getHomeCommunityId");
        String propFile = "gateway";
        String propName = "localHomeCommunityId";
        String homeCommunityId = "";
        try {
            homeCommunityId = PropertyAccessor.getProperty(propFile, propName);
        } catch (PropertyAccessException ex) {
            log.error("SamlTokenExtractorHelper.getHomeCommunityId failed to access property: " + ex.getMessage());
        }
        
        log.debug("Exiting SamlTokenExtractorHelper.getHomeCommunityId: " + homeCommunityId);
        return homeCommunityId;
    }
    
    public static String getEndpointURL(String homeCommunityId, String service) {
        log.debug("Entering SamlTokenExtractorHelper.getEndpointURL");

        CMBusinessEntity oCMBusinessEntity = new CMBusinessEntity();
        String url = null;

        if (NullChecker.isNotNullish(homeCommunityId) &&
                NullChecker.isNotNullish(service)) {
            try {
                oCMBusinessEntity = ConnectionManagerCache.getBusinessEntityByServiceName(homeCommunityId, service);
            } catch (Throwable t) {
                log.error("Failed to retrieve business entity.  Error: " + t.getMessage());
            }

            if (oCMBusinessEntity != null &&
                    oCMBusinessEntity.getBusinessServices() != null &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService() != null &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService().size() > 0 &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService().get(0) != null &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates() != null &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate() != null &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().size() > 0 &&
                    oCMBusinessEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0) != null &&
                    NullChecker.isNotNullish(oCMBusinessEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getEndpointURL())) {
                url = oCMBusinessEntity.getBusinessServices().getBusinessService().get(0).getBindingTemplates().getBindingTemplate().get(0).getEndpointURL();
            }

            if (NullChecker.isNotNullish(url)) {
                log.info("Returning URL: " + url);
            } else {
                log.error("Did not find a URL for home community id: " + homeCommunityId + " and service: " + service);
            }
        }
        else {
            log.error("A Null input parameter was passed to this method");
        }

        log.debug("Exiting SamlTokenExtractorHelper.getEndpointURL");
        return url;
    }
}
