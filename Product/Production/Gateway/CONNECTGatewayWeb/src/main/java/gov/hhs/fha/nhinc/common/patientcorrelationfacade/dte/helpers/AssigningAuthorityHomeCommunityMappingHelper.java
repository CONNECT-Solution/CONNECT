/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers;

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
        String assigningAuthorityId = mappingDao.getAssigningAuthority(homeCommunityId);
        log.info("found assigning authority? [" + assigningAuthorityId + "]");

        List<String> assigningAuthorityIds = new ArrayList<String>();
        assigningAuthorityIds.add(assigningAuthorityId);
        return assigningAuthorityIds;
    }

    public static List<String> lookupAssigningAuthorities(List<String> homeCommunityIds) {
        List<String> fullListOfAssigningAuthorities=null;
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
        a.addAll(b);
        return a;
    }
}