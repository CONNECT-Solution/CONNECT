
package com.targetprocess.integration.userstory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RevisionAssignableDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RevisionAssignableDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="RevisionAssignableID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AssignableID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="RevisionID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AssignableName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RevisionAssignableDTO", propOrder = {
    "revisionAssignableID",
    "assignableID",
    "revisionID",
    "assignableName"
})
public class RevisionAssignableDTO
    extends DataTransferObject
{

    @XmlElement(name = "RevisionAssignableID", required = true, type = Integer.class, nillable = true)
    protected Integer revisionAssignableID;
    @XmlElement(name = "AssignableID", required = true, type = Integer.class, nillable = true)
    protected Integer assignableID;
    @XmlElement(name = "RevisionID", required = true, type = Integer.class, nillable = true)
    protected Integer revisionID;
    @XmlElement(name = "AssignableName")
    protected String assignableName;

    /**
     * Gets the value of the revisionAssignableID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRevisionAssignableID() {
        return revisionAssignableID;
    }

    /**
     * Sets the value of the revisionAssignableID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRevisionAssignableID(Integer value) {
        this.revisionAssignableID = value;
    }

    /**
     * Gets the value of the assignableID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAssignableID() {
        return assignableID;
    }

    /**
     * Sets the value of the assignableID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAssignableID(Integer value) {
        this.assignableID = value;
    }

    /**
     * Gets the value of the revisionID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRevisionID() {
        return revisionID;
    }

    /**
     * Sets the value of the revisionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRevisionID(Integer value) {
        this.revisionID = value;
    }

    /**
     * Gets the value of the assignableName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignableName() {
        return assignableName;
    }

    /**
     * Sets the value of the assignableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignableName(String value) {
        this.assignableName = value;
    }

}
