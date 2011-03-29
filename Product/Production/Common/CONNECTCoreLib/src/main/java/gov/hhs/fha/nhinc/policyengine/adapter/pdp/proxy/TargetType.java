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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Mastan.Ketha
 */
/**
 * <p>Java class for TargetType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TargetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Subjects" type="{}SubjectsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetType", propOrder = {
    "subjects"
})
public class TargetType {

    @XmlElement(name = "Subjects")
    protected SubjectsType subjects;

    /**
     * Gets the value of the subjects property.
     *
     * @return
     *     possible object is
     *     {@link SubjectsType }
     *
     */
    public SubjectsType getSubjects() {
        return subjects;
    }

    /**
     * Sets the value of the subjects property.
     *
     * @param value
     *     allowed object is
     *     {@link SubjectsType }
     *
     */
    public void setSubjects(SubjectsType value) {
        this.subjects = value;
    }
}
