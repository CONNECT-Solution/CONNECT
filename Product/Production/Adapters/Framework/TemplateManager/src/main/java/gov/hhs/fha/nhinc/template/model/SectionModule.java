/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
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
