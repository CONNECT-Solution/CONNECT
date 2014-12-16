/*
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.application.EndpointManagerCache;
import gov.hhs.fha.nhinc.admingui.model.ConnectionEndpoint;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.log4j.Logger;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.Contact;
import org.uddi.api_v3.KeyedReference;

/**
 *
 * @author jassmit
 */
@ManagedBean(name = "connectionManagerBean")
@ViewScoped
public class ConnectionManagerBean {

    private static final Logger LOG = Logger.getLogger(ConnectionManagerBean.class);

    private static final String WSDL_SUFFIX = "?wsdl";
    
    private final HashMap<String, BusinessEntity> externalEntities = new HashMap<String, BusinessEntity>();
    private final List<String> entityNames = new ArrayList<String>();
    
    private BusinessEntity localEntity;
    private BusinessEntity selectedEntity;
    private String selectedEntityName;
    
    private List<ConnectionEndpoint> endpoints;
    private ConnectionEndpoint selectedEndpoint;
    
    @PostConstruct
    public void init() {
        refresh();
    }
    
    public List<String> getEntityNames(){
        return entityNames;
    }
    
    public String getSelectedEntityName() {
        String name = "--";
        if(selectedEntity != null && selectedEntity.getName() != null
                && !selectedEntity.getName().isEmpty()) {
            name = selectedEntity.getName().get(0).getValue();
        }
        return name;
    }
    
    public void setSelectedEntityName(String selectedEntityName) {
        if(selectedEntityName != null) {
            this.selectedEntityName = selectedEntityName;
            selectedEntity = externalEntities.get(this.selectedEntityName);
        }
    }
    
    public BusinessEntity getSelectedEntity(){
        return selectedEntity;
    }
    
    public String getSelectedEntityDescription(){
        String description = "--";
        
        if(selectedEntity != null && selectedEntity.getDescription() != null && !selectedEntity.getDescription().isEmpty()) {
            description = selectedEntity.getDescription().get(0).getValue();
        }
        
        return description;
    }
    
    public String getSelectedEntityRegions(){
        String regions = "--";
        if(selectedEntity != null && selectedEntity.getCategoryBag() != null
                && selectedEntity.getCategoryBag().getKeyedReference() != null
                && !selectedEntity.getCategoryBag().getKeyedReference().isEmpty()) {
            StringBuilder regionBuilder = new StringBuilder();
            for(KeyedReference ref : selectedEntity.getCategoryBag().getKeyedReference()){
               if(ref.getTModelKey().equals("uddi:uddi.org:ubr:categorization:iso3166")) {
                   regionBuilder.append(ref.getKeyName()).append(", ");
               }
            }
            if(regionBuilder.length() > 0) {
                regions = regionBuilder.substring(0, regionBuilder.length() - 2);
            }
        }
        return regions;
    }
    
    public String getSelectedEntityContact(){
        String contactValue = "--";
        
        if(selectedEntity != null && selectedEntity.getContacts() != null && selectedEntity.getContacts().getContact() != null
                && !selectedEntity.getContacts().getContact().isEmpty()) {
            Contact contact = selectedEntity.getContacts().getContact().get(0);
            
            if(contact.getPersonName() != null && !contact.getPersonName().isEmpty()) {
                contactValue = contact.getPersonName().get(0).getValue();
            }
        }
        
        return contactValue;
    }
    
    public String getSelectedEntityHcid(){
        String hcid = "--";
        if(selectedEntity != null && selectedEntity.getIdentifierBag() != null
                && selectedEntity.getIdentifierBag().getKeyedReference() != null
                && !selectedEntity.getIdentifierBag().getKeyedReference().isEmpty()) {
           for(KeyedReference ref : selectedEntity.getIdentifierBag().getKeyedReference()) {
               if(ref.getTModelKey().equals("uddi:nhin:nhie:homecommunityid")) {
                   hcid = ref.getKeyValue();
               }
           } 
        }
        return hcid;
    }
    
    public void ping(){
        if(selectedEndpoint != null) {
            boolean status = pingService(selectedEndpoint.getServiceUrl());
            Date timestamp = new Date();
            EndpointManagerCache.getInstance().addOrUpdateEndPoint(selectedEndpoint.getServiceUrl(), timestamp, status);
        }
    }
    
    public List<ConnectionEndpoint> getEndpoints(){
        endpoints = new ArrayList<ConnectionEndpoint>();
        if(selectedEntity != null && selectedEntity.getBusinessKey() != null
                && selectedEntity.getBusinessServices().getBusinessService() != null
                && !selectedEntity.getBusinessServices().getBusinessService().isEmpty()) {
            for(BusinessService bService : selectedEntity.getBusinessServices().getBusinessService()) {
                if(bService.getBindingTemplates() != null && bService.getBindingTemplates().getBindingTemplate() != null) { 
                    for(BindingTemplate template : bService.getBindingTemplates().getBindingTemplate()) {
                        String url = template.getAccessPoint().getValue();
                        String version = getSpecVersion(template.getCategoryBag());
                        EndpointManagerCache.EndpointCacheInfo info = EndpointManagerCache.getInstance().getEndpointInfo(url);
                        
                        String timestamp = null;
                        String status = "None";
                        
                        if(info != null) {
                            timestamp = DateUtils.formatDate(info.getTimestamp(), "MM-dd-yy hh:mm:ss");
                            status = info.isPingResult() ? "Pass" : "Fail";
                        }                         
                        endpoints.add(new ConnectionEndpoint(bService.getName().get(0).getValue(), url, version, status, timestamp));
                    } 
                }    
            }
        }
        return endpoints;
    }
    
    public void refreshConnections(){
        refresh();
    }
    
    private void refresh() {
        try {
            List<BusinessEntity> externalEntityList = ConnectionManagerCache.getInstance().getAllBusinessEntities();
            String localHcid = PropertyAccessor.getInstance().getProperty(NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            
            for(int i = 0; i < externalEntityList.size(); i++) {
                BusinessEntity entity = externalEntityList.get(i);
                if(entity.getIdentifierBag() != null && entity.getIdentifierBag().getKeyedReference() != null
                        && !entity.getIdentifierBag().getKeyedReference().isEmpty()){
                    if(isLocalEntity(entity.getIdentifierBag().getKeyedReference(), localHcid)){
                        localEntity = entity;
                    } else if (entity.getName() != null && !entity.getName().isEmpty()) {
                        externalEntities.put(entity.getName().get(0).getValue(), entity);                      
                    }
                }
            }
        } catch (ConnectionManagerException ex) {
            LOG.error(ex, ex);
        } catch (PropertyAccessException ex) {
            LOG.error(ex, ex);
        }
        
        entityNames.addAll(externalEntities.keySet());
        if(!entityNames.isEmpty()) {
            selectedEntity = externalEntities.get(entityNames.get(0));
        }
    }
       
    private boolean isLocalEntity(List<KeyedReference> references, String localHcid) {
        for(KeyedReference ref : references) {
            if(ref.getTModelKey().equalsIgnoreCase("uddi:nhin:nhie:homecommunityid")
                    && formatHcid(localHcid).equals(formatHcid(ref.getKeyValue()))) {
                return true;
            }
        }
        return false;
    }
    
    private String formatHcid(String hcid){
        String formattedHcid = hcid;
        if(hcid.startsWith(NhincConstants.HCID_PREFIX)) {
            formattedHcid = hcid.substring(NhincConstants.HCID_PREFIX.length(), hcid.length());
        }
        return formattedHcid;
    }   

    public boolean pingService(String url) {
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader in = null;
        try {
            URL webserviceUrl = new URL(prepUrl(url));
            HttpsURLConnection.setDefaultHostnameVerifier(getHostNameVerifier());
            HttpURLConnection con = (HttpURLConnection) webserviceUrl.openConnection();

            is = con.getInputStream();
            isReader = new InputStreamReader(is);

            in = new BufferedReader(isReader);
            String inputLine;
            StringBuilder pingOutput = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                pingOutput.append(inputLine);
            }

            LOG.info(pingOutput.toString());
            con.disconnect();
            return true;
        } catch (IOException ex) {
            LOG.warn(ex, ex);
        } finally {
            StreamUtils.closeStreamSilently(is);
            StreamUtils.closeFileSilently(isReader);
            StreamUtils.closeFileSilently(in);
        }
        return false;
    }

    public ConnectionEndpoint getSelectedEndpoint() {
        return selectedEndpoint;
    }

    public void setSelectedEndpoint(ConnectionEndpoint selectedEndpoint) {
        this.selectedEndpoint = selectedEndpoint;
    }

    private String getSpecVersion(CategoryBag categoryBag) {
        if (categoryBag != null && categoryBag.getKeyedReference() != null
                && !categoryBag.getKeyedReference().isEmpty()) {
            for (KeyedReference kRef : categoryBag.getKeyedReference()) {
                if (kRef.getTModelKey() != null && kRef.getTModelKey().equals(ConnectionManagerCache.UDDI_SPEC_VERSION_KEY)) {
                    return kRef.getKeyValue();
                }
            }
        }
        return "-";
    }

    private String prepUrl(String serviceUrl) {
        if (!serviceUrl.endsWith(WSDL_SUFFIX)) {
            serviceUrl = serviceUrl.concat(WSDL_SUFFIX);
        }
        return serviceUrl;
    }

    private HostnameVerifier getHostNameVerifier() {
        return new javax.net.ssl.HostnameVerifier(){
            @Override
            public boolean verify(String hostname, 
                    javax.net.ssl.SSLSession sslSession) {
                return true;
            }
        };
    }
}
