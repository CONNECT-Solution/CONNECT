
package com.targetprocess.integration.feature;

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
 *         &lt;element name="RetrieveCountResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "retrieveCountResult"
})
@XmlRootElement(name = "RetrieveCountResponse")
public class RetrieveCountResponse {

    @XmlElement(name = "RetrieveCountResult")
    protected int retrieveCountResult;

    /**
     * Gets the value of the retrieveCountResult property.
     * 
     */
    public int getRetrieveCountResult() {
        return retrieveCountResult;
    }

    /**
     * Sets the value of the retrieveCountResult property.
     * 
     */
    public void setRetrieveCountResult(int value) {
        this.retrieveCountResult = value;
    }

}
