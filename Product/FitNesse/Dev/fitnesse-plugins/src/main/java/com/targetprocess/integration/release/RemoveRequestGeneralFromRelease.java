
package com.targetprocess.integration.release;

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
 *         &lt;element name="releaseID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="requestGeneralID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "releaseID",
    "requestGeneralID"
})
@XmlRootElement(name = "RemoveRequestGeneralFromRelease")
public class RemoveRequestGeneralFromRelease {

    protected int releaseID;
    protected int requestGeneralID;

    /**
     * Gets the value of the releaseID property.
     * 
     */
    public int getReleaseID() {
        return releaseID;
    }

    /**
     * Sets the value of the releaseID property.
     * 
     */
    public void setReleaseID(int value) {
        this.releaseID = value;
    }

    /**
     * Gets the value of the requestGeneralID property.
     * 
     */
    public int getRequestGeneralID() {
        return requestGeneralID;
    }

    /**
     * Sets the value of the requestGeneralID property.
     * 
     */
    public void setRequestGeneralID(int value) {
        this.requestGeneralID = value;
    }

}
