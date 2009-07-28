
package com.targetprocess.integration.time;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customActivityID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "customActivityID"
})
@XmlRootElement(name = "RetrieveAllForCustomActivity")
public class RetrieveAllForCustomActivity {

    protected int customActivityID;

    /**
     * Gets the value of the customActivityID property.
     * 
     */
    public int getCustomActivityID() {
        return customActivityID;
    }

    /**
     * Sets the value of the customActivityID property.
     * 
     */
    public void setCustomActivityID(int value) {
        this.customActivityID = value;
    }

}
