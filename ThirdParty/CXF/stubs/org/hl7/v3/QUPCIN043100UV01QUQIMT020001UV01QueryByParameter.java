
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QUPC_IN043100UV01.QUQI_MT020001UV01.QueryByParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QUPC_IN043100UV01.QUQI_MT020001UV01.QueryByParameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="queryId" type="{urn:hl7-org:v3}II" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS"/>
 *         &lt;element name="modifyCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="responseElementGroupId" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="responseModalityCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="responsePriorityCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="initialQuantity" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;element name="initialQuantityCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="executionAndDeliveryTime" type="{urn:hl7-org:v3}TS_explicit" minOccurs="0"/>
 *         &lt;element name="parameterList" type="{urn:hl7-org:v3}QUPC_MT040300UV01.ParameterList" maxOccurs="unbounded"/>
 *         &lt;element name="sortControl" type="{urn:hl7-org:v3}QUQI_MT020001UV01.SortControl" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QUPC_IN043100UV01.QUQI_MT020001UV01.QueryByParameter", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "queryId",
    "statusCode",
    "modifyCode",
    "responseElementGroupId",
    "responseModalityCode",
    "responsePriorityCode",
    "initialQuantity",
    "initialQuantityCode",
    "executionAndDeliveryTime",
    "parameterList",
    "sortControl"
})
public class QUPCIN043100UV01QUQIMT020001UV01QueryByParameter {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected II queryId;
    @XmlElement(required = true)
    protected CS statusCode;
    protected CS modifyCode;
    protected List<II> responseElementGroupId;
    protected CS responseModalityCode;
    protected CS responsePriorityCode;
    protected INT initialQuantity;
    protected CE initialQuantityCode;
    protected TSExplicit executionAndDeliveryTime;
    @XmlElement(required = true)
    protected List<QUPCMT040300UV01ParameterList> parameterList;
    @XmlElement(nillable = true)
    protected List<QUQIMT020001UV01SortControl> sortControl;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;

    /**
     * Gets the value of the realmCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CS }
     * 
     * 
     */
    public List<CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<CS>();
        }
        return this.realmCode;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setTypeId(II value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<II>();
        }
        return this.templateId;
    }

    /**
     * Gets the value of the queryId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getQueryId() {
        return queryId;
    }

    /**
     * Sets the value of the queryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setQueryId(II value) {
        this.queryId = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setStatusCode(CS value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the modifyCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getModifyCode() {
        return modifyCode;
    }

    /**
     * Sets the value of the modifyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setModifyCode(CS value) {
        this.modifyCode = value;
    }

    /**
     * Gets the value of the responseElementGroupId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseElementGroupId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseElementGroupId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getResponseElementGroupId() {
        if (responseElementGroupId == null) {
            responseElementGroupId = new ArrayList<II>();
        }
        return this.responseElementGroupId;
    }

    /**
     * Gets the value of the responseModalityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getResponseModalityCode() {
        return responseModalityCode;
    }

    /**
     * Sets the value of the responseModalityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setResponseModalityCode(CS value) {
        this.responseModalityCode = value;
    }

    /**
     * Gets the value of the responsePriorityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getResponsePriorityCode() {
        return responsePriorityCode;
    }

    /**
     * Sets the value of the responsePriorityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setResponsePriorityCode(CS value) {
        this.responsePriorityCode = value;
    }

    /**
     * Gets the value of the initialQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getInitialQuantity() {
        return initialQuantity;
    }

    /**
     * Sets the value of the initialQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setInitialQuantity(INT value) {
        this.initialQuantity = value;
    }

    /**
     * Gets the value of the initialQuantityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getInitialQuantityCode() {
        return initialQuantityCode;
    }

    /**
     * Sets the value of the initialQuantityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setInitialQuantityCode(CE value) {
        this.initialQuantityCode = value;
    }

    /**
     * Gets the value of the executionAndDeliveryTime property.
     * 
     * @return
     *     possible object is
     *     {@link TSExplicit }
     *     
     */
    public TSExplicit getExecutionAndDeliveryTime() {
        return executionAndDeliveryTime;
    }

    /**
     * Sets the value of the executionAndDeliveryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSExplicit }
     *     
     */
    public void setExecutionAndDeliveryTime(TSExplicit value) {
        this.executionAndDeliveryTime = value;
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUPCMT040300UV01ParameterList }
     * 
     * 
     */
    public List<QUPCMT040300UV01ParameterList> getParameterList() {
        if (parameterList == null) {
            parameterList = new ArrayList<QUPCMT040300UV01ParameterList>();
        }
        return this.parameterList;
    }

    /**
     * Gets the value of the sortControl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sortControl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSortControl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUQIMT020001UV01SortControl }
     * 
     * 
     */
    public List<QUQIMT020001UV01SortControl> getSortControl() {
        if (sortControl == null) {
            sortControl = new ArrayList<QUQIMT020001UV01SortControl>();
        }
        return this.sortControl;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nullFlavor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNullFlavor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

}
