
package com.targetprocess.integration.task;

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
 *         &lt;element name="taskID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "taskID",
    "requestGeneral"
})
@XmlRootElement(name = "AddRequestGeneralToTask")
public class AddRequestGeneralToTask {

    protected int taskID;
    protected RequestGeneralDTO requestGeneral;

    /**
     * Gets the value of the taskID property.
     * 
     */
    public int getTaskID() {
        return taskID;
    }

    /**
     * Sets the value of the taskID property.
     * 
     */
    public void setTaskID(int value) {
        this.taskID = value;
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
