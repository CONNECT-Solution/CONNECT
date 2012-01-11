package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * 
 * @author kshtabnoy
 * 
 *  Factory that provides instance of ConnectionManagerDAOFileImpl that uses
 *  InternalConnections.xml file as the storage.
 * 
 */

public class InternalConnectionManagerDAOFileImplFactory {
	
	static private String getFullFileName() {
		return PropertyAccessor.getPropertyFileLocation() + "InternalConnectionInfo.xml";
	}
	
	static private ConnectionManagerDAOFileImpl instance = new ConnectionManagerDAOFileImpl(getFullFileName());
	
	private InternalConnectionManagerDAOFileImplFactory() {
	}

	static public ConnectionManagerDAOFileImpl getInstance() {
		return instance; 
	}

}
