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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.ONExplicit;
import org.hl7.v3.POCDMT000040AssignedCustodian;
import org.hl7.v3.POCDMT000040Custodian;
import org.hl7.v3.POCDMT000040CustodianOrganization;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.TELExplicit;

/**
 *
 * @author kim
 */
public class CustodianModule extends DocumentBuilder {

   public CustodianModule() {
   }

   public POCDMT000040Custodian build() throws DocumentBuilderException {
      POCDMT000040Custodian custodian = new POCDMT000040Custodian();

      POCDMT000040AssignedCustodian assignedCustodian = new POCDMT000040AssignedCustodian();
      assignedCustodian.setRepresentedCustodianOrganization(createCustodianOrganization());
      custodian.setAssignedCustodian(assignedCustodian);

      return custodian;
   }

   private POCDMT000040CustodianOrganization createCustodianOrganization() {
      POCDMT000040CustodianOrganization custodianOrg = new POCDMT000040CustodianOrganization();
      POCDMT000040Organization  representedOrganization = getRepresentedOrganization();

      custodianOrg.getId().add(getOrganization());
      ONExplicit oOrgName = objectFactory.createONExplicit();
      oOrgName.getContent().add(orgName);
      custodianOrg.setName(oOrgName);
      //For CHS, set the assigned author addr and telecom to the same values as Represented Organization
     custodianOrg.setAddr((ADExplicit)representedOrganization.getAddr().get(0));
     custodianOrg.setTelecom((TELExplicit)representedOrganization.getTelecom().get(0));

      return custodianOrg;
   }
}
