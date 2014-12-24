/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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

    private List<PropValue> properties;

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
            PropertyAccessor.getInstance().setProperty("gateway", selectedProp.key, newValue);
            PropertyAccessor.getInstance().saveProperties("gateway");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "INFO", "Property value changed for " + selectedProp.key
                            + " from " + oldValue + " to " + newValue + "."));
        } catch (PropertyAccessException ex) {

        } catch (IOException ex) {
           
        }
    }

    private void setProperties() {
        properties = new ArrayList<PropValue>();
        try {
            Properties props = PropertyAccessor.getInstance().getProperties("gateway");
            Properties propDesc = PropertyAccessor.getInstance().getProperties("gatewayText");
            if (props != null) {
                for (Object key : props.keySet()) {
                    String strKey = (String) key;
                    String value = props.getProperty(strKey);
                    String text = propDesc.getProperty(strKey);
                    properties.add(new PropValue(strKey, value, text));
                }
            }
        } catch (PropertyAccessException ex) {

        }
    }

    public class PropValue {

        private String key;
        private String value;
        private String text;

        public PropValue(String key, String value, String text) {
            this.key = key;
            this.value = value;
            this.text = text;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
