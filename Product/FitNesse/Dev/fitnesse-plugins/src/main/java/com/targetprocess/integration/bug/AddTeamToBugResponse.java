
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
 *         &lt;element name="AddTeamToBugResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addTeamToBugResult"
})
@XmlRootElement(name = "AddTeamToBugResponse")
public class AddTeamToBugResponse {

    @XmlElement(name = "AddTeamToBugResult")
    protected int addTeamToBugResult;

    /**
     * Gets the value of the addTeamToBugResult property.
     * 
     */
    public int getAddTeamToBugResult() {
        return addTeamToBugResult;
    }

    /**
     * Sets the value of the addTeamToBugResult property.
     * 
     */
    public void setAddTeamToBugResult(int value) {
        this.addTeamToBugResult = value;
    }

}
