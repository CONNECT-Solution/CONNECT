/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.admingui.model.PropValue;
import gov.hhs.fha.nhinc.admingui.services.InternalExchangeManagerService;
import gov.hhs.fha.nhinc.admingui.services.PropertyService;
import gov.hhs.fha.nhinc.admingui.services.impl.InternalExchangeManagerServiceImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.PropertyServiceImpl;
import gov.hhs.fha.nhinc.common.internalexchangemanagement.EndpointPropertyType;
import gov.hhs.fha.nhinc.common.propertyaccess.PropertyType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author jassmit
 */
@ManagedBean(name = "propertyBean")
@ViewScoped
public class PropertyBean {

    private List<PropValue> gatewayProperties;
    private List<PropValue> adapterProperties;
    private List<PropValue> auditProperties;
    private List<EndpointPropertyType> internalEndpointsProperties;

    private String gatewayPropMsg = "gatwayPropMsg";
    private String adapterPropMsg = "adapterPropMsg";
    private String auditPropMsg = "auditPropMsg";
    private String internalEndpointsPropMsg = "internalEndpointsPropMsg";
    private static final String PROP_UPDATE_MSG = "Property value changed for ";
    private static final String PROP_UPDATE_FAIL_MSG = "Unable to set property value: ";
    private PropertyService propertyService = new PropertyServiceImpl();
    private InternalExchangeManagerService internalExchangeManagerService = new InternalExchangeManagerServiceImpl();

    public PropertyBean() {
        setProperties();
    }

    public List<PropValue> getGatewayProperties() {
        return gatewayProperties;
    }

    public List<PropValue> getAdapterProperties() {
        return adapterProperties;
    }

    public String getGatewayPropMsg() {
        return gatewayPropMsg;
    }

    public String getAdapterPropMsg() {
        return adapterPropMsg;
    }

    public List<PropValue> getAuditProperties() {
        return auditProperties;
    }

    public String getAuditPropMsg() {
        return auditPropMsg;
    }

    public List<EndpointPropertyType> getInternalEndpointsProperties() {
        return internalEndpointsProperties;
    }

    public String getInternalEndpointsPropMsg() {
        return internalEndpointsPropMsg;
    }

    public void refresh() {
        setProperties();
    }

    public void onGatewayValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        PropValue selectedProp = (PropValue) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        boolean status = propertyService.saveProperty(NhincConstants.GATEWAY_PROPERTY_FILE, selectedProp.getKey(),
            newValue);

        if (status) {
            FacesContext.getCurrentInstance().addMessage(gatewayPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", getPropertiesUpdateMessage(PROP_UPDATE_MSG,
                    selectedProp.getKey(), oldValue, newValue)));
        } else {
            FacesContext.getCurrentInstance().addMessage(gatewayPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", getPropertiesUpdateMessage(PROP_UPDATE_FAIL_MSG,
                    selectedProp.getKey(), oldValue, newValue)));
        }
    }

    public void onAdapterValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        PropValue selectedProp = (PropValue) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        boolean status = propertyService.saveProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME, selectedProp.getKey(),
            newValue);

        if (status) {
            FacesContext.getCurrentInstance().addMessage(adapterPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", getPropertiesUpdateMessage(PROP_UPDATE_MSG,
                    selectedProp.getKey(), oldValue, newValue)));
        } else {
            FacesContext.getCurrentInstance().addMessage(adapterPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", getPropertiesUpdateMessage(PROP_UPDATE_FAIL_MSG,
                    selectedProp.getKey(), oldValue, newValue)));
        }
    }

    public void onAuditValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        PropValue selectedProp = (PropValue) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        boolean status = propertyService.saveProperty(NhincConstants.AUDIT_LOGGING_PROPERTY_FILE, selectedProp.getKey(),
            newValue);

        if (status) {
            FacesContext.getCurrentInstance().addMessage(auditPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", getPropertiesUpdateMessage(PROP_UPDATE_MSG,
                    selectedProp.getKey(), oldValue, newValue)));
        } else {
            FacesContext.getCurrentInstance().addMessage(auditPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", getPropertiesUpdateMessage(PROP_UPDATE_FAIL_MSG,
                    selectedProp.getKey(), oldValue, newValue)));
        }
    }

    public void onInternalEndpointsValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        EndpointPropertyType selectedProp = (EndpointPropertyType) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        boolean status = internalExchangeManagerService.updateEndpoint(selectedProp);

        if (status) {
            FacesContext.getCurrentInstance().addMessage(internalEndpointsPropMsg, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "INFO", getPropertiesUpdateMessage(PROP_UPDATE_MSG, selectedProp.getName(), oldValue, newValue)));
        } else {
            FacesContext.getCurrentInstance().addMessage(internalEndpointsPropMsg, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "WARN",
                getPropertiesUpdateMessage(PROP_UPDATE_FAIL_MSG, selectedProp.getName(), oldValue, newValue)));
        }
    }

    private void setProperties() {
        gatewayProperties = convertPropValue(propertyService.listProperties(NhincConstants.GATEWAY_PROPERTY_FILE));
        adapterProperties = convertPropValue(propertyService.listProperties(NhincConstants.ADAPTER_PROPERTY_FILE_NAME));
        auditProperties = convertPropValue(propertyService.listProperties(NhincConstants.AUDIT_LOGGING_PROPERTY_FILE));
        internalEndpointsProperties = internalExchangeManagerService.getAllEndpoints();
    }

    private static List<PropValue> convertPropValue(List<PropertyType> ptList) {
        List<PropValue> pvList = new ArrayList();
        for (PropertyType pt : ptList) {
            pvList.add(new PropValue(pt.getPropertyName(), pt.getPropertyValue(), pt.getPropertyText()));
        }
        return pvList;
    }

    private static String getPropertiesUpdateMessage(String msg, String key, String oldValue, String newValue) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(msg).append(key).append(" from ").append(oldValue).append(" to ").append(newValue).append(".");
        return buffer.toString();
    }
}
