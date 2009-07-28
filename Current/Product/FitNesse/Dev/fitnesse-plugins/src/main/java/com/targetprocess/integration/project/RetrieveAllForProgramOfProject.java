
package com.targetprocess.integration.project;

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
 *         &lt;element name="programOfProjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "programOfProjectID"
})
@XmlRootElement(name = "RetrieveAllForProgramOfProject")
public class RetrieveAllForProgramOfProject {

    protected int programOfProjectID;

    /**
     * Gets the value of the programOfProjectID property.
     * 
     */
    public int getProgramOfProjectID() {
        return programOfProjectID;
    }

    /**
     * Sets the value of the programOfProjectID property.
     * 
     */
    public void setProgramOfProjectID(int value) {
        this.programOfProjectID = value;
    }

}
