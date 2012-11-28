package gov.hhs.fha.nhinc.direct.xdr;

import gov.hhs.fha.nhinc.direct.DirectClient;
import gov.hhs.fha.nhinc.direct.DirectMailClient;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

public class DirectObjectFactory extends ComponentProxyObjectFactory {
    
    private static final String CONFIG_FILE_NAME = "direct.appcontext.xml";
    private static final String BEAN_NAME = "extDirectMailClient";
	
    public DirectClient getDirectClient() {
    	return getBean(BEAN_NAME, DirectMailClient.class);
    }
    
	@Override
	protected String getConfigFileName() {
		return CONFIG_FILE_NAME;
	}
}
