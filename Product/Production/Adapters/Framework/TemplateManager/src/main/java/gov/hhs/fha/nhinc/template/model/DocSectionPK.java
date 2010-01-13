/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.template.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author kim
 */
@Embeddable
public class DocSectionPK implements Serializable {
   @Basic(optional = false)
   @Column(name = "DOC_ID")
   private int docId;
   @Basic(optional = false)
   @Column(name = "SECTION_ID")
   private int sectionId;

   public DocSectionPK() {
   }

   public DocSectionPK(int docId, int sectionId) {
      this.docId = docId;
      this.sectionId = sectionId;
   }

   public int getDocId() {
      return docId;
   }

   public void setDocId(int docId) {
      this.docId = docId;
   }

   public int getSectionId() {
      return sectionId;
   }

   public void setSectionId(int sectionId) {
      this.sectionId = sectionId;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (int) docId;
      hash += (int) sectionId;
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof DocSectionPK)) {
         return false;
      }
      DocSectionPK other = (DocSectionPK) object;
      if (this.docId != other.docId) {
         return false;
      }
      if (this.sectionId != other.sectionId) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "DocSectionPK[docId=" + docId + ", sectionId=" + sectionId + "]";
   }

}
