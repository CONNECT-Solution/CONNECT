package gov.hhs.fha.nhinc.docsubmission.entity.proxy;

import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;


public class EntityDocSubmissionProxyObjectFactory extends ComponentProxyObjectFactory {
    private static final String CONFIG_FILE_NAME = "EntityDocSubmissionProxyConfig.xml";
    private static final String BEAN_NAME = "entitydocsubmission";

    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    public EntityDocSubmissionProxy getEntityDocSubmissionProxy() {
        return getBean(BEAN_NAME, EntityDocSubmissionProxy.class);
    }

}
