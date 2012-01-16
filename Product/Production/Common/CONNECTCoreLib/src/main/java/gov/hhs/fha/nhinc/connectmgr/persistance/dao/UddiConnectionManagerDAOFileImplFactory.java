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

public class UddiConnectionManagerDAOFileImplFactory {
	
	static private String getFullFileName() {
		return PropertyAccessor.getPropertyFileLocation() + "uddiConnectionInfo2.xml";
	}
	
	static private ConnectionManagerDAOFileImpl instance = new ConnectionManagerDAOFileImpl(getFullFileName());
	
	private UddiConnectionManagerDAOFileImplFactory() {
	}

	static public ConnectionManagerDAOFileImpl getInstance() {
		return instance; 
	}

}
