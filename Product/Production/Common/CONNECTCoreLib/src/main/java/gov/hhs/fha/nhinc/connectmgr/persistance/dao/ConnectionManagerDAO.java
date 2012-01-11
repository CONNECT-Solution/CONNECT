package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import org.uddi.api_v3.BusinessDetail;

/**
 * 
 * @author kshtabnoy
 * 
 * Isolates load/save functionality
 *  
 */
public interface ConnectionManagerDAO {
	
	BusinessDetail loadBusinessDetail();
	
	void saveBusinessDetail(BusinessDetail businessEntity);
	
}
