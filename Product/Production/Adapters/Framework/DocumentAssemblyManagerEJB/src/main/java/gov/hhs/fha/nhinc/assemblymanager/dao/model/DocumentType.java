/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.assemblymanager.dao.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "document_type")
@NamedQueries({
   @NamedQuery(name = "DocumentType.findAll", query = "SELECT d FROM DocumentType d"),
   @NamedQuery(name = "DocumentType.findByTypeId", query = "SELECT d FROM DocumentType d WHERE d.typeId = :typeId"),
   @NamedQuery(name = "DocumentType.findByDisplayName", query = "SELECT d FROM DocumentType d WHERE d.displayName = :displayName"),
   @NamedQuery(name = "DocumentType.findByActive", query = "SELECT d FROM DocumentType d WHERE d.active = :active"),
   @NamedQuery(name = "DocumentType.findByVersion", query = "SELECT d FROM DocumentType d WHERE d.version = :version"),
   @NamedQuery(name = "DocumentType.findByComments", query = "SELECT d FROM DocumentType d WHERE d.comments = :comments"),
   @NamedQuery(name = "DocumentType.findByCodeSystemOid", query = "SELECT d FROM DocumentType d WHERE d.codeSystemOid = :codeSystemOid"),
   @NamedQuery(name = "DocumentType.findByCodeSystem", query = "SELECT d FROM DocumentType d WHERE d.codeSystem = :codeSystem")
})
public class DocumentType implements Serializable {
   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @Column(name = "TYPE_ID")
   private String typeId;
   @Basic(optional = false)
   @Column(name = "DISPLAY_NAME")
   private String displayName;
   @Basic(optional = false)
   @Column(name = "ACTIVE")
   private boolean active;
   @Column(name = "VERSION")
   private String version;
   @Column(name = "COMMENTS")
   private String comments;
   @Column(name = "CODE_SYSTEM_OID")
   private String codeSystemOid;
   @Column(name = "CODE_SYSTEM")
   private String codeSystem;

   public DocumentType() {
   }

   public DocumentType(String typeId) {
      this.typeId = typeId;
   }

   public DocumentType(String typeId, String displayName, boolean active) {
      this.typeId = typeId;
      this.displayName = displayName;
      this.active = active;
   }

   public String getTypeId() {
      return typeId;
   }

   public void setTypeId(String typeId) {
      this.typeId = typeId;
   }

   public String getDisplayName() {
      return displayName != null? displayName: "";
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   public boolean getActive() {
      return active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getComments() {
      return comments;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }

   public String getCodeSystemOid() {
      return codeSystemOid != null? codeSystemOid: "";
   }

   public void setCodeSystemOid(String codeSystemOid) {
      this.codeSystemOid = codeSystemOid;
   }

   public String getCodeSystem() {
      return codeSystem;
   }

   public void setCodeSystem(String codeSystem) {
      this.codeSystem = codeSystem;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (typeId != null ? typeId.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof DocumentType)) {
         return false;
      }
      DocumentType other = (DocumentType) object;
      if ((this.typeId == null && other.typeId != null) || (this.typeId != null && !this.typeId.equals(other.typeId))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      StringBuffer str = new StringBuffer();

      str.append("DocumentType[" + System.getProperty("line.separator"));
      str.append("\ttypeId=" + typeId + System.getProperty("line.separator"));
      str.append("\tdisplayName=" + displayName + System.getProperty("line.separator"));
      str.append("\tactive=" + active + System.getProperty("line.separator"));
      str.append("\tcodeSystemOid=" + codeSystemOid + ",codeSystem=" + codeSystem + "]");
      return str.toString();
   }

}
