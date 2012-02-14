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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilder;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import org.hl7.v3.POCDMT000040AssignedAuthor;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040AuthoringDevice;
import org.hl7.v3.SCExplicit;
import org.hl7.v3.TSExplicit;

/**
 * This module includes information about the author or creator of the information contained within the exchange.
 * 
 * <author> <time value="20081216170000+0500"/> <assignedAuthor> <id root="[organization OID]"/>
 * <assignedAuthoringDevice> <softwareName>[name of software system]</softwareName> </assignedAuthoringDevice>*
 * <representedOrganization> <id root="<organization OID>"/> <name>[organization name]</name> </representedOrganization>
 * </assignedEntity> </informant>
 * 
 * @author kim
 */
public class Author extends DocumentBuilder {

    public Author() {
    }

    public POCDMT000040Author build() throws DocumentBuilderException {
        POCDMT000040Author author = new POCDMT000040Author();

        try {
            // Author.time: Signifies the time at which this information was
            // created.
            // Will be the same as ClinicalDocument.effectiveTime and will be
            // set when
            // response is formulated..
            TSExplicit authorTime = new TSExplicit();
            authorTime.setValue("000000000000000-0000");
            // authorTime.setValue(DateUtil.convertToCDATime(getCreatedDTM()));
            author.setTime(authorTime);

            // Author.assignedAuthor: The name of the system that created the
            // information content
            author.setAssignedAuthor(createAssignedAuthor());
            return author;
        } catch (Exception e) {
            throw new DocumentBuilderException(e.getMessage());
        }
    }

    private POCDMT000040AssignedAuthor createAssignedAuthor() {
        POCDMT000040AssignedAuthor assignedAuthor = new POCDMT000040AssignedAuthor();

        assignedAuthor.getId().add(this.getOrganization());
        assignedAuthor.setAssignedAuthoringDevice(createAuthoringDevice());
        assignedAuthor.setRepresentedOrganization(getRepresentedOrganization());

        return assignedAuthor;
    }

    private POCDMT000040AuthoringDevice createAuthoringDevice() {
        POCDMT000040AuthoringDevice device = new POCDMT000040AuthoringDevice();

        SCExplicit softwareName = new SCExplicit();
        softwareName.getContent().add(
                PropertiesDAO.getInstance().getAttributeValue(AssemblyConstants.ORGANIZATION_SYSTEM, true));
        device.setSoftwareName(softwareName);

        return device;
    }
}
