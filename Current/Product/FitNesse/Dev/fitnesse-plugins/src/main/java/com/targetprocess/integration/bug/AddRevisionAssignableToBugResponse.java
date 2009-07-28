
package com.targetprocess.integration.bug;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="AddRevisionAssignableToBugResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addRevisionAssignableToBugResult"
})
@XmlRootElement(name = "AddRevisionAssignableToBugResponse")
public class AddRevisionAssignableToBugResponse {

    @XmlElement(name = "AddRevisionAssignableToBugResult")
    protected int addRevisionAssignableToBugResult;

    /**
     * Gets the value of the addRevisionAssignableToBugResult property.
     * 
     */
    public int getAddRevisionAssignableToBugResult() {
        return addRevisionAssignableToBugResult;
    }

    /**
     * Sets the value of the addRevisionAssignableToBugResult property.
     * 
     */
    public void setAddRevisionAssignableToBugResult(int value) {
        this.addRevisionAssignableToBugResult = value;
    }

}
