/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
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
@Table(name = "das_config")
@NamedQueries({
   @NamedQuery(name = "DasConfig.findAll", query = "SELECT d FROM DasConfig d"),
   @NamedQuery(name = "DasConfig.findByAttName", query = "SELECT d FROM DasConfig d WHERE d.attName = :attName"),
   @NamedQuery(name = "DasConfig.findByAttValue", query = "SELECT d FROM DasConfig d WHERE d.attValue = :attValue"),
   @NamedQuery(name = "DasConfig.findByActiveYn", query = "SELECT d FROM DasConfig d WHERE d.activeYn = :activeYn"),
   @NamedQuery(name = "DasConfig.findByAttName_ActiveYn", query = "SELECT d FROM DasConfig d WHERE d.attName = :attName and d.activeYn = :activeYn")
})
public class DasConfig implements Serializable {
   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @Column(name = "ATT_NAME")
   private String attName;
   @Column(name = "ATT_VALUE")
   private String attValue;
   @Basic(optional = false)
   @Column(name = "ACTIVE_YN")
   private char activeYn;

   public DasConfig() {
   }

   public DasConfig(String attName) {
      this.attName = attName;
   }

   public DasConfig(String attName, char activeYn) {
      this.attName = attName;
      this.activeYn = activeYn;
   }

   public String getAttName() {
      return attName;
   }

   public void setAttName(String attName) {
      this.attName = attName;
   }

   public String getAttValue() {
      return attValue;
   }

   public void setAttValue(String attValue) {
      this.attValue = attValue;
   }

   public char getActiveYn() {
      return activeYn;
   }

   public void setActiveYn(char activeYn) {
      this.activeYn = activeYn;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (attName != null ? attName.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof DasConfig)) {
         return false;
      }
      DasConfig other = (DasConfig) object;
      if ((this.attName == null && other.attName != null) || (this.attName != null && !this.attName.equals(other.attName))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "DasConfig[attName=" +
              attName + ",attValue=" + attValue + ",activeYn=" + activeYn + "]";
   }

}
