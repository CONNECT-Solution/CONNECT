/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.event.service;

import gov.hhs.fha.nhinc.admingui.event.model.EventNwhinOrganization;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import gov.hhs.fha.nhinc.event.model.DatabaseEvent;
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
public class EventServiceImpl implements EventService {

    private static final HashMap<String, EventNwhinOrganization> inboundOrganizations = new HashMap<String, EventNwhinOrganization>();
    private static final HashMap<String, EventNwhinOrganization> outboundOrganizations = new HashMap<String, EventNwhinOrganization>();

    public static final String INBOUND_EVENT_TYPE = "END_INBOUND_MESSAGE";
    public static final String OUTBOUND_EVENT_TYPE = "END_INVOCATION_TO_NWHIN";
    public static final String INBOUND_HCID_TYPE = "initiatorHcid";
    public static final String OUTBOUND_HCID_TYPE = "respondingHcid";
    public static final String INBOUND_DIRECT_EVENT_TYPE = "END_INBOUND_DIRECT";
    public static final String OUTBOUND_DIRECT_EVENT_TYPE = "END_OUTBOUND_DIRECT";

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

    private static final Logger LOG = Logger.getLogger(EventServiceImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.event.service.EventCountService#setCounts
     */
    @Override
    public void setCounts() {
        List inboundResults = getEventLoggerDao().getCounts(INBOUND_EVENT_TYPE, INBOUND_HCID_TYPE);
        List outboundResults = getEventLoggerDao().getCounts(OUTBOUND_EVENT_TYPE, OUTBOUND_HCID_TYPE);
        List inboundDirectResults = getEventLoggerDao().getCounts(INBOUND_DIRECT_EVENT_TYPE, INBOUND_HCID_TYPE);
        List outboundDirectResults = getEventLoggerDao().getCounts(OUTBOUND_DIRECT_EVENT_TYPE, OUTBOUND_HCID_TYPE);

        if (null != inboundResults && !inboundResults.isEmpty()) {
            if (null != inboundDirectResults && !inboundDirectResults.isEmpty()) {
                inboundResults.addAll(inboundDirectResults);
            }
        } else {
            inboundResults = inboundDirectResults;
        }

        if (null != outboundResults && !outboundResults.isEmpty()) {
            if (null != outboundDirectResults && !outboundDirectResults.isEmpty()) {
                outboundResults.addAll(outboundDirectResults);
            }
        } else {
            outboundResults = outboundDirectResults;
        }

        setEvents(inboundResults, inboundOrganizations);
        setEvents(outboundResults, outboundOrganizations);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.event.service.EventCountService#getTotalOrganizations
     */
    @Override
    public List<EventNwhinOrganization> getTotalOrganizations() {
        HashMap<String, EventNwhinOrganization> totalEvents = new HashMap<String, EventNwhinOrganization>();
        for (String hcid : inboundOrganizations.keySet()) {
            totalEvents.put(hcid, inboundOrganizations.get(hcid));
        }

        for (String hcid : outboundOrganizations.keySet()) {
            if (totalEvents.containsKey(hcid)) {
                EventNwhinOrganization combinedOrg
                    = combineOrganizations(outboundOrganizations.get(hcid), totalEvents.remove(hcid));
                totalEvents.put(hcid, combinedOrg);
            } else {
                totalEvents.put(hcid, outboundOrganizations.get(hcid));
            }
        }

        return new ArrayList(totalEvents.values());
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.event.service.EventCountService#getInboundOrganizations
     */
    @Override
    public List<EventNwhinOrganization> getInboundOrganizations() {
        return new ArrayList(inboundOrganizations.values());
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.event.service.EventCountService#getOutboundOrganizations
     */
    @Override
    public List<EventNwhinOrganization> getOutboundOrganizations() {
        return new ArrayList(outboundOrganizations.values());
    }

    private void setEvents(List results, HashMap<String, EventNwhinOrganization> organizations) {
        organizations.clear();
        for (Object result : results) {
            if (result instanceof Object[]
                && ((Object[]) result).length == 3) {
                Object[] resultArray = (Object[]) result;
                String hcid = setHcid((String) resultArray[1]);
                Integer count = (Integer) resultArray[0];
                String serviceType = (String) resultArray[2];

                if (hcid == null) {
                    continue;
                }

                if (organizations.containsKey(hcid)) {
                    organizations.put(hcid, updateEvent(organizations.remove(hcid), serviceType, count));
                } else {
                    organizations.put(hcid, updateEvent(serviceType, count, hcid));
                }
            }
        }
    }

    private String setHcid(String resultHcid) {
        try {
            resultHcid = getConnectionManager().getBusinessEntityName(resultHcid);
        } catch (ConnectionManagerException e) {
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

        if (serviceType.equalsIgnoreCase(PD_SERVICE_TYPE)) {
            organization.setPdSyncCount(count);
        } else if (serviceType.equalsIgnoreCase(PD_DEF_REQ_SERVICE_TYPE)) {
            organization.setPdDefReqCount(count);
        } else if (serviceType.equalsIgnoreCase(PD_DEF_RESP_SERVICE_TYPE)) {
            organization.setPdDefRespCount(count);
        } else if (serviceType.equalsIgnoreCase(DQ_SERVICE_TYPE)) {
            organization.setDqCount(count);
        } else if (serviceType.equalsIgnoreCase(DR_SERVICE_TYPE)) {
            organization.setDrCount(count);
        } else if (serviceType.equalsIgnoreCase(DS_SERVICE_TYPE)) {
            organization.setDsSyncCount(count);
        } else if (serviceType.equalsIgnoreCase(DS_DEF_REQ_SERVICE_TYPE)) {
            organization.setDsDefReqCount(count);
        } else if (serviceType.equalsIgnoreCase(DS_DEF_RESP_SERVICE_TYPE)) {
            organization.setDsDefRespCount(count);
        } else if (serviceType.equalsIgnoreCase(AD_SERVICE_TYPE)) {
            organization.setAdCount(count);
        } else if (serviceType.equalsIgnoreCase(DIRECT_SERVICE_TYPE)) {
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

    /**
     *
     * @return
     */
    protected DatabaseEventLoggerDao getEventLoggerDao() {
        return new DatabaseEventLoggerDao();
    }

    /**
     *
     * @return
     */
    protected ConnectionManager getConnectionManager() {
        return ConnectionManagerCache.getInstance();
    }

    /**
     *
     * @return
     */
    @Override
    public DatabaseEvent getLatestInbound() {
        return getEventLoggerDao().getLatestEvent(INBOUND_EVENT_TYPE);
    }

    /**
     *
     * @return
     */
    @Override
    public DatabaseEvent getLatestOutbound() {
        return getEventLoggerDao().getLatestEvent(OUTBOUND_EVENT_TYPE);
    }

}
