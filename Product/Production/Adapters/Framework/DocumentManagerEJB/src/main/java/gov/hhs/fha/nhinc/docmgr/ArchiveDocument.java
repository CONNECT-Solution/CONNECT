/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.docmgr;

import gov.hhs.fha.nhinc.common.docmgr.UpdateDocumentSlotRequestType;
import gov.hhs.fha.nhinc.docmgr.msgobject.ArchiveInfo;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class ArchiveDocument {

    private static Log log = LogFactory.getLog(ArchiveDocument.class);

    /**
     * Handle archive.
     *
     * The document is archived by first querying for the metadata, then querying for
     * the document itself.  With this information, we can re-store the document as a replacement
     * of the original (updating the hasBeenAccessed field).  The hasBeenAccessed field is our
     * archiving flag.
     *
     * Updated to use new "updateSlot" method.
     *
     * @param timer
     */
    @Timeout
    public RegistryResponseType setArchiveMetaData(String repositoryId, String documentUniqueId, String homeCommunityId) {
        RegistryResponseType response = null;

        log.debug("Starting archive document.");

        try {
            if ((repositoryId == null) || (documentUniqueId == null) || homeCommunityId == null) {
                throw new Exception("ArchiveDocument.setArchiveMetaData(): Either repositoryId or documentUniqueId or homeCommunityId is missing.");
            }
            //Prepare request for update
            UpdateDocumentSlotRequestType updateRequest = new UpdateDocumentSlotRequestType();
            updateRequest.setRepositoryUniqueId(repositoryId);
            updateRequest.setDocumentUniqueId(documentUniqueId);
            updateRequest.setHomeCommunityId(homeCommunityId);
            updateRequest.setSlotName(DocumentManagerImpl.XDS_ARCHIVE_SLOT);
            updateRequest.getSlotValueList().add(DocumentManagerImpl.XDS_DATE_FORMAT.format(new Date()));

            //Do store with metdata for "accessed" set
            response = new DocumentManagerImpl().documentManagerUpdateDocumentSlot(updateRequest);
        } catch (Exception e) {
            log.error("ArchiveDocument.setArchiveMetaData(): Error performing archive.", e);
        }
        return response;
    }
}
