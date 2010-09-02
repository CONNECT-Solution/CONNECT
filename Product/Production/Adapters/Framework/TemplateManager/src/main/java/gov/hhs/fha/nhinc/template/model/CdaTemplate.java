/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.template.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "cda_template")
@NamedQueries({
   @NamedQuery(name = "CdaTemplate.findAll", query = "SELECT c FROM CdaTemplate c"),
   @NamedQuery(name = "CdaTemplate.findById", query = "SELECT c FROM CdaTemplate c WHERE c.id = :id"),
   @NamedQuery(name = "CdaTemplate.findByType", query = "SELECT c FROM CdaTemplate c WHERE c.type = :type"),
   @NamedQuery(name = "CdaTemplate.findByHitspTemplateId", query = "SELECT c FROM CdaTemplate c WHERE c.hitspTemplateId = :hitspTemplateId"),
   @NamedQuery(name = "CdaTemplate.findByIheTemplateId", query = "SELECT c FROM CdaTemplate c WHERE c.iheTemplateId = :iheTemplateId"),
   @NamedQuery(name = "CdaTemplate.findByLoincCode", query = "SELECT c FROM CdaTemplate c WHERE c.loincCode = :loincCode")
})
public class CdaTemplate implements Serializable {
   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Basic(optional = false)
   @Column(name = "TYPE")
   private char type;
   @Column(name = "DESCR")
   private String descr;
   @Column(name = "HITSP_TEMPLATE_ID")
   private String hitspTemplateId;
   @Column(name = "IHE_TEMPLATE_ID")
   private String iheTemplateId;
   @Column(name = "CDA_TEMPLATE_ID")
   private String cdaTemplateId;
   @Column(name = "HITSP_TEMPLATE_ID_PRE25")
   private String hitspTemplateIdPre25;
   @Column(name = "LOINC_CODE")
   private String loincCode;
   @Column(name = "DATA_STATUS")
   private Character dataStatus;
   @Column(name = "DATA_DATE_RANGE_START")
   private String dataDateRangeStart;
   @Column(name = "DATA_DATE_RANGE_END")
   private String dataDateRangeEnd;
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "cdaTemplate", fetch = FetchType.EAGER)
   private Collection<SectionModule> sectionModuleCollection;
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "cdaTemplate1", fetch = FetchType.EAGER)
   private Collection<SectionModule> sectionModuleCollection1;
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "cdaTemplate", fetch = FetchType.EAGER)
   private Collection<DocSection> docSectionCollection;
   @OneToMany(cascade = CascadeType.ALL, mappedBy = "cdaTemplate1", fetch = FetchType.EAGER)
   private Collection<DocSection> docSectionCollection1;

   public CdaTemplate() {
   }

   public CdaTemplate(Integer id) {
      this.id = id;
   }

   public CdaTemplate(Integer id, char type) {
      this.id = id;
      this.type = type;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public char getType() {
      return type;
   }

   public void setType(char type) {
      this.type = type;
   }

   public String getDescr() {
      return descr;
   }

   public void setDescr(String descr) {
      this.descr = descr;
   }

   public String getHitspTemplateId() {
      return hitspTemplateId;
   }

   public void setHitspTemplateId(String hitspTemplateId) {
      this.hitspTemplateId = hitspTemplateId;
   }

   public String getHitspTemplateIdPre25() {
      return hitspTemplateIdPre25;
   }

   public void setHitspTemplateIdPre25(String hitspTemplateIdPre25) {
      this.hitspTemplateIdPre25 = hitspTemplateIdPre25;
   }

   public String getIheTemplateId() {
      return iheTemplateId;
   }

   public void setIheTemplateId(String iheTemplateId) {
      this.iheTemplateId = iheTemplateId;
   }

   public String getCdaTemplateId() {
      return cdaTemplateId;
   }

   public void setCdaTemplateId(String cdaTemplateId) {
      this.cdaTemplateId = cdaTemplateId;
   }

   public String getLoincCode() {
      return loincCode;
   }

   public void setLoincCode(String loincCode) {
      this.loincCode = loincCode;
   }

   public Character getDataStatus() {
      return dataStatus;
   }

   public void setDataStatus(Character dataStatus) {
      this.dataStatus = dataStatus;
   }

   public String getDataDateRangeStart() {
      return dataDateRangeStart;
   }

   public void setDataDateRangeStart(String dataDateRangeStart) {
      this.dataDateRangeStart = dataDateRangeStart;
   }

   public String getDataDateRangeEnd() {
      return dataDateRangeEnd;
   }

   public void setDataDateRangeEnd(String dataDateRangeEnd) {
      this.dataDateRangeEnd = dataDateRangeEnd;
   }

   public Collection<SectionModule> getSectionModuleCollection() {
      return sectionModuleCollection;
   }

   public void setSectionModuleCollection(Collection<SectionModule> sectionModuleCollection) {
      this.sectionModuleCollection = sectionModuleCollection;
   }

   public Collection<SectionModule> getSectionModuleCollection1() {
      return sectionModuleCollection1;
   }

   public void setSectionModuleCollection1(Collection<SectionModule> sectionModuleCollection1) {
      this.sectionModuleCollection1 = sectionModuleCollection1;
   }

   public Collection<DocSection> getDocSectionCollection() {
      return docSectionCollection;
   }

   public void setDocSectionCollection(Collection<DocSection> docSectionCollection) {
      this.docSectionCollection = docSectionCollection;
   }

   public Collection<DocSection> getDocSectionCollection1() {
      return docSectionCollection1;
   }

   public void setDocSectionCollection1(Collection<DocSection> docSectionCollection1) {
      this.docSectionCollection1 = docSectionCollection1;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (id != null ? id.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof CdaTemplate)) {
         return false;
      }
      CdaTemplate other = (CdaTemplate) object;
      if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuffer str = new StringBuffer();

      str.append("CdaTemplate[id=" + id + "]" + System.getProperty("line.separator"));
      str.append("\t\ttype:" + type + System.getProperty("line.separator"));
      str.append("\t\tdescr:" + descr + System.getProperty("line.separator"));
      str.append("\t\thitspTemplateId:" + hitspTemplateId + System.getProperty("line.separator"));
      str.append("\t\thitspTemplateIdPre25:" + hitspTemplateIdPre25 + System.getProperty("line.separator"));
      str.append("\t\tiheTemplateId:" + iheTemplateId + System.getProperty("line.separator"));
      str.append("\t\tcdaTemplateId:" + cdaTemplateId + System.getProperty("line.separator"));
      str.append("\t\tloincCode:" + loincCode);
      str.append("\t\tdataStatus:" + dataStatus + System.getProperty("line.separator"));
      str.append("\t\tdataDateRangeStart:" + dataDateRangeStart + System.getProperty("line.separator"));
      str.append("\t\tdataDateRangeEnd:" + dataDateRangeEnd + System.getProperty("line.separator"));
      
      return str.toString();
   }

}
