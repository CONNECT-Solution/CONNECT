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
@Table(name = "doc_section")
@NamedQueries({
   @NamedQuery(name = "DocSection.findAll", query = "SELECT d FROM DocSection d"),
   @NamedQuery(name = "DocSection.findByDocId", query = "SELECT d FROM DocSection d WHERE d.docSectionPK.docId = :docId"),
   @NamedQuery(name = "DocSection.findBySectionId", query = "SELECT d FROM DocSection d WHERE d.docSectionPK.sectionId = :sectionId"),
   @NamedQuery(name = "DocSection.findByActiveYn", query = "SELECT d FROM DocSection d WHERE d.activeYn = :activeYn"),
   @NamedQuery(name = "DocSection.findByLoincCode", query = "SELECT d FROM DocSection d WHERE d.cdaTemplate.loincCode = :loincCode AND d.activeYn = :activeYn")
})
public class DocSection implements Serializable {
   private static final long serialVersionUID = 1L;
   @EmbeddedId
   protected DocSectionPK docSectionPK;
   @Basic(optional = false)
   @Column(name = "ACTIVE_YN")
   private char activeYn;
   @JoinColumn(name = "DOC_ID", referencedColumnName = "ID", insertable = false, updatable = false)
   @ManyToOne(optional = false, fetch = FetchType.EAGER)
   private CdaTemplate cdaTemplate;
   @JoinColumn(name = "SECTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
   @ManyToOne(optional = false, fetch = FetchType.EAGER)
   private CdaTemplate cdaTemplate1;

   public DocSection() {
   }

   public DocSection(DocSectionPK docSectionPK) {
      this.docSectionPK = docSectionPK;
   }

   public DocSection(DocSectionPK docSectionPK, char activeYn) {
      this.docSectionPK = docSectionPK;
      this.activeYn = activeYn;
   }

   public DocSection(int docId, int sectionId) {
      this.docSectionPK = new DocSectionPK(docId, sectionId);
   }

   public DocSectionPK getDocSectionPK() {
      return docSectionPK;
   }

   public void setDocSectionPK(DocSectionPK docSectionPK) {
      this.docSectionPK = docSectionPK;
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
      hash += (docSectionPK != null ? docSectionPK.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof DocSection)) {
         return false;
      }
      DocSection other = (DocSection) object;
      if ((this.docSectionPK == null && other.docSectionPK != null) || (this.docSectionPK != null && !this.docSectionPK.equals(other.docSectionPK))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuffer str = new StringBuffer();

      str.append("DocSection[docSectionPK=" + docSectionPK + "]" + System.getProperty("line.separator"));
      str.append("\t\tactiveYn:" + activeYn + System.getProperty("line.separator"));
      str.append("Document template: " + cdaTemplate.toString());
      str.append("Section template: " + cdaTemplate1.toString());
      return str.toString();
   }

}
