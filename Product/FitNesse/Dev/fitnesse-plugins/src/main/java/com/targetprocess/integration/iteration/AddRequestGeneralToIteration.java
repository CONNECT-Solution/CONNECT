
package com.targetprocess.integration.iteration;

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
 *         &lt;element name="iterationID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="requestGeneral" type="{http://targetprocess.com}RequestGeneralDTO" minOccurs="0"/>
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
    "iterationID",
    "requestGeneral"
})
@XmlRootElement(name = "AddRequestGeneralToIteration")
public class AddRequestGeneralToIteration {

    protected int iterationID;
    protected RequestGeneralDTO requestGeneral;

    /**
     * Gets the value of the iterationID property.
     * 
     */
    public int getIterationID() {
        return iterationID;
    }

    /**
     * Sets the value of the iterationID property.
     * 
     */
    public void setIterationID(int value) {
        this.iterationID = value;
    }

    /**
     * Gets the value of the requestGeneral property.
     * 
     * @return
     *     possible object is
     *     {@link RequestGeneralDTO }
     *     
     */
    public RequestGeneralDTO getRequestGeneral() {
        return requestGeneral;
    }

    /**
     * Sets the value of the requestGeneral property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestGeneralDTO }
     *     
     */
    public void setRequestGeneral(RequestGeneralDTO value) {
        this.requestGeneral = value;
    }

}
