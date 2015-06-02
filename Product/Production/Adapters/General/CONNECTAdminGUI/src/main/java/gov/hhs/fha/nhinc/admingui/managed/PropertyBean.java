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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.model.PropValue;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
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

    private String gatewayPropMsg = "gatwayPropMsg";
    private String adapterPropMsg = "adapterPropMsg";

    private static final Logger LOG = Logger.getLogger(PropertyBean.class);

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

    public void refresh() {
        setProperties();
    }

    public void onGatewayValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        PropValue selectedProp = (PropValue) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.GATEWAY_PROPERTY_FILE, selectedProp.getKey(), newValue);
            FacesContext.getCurrentInstance().addMessage(gatewayPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Property value changed for " + selectedProp.getKey()
                    + " from " + oldValue + " to " + newValue + "."));
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to update property: " + selectedProp.getKey());
            FacesContext.getCurrentInstance().addMessage(gatewayPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", "Unable to set property value: " + selectedProp.getKey()
                    + " from " + oldValue + " to " + newValue + "."));
        }
    }

     public void onAdapterValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        PropValue selectedProp = (PropValue) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        try {
            PropertyAccessor.getInstance().setProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME, selectedProp.getKey(), newValue);
            FacesContext.getCurrentInstance().addMessage(adapterPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Property value changed for " + selectedProp.getKey()
                    + " from " + oldValue + " to " + newValue + "."));
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to update property: " + selectedProp.getKey());
            FacesContext.getCurrentInstance().addMessage(adapterPropMsg,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", "Unable to set property value: " + selectedProp.getKey()
                    + " from " + oldValue + " to " + newValue + "."));
        }
    }

    private void setProperties() {
        gatewayProperties = new ArrayList<PropValue>();
        adapterProperties = new ArrayList<PropValue>();

        try {
            Properties gatewayProps = PropertyAccessor.getInstance().getProperties(NhincConstants.GATEWAY_PROPERTY_FILE);
            addProperties(gatewayProps, gatewayProperties, NhincConstants.GATEWAY_PROPERTY_FILE);
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to set " + NhincConstants.GATEWAY_PROPERTY_FILE + " properties file.", ex);
        }

        try {
            Properties adapterProps = PropertyAccessor.getInstance().getProperties(NhincConstants.ADAPTER_PROPERTY_FILE_NAME);
            addProperties(adapterProps, adapterProperties, NhincConstants.ADAPTER_PROPERTY_FILE_NAME);
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to set " + NhincConstants.ADAPTER_PROPERTY_FILE_NAME + " properties file.", ex);
        }
    }

    private void addProperties(Properties props, List<PropValue> viewProps, String propFileName) throws PropertyAccessException {
        if (props != null) {
            for (Object key : props.keySet()) {
                String strKey = (String) key;
                String value = props.getProperty(strKey);
                String text = PropertyAccessor.getInstance().getPropertyComment(propFileName, strKey);
                viewProps.add(new PropValue(strKey, value, text));
            }
        }
    }

}
