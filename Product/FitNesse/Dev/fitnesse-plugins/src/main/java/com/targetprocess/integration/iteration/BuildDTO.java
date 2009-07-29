
package com.targetprocess.integration.iteration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for BuildDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BuildDTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://targetprocess.com}DataTransferObject">
 *       &lt;sequence>
 *         &lt;element name="BuildID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BuildDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="IterationID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ReleaseID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="OwnerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ProjectID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IterationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReleaseName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BuildDTO", propOrder = {
    "buildID",
    "name",
    "buildDate",
    "iterationID",
    "releaseID",
    "ownerID",
    "projectID",
    "iterationName",
    "releaseName",
    "projectName"
})
public class BuildDTO
    extends DataTransferObject
{

    @XmlElement(name = "BuildID", required = true, type = Integer.class, nillable = true)
    protected Integer buildID;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "BuildDate", required = true, nillable = true)
    protected XMLGregorianCalendar buildDate;
    @XmlElement(name = "IterationID", required = true, type = Integer.class, nillable = true)
    protected Integer iterationID;
    @XmlElement(name = "ReleaseID", required = true, type = Integer.class, nillable = true)
    protected Integer releaseID;
    @XmlElement(name = "OwnerID", required = true, type = Integer.class, nillable = true)
    protected Integer ownerID;
    @XmlElement(name = "ProjectID", required = true, type = Integer.class, nillable = true)
    protected Integer projectID;
    @XmlElement(name = "IterationName")
    protected String iterationName;
    @XmlElement(name = "ReleaseName")
    protected String releaseName;
    @XmlElement(name = "ProjectName")
    protected String projectName;

    /**
     * Gets the value of the buildID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBuildID() {
        return buildID;
    }

    /**
     * Sets the value of the buildID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBuildID(Integer value) {
        this.buildID = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the buildDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBuildDate() {
        return buildDate;
    }

    /**
     * Sets the value of the buildDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBuildDate(XMLGregorianCalendar value) {
        this.buildDate = value;
    }

    /**
     * Gets the value of the iterationID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIterationID() {
        return iterationID;
    }

    /**
     * Sets the value of the iterationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIterationID(Integer value) {
        this.iterationID = value;
    }

    /**
     * Gets the value of the releaseID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReleaseID() {
        return releaseID;
    }

    /**
     * Sets the value of the releaseID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReleaseID(Integer value) {
        this.releaseID = value;
    }

    /**
     * Gets the value of the ownerID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOwnerID() {
        return ownerID;
    }

    /**
     * Sets the value of the ownerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOwnerID(Integer value) {
        this.ownerID = value;
    }

    /**
     * Gets the value of the projectID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getProjectID() {
        return projectID;
    }

    /**
     * Sets the value of the projectID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setProjectID(Integer value) {
        this.projectID = value;
    }

    /**
     * Gets the value of the iterationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIterationName() {
        return iterationName;
    }

    /**
     * Sets the value of the iterationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIterationName(String value) {
        this.iterationName = value;
    }

    /**
     * Gets the value of the releaseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseName() {
        return releaseName;
    }

    /**
     * Sets the value of the releaseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseName(String value) {
        this.releaseName = value;
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjectName(String value) {
        this.projectName = value;
    }

}
