package gov.hhs.fha.nhinc.admingui.event.service;

import gov.hhs.fha.nhinc.admingui.event.model.EventNwhinOrganization;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author jasonasmith
 */
@Service
public class EventCountServiceImpl implements EventCountService {
    
    private static final HashMap<String, EventNwhinOrganization> inboundOrganizations = new HashMap<String, EventNwhinOrganization>();
    private static final HashMap<String, EventNwhinOrganization> outboundOrganizations = new HashMap<String, EventNwhinOrganization>();
    
    public static final String INBOUND_EVENT_TYPE = "END_INBOUND_MESSAGE";
    public static final String OUTBOUND_EVENT_TYPE = "END_INVOCATION_TO_NWHIN";
    public static final String INBOUND_HCID_TYPE = "initiatorHcid";
    public static final String OUTBOUND_HCID_TYPE = "respondingHcid";
    
    private static final String PD_SERVICE_TYPE = "Patient Discovery";
    private static final String PD_DEF_REQ_SERVICE_TYPE = "Patient Discovery Deferred Request";
    private static final String PD_DEF_RESP_SERVICE_TYPE = "Patient Discovery Deferred Response";
    private static final String DQ_SERVICE_TYPE = "Document Query";
    private static final String DR_SERVICE_TYPE = "Retrieve Document";
    private static final String DS_SERVICE_TYPE = "Document Submission";
    private static final String DS_DEF_REQ_SERVICE_TYPE = "Document Submission Deferred Request";
    private static final String DS_DEF_RESP_SERVICE_TYPE = "Document Submission Deferred Response";
    private static final String AD_SERVICE_TYPE = "Admin Distribution";
    private static final String DIRECT_SERVICE_TYPE = "Direct";
    
    private static final Logger LOG = Logger.getLogger(EventCountServiceImpl.class);
    
    @Override
    public void setCounts(){
        List inboundResults = getEventLoggerDao().getCounts(INBOUND_EVENT_TYPE, INBOUND_HCID_TYPE);
        List outboundResults = getEventLoggerDao().getCounts(OUTBOUND_EVENT_TYPE, OUTBOUND_HCID_TYPE);
        
        setEvents(inboundResults, inboundOrganizations);
        setEvents(outboundResults, outboundOrganizations);
    }
    
    @Override
    public List<EventNwhinOrganization> getTotalOrganizations(){
        HashMap<String, EventNwhinOrganization> totalEvents = new HashMap<String, EventNwhinOrganization>();
        for(String hcid : inboundOrganizations.keySet()){
            totalEvents.put(hcid, inboundOrganizations.get(hcid));
        }
        
        for(String hcid : outboundOrganizations.keySet()){
            if(totalEvents.containsKey(hcid)){
                EventNwhinOrganization combinedOrg = 
                    combineOrganizations(outboundOrganizations.get(hcid), totalEvents.remove(hcid));
                totalEvents.put(hcid, combinedOrg);
            }else {
                totalEvents.put(hcid, outboundOrganizations.get(hcid));
            }
        }
                
        return new ArrayList(totalEvents.values());
    }
    
    @Override
    public List<EventNwhinOrganization> getInboundOrganizations(){
        return new ArrayList(inboundOrganizations.values());
    }
    
    @Override
    public List<EventNwhinOrganization> getOutboundOrganizations(){
        return new ArrayList(outboundOrganizations.values());
    }
    
    private void setEvents(List results, HashMap<String, EventNwhinOrganization> organizations) {
        organizations.clear();
        for(Object result : results){
            if(result instanceof Object[] &&
                ((Object[])result).length == 3) {
                Object[] resultArray = (Object[]) result;
                String hcid = setHcid((String) resultArray[1]);
                Integer count = (Integer) resultArray[0];
                String serviceType = (String) resultArray[2];
                
                if(hcid == null){
                    continue;
                }
                
                if(organizations.containsKey(hcid)){
                    organizations.put(hcid, updateEvent(organizations.remove(hcid), serviceType, count));
                }else {
                    organizations.put(hcid, updateEvent(serviceType, count, hcid));
                }
            }
        }
    }
    
    private String setHcid(String resultHcid){
        try {
            resultHcid = getConnectionManager().getBusinessEntityName(resultHcid);
        }catch(ConnectionManagerException e){
            LOG.warn("Exception getting name of HCID from ConnectionManager.", e);
        }
        return resultHcid;
    }
    
    private EventNwhinOrganization updateEvent(String serviceType, Integer count, String hcid) {
        EventNwhinOrganization organization = new EventNwhinOrganization();
        organization.setOrganizationName(hcid);
        return updateEvent(organization, serviceType, count);
    }
    
    private EventNwhinOrganization updateEvent(EventNwhinOrganization organization, String serviceType, Integer count) {
        
        if(serviceType.equalsIgnoreCase(PD_SERVICE_TYPE)){
            organization.setPdSyncCount(count);
        }else if(serviceType.equalsIgnoreCase(PD_DEF_REQ_SERVICE_TYPE)){
            organization.setPdDefReqCount(count);
        }else if(serviceType.equalsIgnoreCase(PD_DEF_RESP_SERVICE_TYPE)){
            organization.setPdDefRespCount(count);
        }else if(serviceType.equalsIgnoreCase(DQ_SERVICE_TYPE)){
            organization.setDqCount(count);
        }else if(serviceType.equalsIgnoreCase(DR_SERVICE_TYPE)){
            organization.setDrCount(count);
        }else if(serviceType.equalsIgnoreCase(DS_SERVICE_TYPE)){
            organization.setDsSyncCount(count);
        }else if(serviceType.equalsIgnoreCase(DS_DEF_REQ_SERVICE_TYPE)){
            organization.setDsDefReqCount(count);
        }else if(serviceType.equalsIgnoreCase(DS_DEF_RESP_SERVICE_TYPE)){
            organization.setDsDefRespCount(count);
        }else if(serviceType.equalsIgnoreCase(AD_SERVICE_TYPE)){
            organization.setAdCount(count);
        }else if(serviceType.equalsIgnoreCase(DIRECT_SERVICE_TYPE)){
            organization.setDirectCount(count);
        }//else do nothing
        
        return organization;
    }

    private EventNwhinOrganization combineOrganizations(EventNwhinOrganization inOrg, EventNwhinOrganization outOrg) {
        EventNwhinOrganization newOrg = new EventNwhinOrganization();
        newOrg.setOrganizationName(inOrg.getOrganizationName());
        
        newOrg.setPdSyncCount(inOrg.getPdSyncCount() + outOrg.getPdSyncCount());
        newOrg.setPdDefReqCount(inOrg.getPdDefReqCount() + outOrg.getDsDefReqCount());
        newOrg.setPdDefRespCount(inOrg.getPdDefRespCount() + outOrg.getPdDefRespCount());
        newOrg.setDqCount(inOrg.getDqCount() + outOrg.getDqCount());
        newOrg.setDrCount(inOrg.getDrCount() + outOrg.getDrCount());
        newOrg.setDsSyncCount(inOrg.getDsSyncCount() + outOrg.getDsSyncCount());
        newOrg.setDsDefReqCount(inOrg.getDsDefReqCount() + outOrg.getDsDefReqCount());
        newOrg.setDsDefRespCount(inOrg.getDsDefRespCount() + outOrg.getDsDefRespCount());
        newOrg.setAdCount(inOrg.getAdCount() + outOrg.getAdCount());
        newOrg.setDirectCount(inOrg.getDirectCount() + outOrg.getDirectCount());
        
        return newOrg;
    }

    protected DatabaseEventLoggerDao getEventLoggerDao(){
        return new DatabaseEventLoggerDao();
    }

    protected ConnectionManager getConnectionManager(){
        return ConnectionManagerCache.getInstance();
    }
    
}
