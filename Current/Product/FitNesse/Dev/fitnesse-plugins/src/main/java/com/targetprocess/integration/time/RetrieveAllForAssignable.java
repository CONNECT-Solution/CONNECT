
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
 *         &lt;element name="assignableID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "assignableID"
})
@XmlRootElement(name = "RetrieveAllForAssignable")
public class RetrieveAllForAssignable {

    protected int assignableID;

    /**
     * Gets the value of the assignableID property.
     * 
     */
    public int getAssignableID() {
        return assignableID;
    }

    /**
     * Sets the value of the assignableID property.
     * 
     */
    public void setAssignableID(int value) {
        this.assignableID = value;
    }

}
