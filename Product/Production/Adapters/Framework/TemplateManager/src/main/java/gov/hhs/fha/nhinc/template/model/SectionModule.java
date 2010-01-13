/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.template.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "section_module")
@NamedQueries({
   @NamedQuery(name = "SectionModule.findAll", query = "SELECT s FROM SectionModule s"),
   @NamedQuery(name = "SectionModule.findBySectionId", query = "SELECT s FROM SectionModule s WHERE s.sectionModulePK.sectionId = :sectionId"),
   @NamedQuery(name = "SectionModule.findByModuleId", query = "SELECT s FROM SectionModule s WHERE s.sectionModulePK.moduleId = :moduleId"),
   @NamedQuery(name = "SectionModule.findByActiveYn", query = "SELECT s FROM SectionModule s WHERE s.activeYn = :activeYn"),
   @NamedQuery(name = "SectionModule.findBySectionIdActiveYn", query = "SELECT s FROM SectionModule s WHERE s.sectionModulePK.sectionId = :sectionId AND s.activeYn = :activeYn")
})
public class SectionModule implements Serializable {
   private static final long serialVersionUID = 1L;
   @EmbeddedId
   protected SectionModulePK sectionModulePK;
   @Basic(optional = false)
   @Column(name = "ACTIVE_YN")
   private char activeYn;
   @JoinColumn(name = "SECTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
   @ManyToOne(optional = false, fetch = FetchType.EAGER)
   private CdaTemplate cdaTemplate;
   @JoinColumn(name = "MODULE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
   @ManyToOne(optional = false, fetch = FetchType.EAGER)
   private CdaTemplate cdaTemplate1;

   public SectionModule() {
   }

   public SectionModule(SectionModulePK sectionModulePK) {
      this.sectionModulePK = sectionModulePK;
   }

   public SectionModule(SectionModulePK sectionModulePK, char activeYn) {
      this.sectionModulePK = sectionModulePK;
      this.activeYn = activeYn;
   }

   public SectionModule(int sectionId, int moduleId) {
      this.sectionModulePK = new SectionModulePK(sectionId, moduleId);
   }

   public SectionModulePK getSectionModulePK() {
      return sectionModulePK;
   }

   public void setSectionModulePK(SectionModulePK sectionModulePK) {
      this.sectionModulePK = sectionModulePK;
   }

   public char getActiveYn() {
      return activeYn;
   }

   public void setActiveYn(char activeYn) {
      this.activeYn = activeYn;
   }

   public CdaTemplate getCdaTemplate() {
      return cdaTemplate;
   }

   public void setCdaTemplate(CdaTemplate cdaTemplate) {
      this.cdaTemplate = cdaTemplate;
   }

   public CdaTemplate getCdaTemplate1() {
      return cdaTemplate1;
   }

   public void setCdaTemplate1(CdaTemplate cdaTemplate1) {
      this.cdaTemplate1 = cdaTemplate1;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (sectionModulePK != null ? sectionModulePK.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof SectionModule)) {
         return false;
      }
      SectionModule other = (SectionModule) object;
      if ((this.sectionModulePK == null && other.sectionModulePK != null) || (this.sectionModulePK != null && !this.sectionModulePK.equals(other.sectionModulePK))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuffer str = new StringBuffer();

      str.append("SectionModule[sectionModulePK=" + sectionModulePK + "]" + System.getProperty("line.separator"));
      str.append("\t\tactiveYn:" + activeYn + System.getProperty("line.separator"));
      str.append("Section template: " + cdaTemplate.toString());
      str.append("Module template: " + cdaTemplate1.toString());
      return str.toString();
   }

}
