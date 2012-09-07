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
import javax.persistence.Embeddable;

/**
 *
 * @author kim
 */
@Embeddable
public class SectionModulePK implements Serializable {
   @Basic(optional = false)
   @Column(name = "SECTION_ID")
   private int sectionId;
   @Basic(optional = false)
   @Column(name = "MODULE_ID")
   private int moduleId;

   public SectionModulePK() {
   }

   public SectionModulePK(int sectionId, int moduleId) {
      this.sectionId = sectionId;
      this.moduleId = moduleId;
   }

   public int getSectionId() {
      return sectionId;
   }

   public void setSectionId(int sectionId) {
      this.sectionId = sectionId;
   }

   public int getModuleId() {
      return moduleId;
   }

   public void setModuleId(int moduleId) {
      this.moduleId = moduleId;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      hash += (int) sectionId;
      hash += (int) moduleId;
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof SectionModulePK)) {
         return false;
      }
      SectionModulePK other = (SectionModulePK) object;
      if (this.sectionId != other.sectionId) {
         return false;
      }
      if (this.moduleId != other.moduleId) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "SectionModulePK[sectionId=" + sectionId + ", moduleId=" + moduleId + "]";
   }

}
