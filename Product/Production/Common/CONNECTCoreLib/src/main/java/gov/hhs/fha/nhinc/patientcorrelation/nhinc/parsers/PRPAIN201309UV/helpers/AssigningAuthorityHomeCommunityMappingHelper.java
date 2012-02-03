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
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author rayj
 */
public class AssigningAuthorityHomeCommunityMappingHelper {

    private static Log log = LogFactory.getLog(AssigningAuthorityHomeCommunityMappingDAO.class);

    public static List<String> lookupAssigningAuthorities(String homeCommunityId) {
        log.info("converting homeCommunityId [" + homeCommunityId + "] to assigning authority");
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        //this really should be a list returned, not a single value

        //****************************************************************************************************
        //String assigningAuthorityId = mappingDao.getAssigningAuthority(homeCommunityId);
        //log.info("found assigning authority? [" + assigningAuthorityId + "]");
        //****************************************************************************************************

        List<String> assigningAuthorityIds = new ArrayList<String>();
        //assigningAuthorityIds.add(assigningAuthorityId);
        assigningAuthorityIds = mappingDao.getAssigningAuthoritiesByHomeCommunity(homeCommunityId);
        return assigningAuthorityIds;
    }

    public static List<String> lookupAssigningAuthorities(List<String> homeCommunityIds) {
        List<String> fullListOfAssigningAuthorities = null;
        if (NullChecker.isNotNullish(homeCommunityIds)) {
            log.info("converting homeCommunityIds [count=" + homeCommunityIds.size() + "] to assigning authorities");
            fullListOfAssigningAuthorities = new ArrayList<String>();

            List<String> partialListOfAssigningAuthorities;

            for (String homeCommunity : homeCommunityIds) {
                partialListOfAssigningAuthorities = lookupAssigningAuthorities(homeCommunity);
                combineLists(fullListOfAssigningAuthorities, partialListOfAssigningAuthorities);
            }
            log.info("converted homeCommunityIds [count=" + homeCommunityIds.size() + "] to assigning authorities [count=" + fullListOfAssigningAuthorities.size() + "]");
        }
        return fullListOfAssigningAuthorities;
    }

    private static List<String> combineLists(List<String> a, List<String> b) {
        if (a == null) {
            a = new ArrayList<String>();
        }

        if ((b != null) &&
                (b.size() > 0)) {
            a.addAll(b);
        } else {
            log.debug("combineLists - Assignin authorities not found for the home community");
        }

        return a;
    }
}
