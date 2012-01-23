package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;

public abstract class AbstractServicePropertyAccessor implements ServicePropertyAccessor {

	 static Log log = LogFactory.getLog(AbstractServicePropertyAccessor.class);
		
	
	public AbstractServicePropertyAccessor() {
		super();
	}

	abstract protected String getServiceName();

	@Override
	public boolean isServiceEnabled() {
		boolean serviceEnabled = false;
		try {
			serviceEnabled = PropertyAccessor.getPropertyBoolean(
					NhincConstants.GATEWAY_PROPERTY_FILE, getServiceName());
		} catch (PropertyAccessException ex) {
			log.error("Error: Failed to retrieve " + getServiceName()
					+ " from property file: "
					+ NhincConstants.GATEWAY_PROPERTY_FILE);
			log.error(ex.getMessage());
		}
	
		return serviceEnabled;
	}


	@Override
	public boolean isInPassThroughMode() {
		boolean passThroughModeEnabled = false;
		try {
			passThroughModeEnabled = PropertyAccessor.getPropertyBoolean(
					NhincConstants.GATEWAY_PROPERTY_FILE, getServiceName());
		} catch (PropertyAccessException ex) {
			log.error("Error: Failed to retrieve " + getServiceName()
					+ " from property file: "
					+ NhincConstants.GATEWAY_PROPERTY_FILE);
			log.error(ex.getMessage());
		}
		return passThroughModeEnabled;
	}

}