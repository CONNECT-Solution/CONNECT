/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
 *
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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040AssignedEntity;
import org.hl7.v3.POCDMT000040Informant12;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.POCDMT000040Person;
import org.hl7.v3.TELExplicit;

/**
 * This module This includes information about the author or creator of the information contained within the exchange.
 * 
 * <informant> <assignedEntity> <id root="[organization OID]"/> <representedOrganization> <id
 * root="<organization OID>"/> <name>[organization name]</name> </representedOrganization> </assignedEntity>
 * </informant>
 * 
 * @author kim
 */
public class InformationSource extends DocumentBuilder {

    public InformationSource() {
        super();
    }

    public POCDMT000040Informant12 build() throws DocumentBuilderException {
        POCDMT000040Informant12 informant = new POCDMT000040Informant12();
        POCDMT000040AssignedEntity assignedEntity = new POCDMT000040AssignedEntity();
        POCDMT000040Organization representedOrganization = getRepresentedOrganization();

        try {
            assignedEntity.getId().add(getOrganization());
            //For CHS, set the assigned author addr and telecom to the same values as Represented Organization
            assignedEntity.getAddr().add((ADExplicit) representedOrganization.getAddr().get(0));
            assignedEntity.getTelecom().add((TELExplicit) representedOrganization.getTelecom().get(0));
            //assignedPerson
            POCDMT000040Person assignedPerson = objectFactory.createPOCDMT000040Person();
            PNExplicit assignedPersonName = objectFactory.createPNExplicit();
            assignedPersonName.getNullFlavor().add("UNK");
            assignedPerson.getName().add(assignedPersonName);
            assignedEntity.setAssignedPerson(assignedPerson);
            assignedEntity.setRepresentedOrganization(getRepresentedOrganization());
            informant.setAssignedEntity(assignedEntity);
            return informant;
        } catch (Exception e) {
            throw new DocumentBuilderException(e.getMessage());
        }
    }
}
