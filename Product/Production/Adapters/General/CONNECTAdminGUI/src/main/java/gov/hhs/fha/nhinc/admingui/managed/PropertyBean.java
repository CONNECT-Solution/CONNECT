/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services. 
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

    private List<PropValue> properties;

    private static final Logger LOG = Logger.getLogger(PropertyBean.class);

    public PropertyBean() {
        setProperties();
    }

    public List<PropValue> getProperties() {
        return properties;
    }

    public void refresh() {
        setProperties();
    }

    public void onValueEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        PropValue selectedProp = (PropValue) dataTable.getRowData();
        String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        try {
            PropertyAccessor.getInstance().setProperty("gateway", selectedProp.getKey(), newValue);
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Property value changed for " + selectedProp.getKey()
                    + " from " + oldValue + " to " + newValue + "."));
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to update property: " + selectedProp.getKey());
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", "Unable to set property value: " + selectedProp.getKey()
                    + " from " + oldValue + " to " + newValue + "."));
        }
    }

    private void setProperties() {
        properties = new ArrayList<PropValue>();
        try {
            Properties props = PropertyAccessor.getInstance().getProperties("gateway");

            if (props != null) {
                for (Object key : props.keySet()) {
                    String strKey = (String) key;
                    String value = props.getProperty(strKey);
                    String text = PropertyAccessor.getInstance().getPropertyComment("gateway", strKey);
                    properties.add(new PropValue(strKey, value, text));
                }
            }
        } catch (PropertyAccessException ex) {
            LOG.warn("Unable to set properties file.", ex);
        }
    }

}
