/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Mastan.Ketha
 */
/**
 * <p>Java class for SubjectMatchType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SubjectMatchType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttributeValue" type="{}AttributeValueType"/>
 *         &lt;element name="SubjectAttributeDesignator" type="{}AttributeDesignatorType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="MatchId" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectMatchType", propOrder = {
    "attributeValue",
    "subjectAttributeDesignator"
})
public class SubjectMatchType {

    @XmlElement(name = "AttributeValue", required = true)
    protected AttributeValueType attributeValue;
    @XmlElement(name = "SubjectAttributeDesignator", required = true)
    protected AttributeDesignatorType subjectAttributeDesignator;
    @XmlAttribute(name = "MatchId", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String matchId;

    /**
     * Gets the value of the attributeValue property.
     *
     * @return
     *     possible object is
     *     {@link AttributeValueType }
     *
     */
    public AttributeValueType getAttributeValue() {
        return attributeValue;
    }

    /**
     * Sets the value of the attributeValue property.
     *
     * @param value
     *     allowed object is
     *     {@link AttributeValueType }
     *
     */
    public void setAttributeValue(AttributeValueType value) {
        this.attributeValue = value;
    }

    /**
     * Gets the value of the subjectAttributeDesignator property.
     *
     * @return
     *     possible object is
     *     {@link AttributeDesignatorType }
     *
     */
    public AttributeDesignatorType getSubjectAttributeDesignator() {
        return subjectAttributeDesignator;
    }

    /**
     * Sets the value of the subjectAttributeDesignator property.
     *
     * @param value
     *     allowed object is
     *     {@link AttributeDesignatorType }
     *
     */
    public void setSubjectAttributeDesignator(AttributeDesignatorType value) {
        this.subjectAttributeDesignator = value;
    }

    /**
     * Gets the value of the matchId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMatchId() {
        return matchId;
    }

    /**
     * Sets the value of the matchId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMatchId(String value) {
        this.matchId = value;
    }
}
