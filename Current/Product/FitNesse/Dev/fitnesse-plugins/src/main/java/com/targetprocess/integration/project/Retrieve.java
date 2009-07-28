
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
 *         &lt;element name="hql" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parameters" type="{http://targetprocess.com}ArrayOfAnyType" minOccurs="0"/>
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
    "hql",
    "parameters"
})
@XmlRootElement(name = "Retrieve")
public class Retrieve {

    protected String hql;
    protected ArrayOfAnyType parameters;

    /**
     * Gets the value of the hql property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHql() {
        return hql;
    }

    /**
     * Sets the value of the hql property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHql(String value) {
        this.hql = value;
    }

    /**
     * Gets the value of the parameters property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setParameters(ArrayOfAnyType value) {
        this.parameters = value;
    }

}
