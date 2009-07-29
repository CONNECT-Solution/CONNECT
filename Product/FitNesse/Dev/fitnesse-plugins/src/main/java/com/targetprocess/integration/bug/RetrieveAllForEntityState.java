
package com.targetprocess.integration.bug;

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
 *         &lt;element name="entityStateID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "entityStateID"
})
@XmlRootElement(name = "RetrieveAllForEntityState")
public class RetrieveAllForEntityState {

    protected int entityStateID;

    /**
     * Gets the value of the entityStateID property.
     * 
     */
    public int getEntityStateID() {
        return entityStateID;
    }

    /**
     * Sets the value of the entityStateID property.
     * 
     */
    public void setEntityStateID(int value) {
        this.entityStateID = value;
    }

}
