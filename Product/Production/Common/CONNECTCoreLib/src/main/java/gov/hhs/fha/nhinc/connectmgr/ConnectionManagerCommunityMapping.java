/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author jhoppesc
 */
public class ConnectionManagerCommunityMapping {

    AssigningAuthorityHomeCommunityMappingDAO mappingDao;

    /**
     *
     */
    public ConnectionManagerCommunityMapping() {
        mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
    }

    /**
     * @param mappingDao
     */
    public ConnectionManagerCommunityMapping(AssigningAuthorityHomeCommunityMappingDAO mappingDao) {
        this.mappingDao = mappingDao;
    }

    /**
     *
     * @param requestType
     * @return GetHomeCommunityByAssigningAuthorityResponseType
     */
    public HomeCommunityType getHomeCommunityByAssigningAuthority(String assigningAuthId) {
        HomeCommunityType hc = new HomeCommunityType();

        // Verify assigning authority id is valid
        if (NullChecker.isNotNullish(assigningAuthId)) {
            hc.setHomeCommunityId(mappingDao.getHomeCommunityId(assigningAuthId));
        } else {
            hc = null;
        }

        return hc;
    }

}
