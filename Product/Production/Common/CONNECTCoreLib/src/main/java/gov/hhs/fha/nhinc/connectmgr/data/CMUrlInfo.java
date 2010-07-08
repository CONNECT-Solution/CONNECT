/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author jhoppesc
 */
public class CMUrlInfo {
    private String url;
    private String homeCommunityId;

    /**
     * Default constructor.
     */
    public CMUrlInfo() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        url = "";
        homeCommunityId = "";
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMUrlInfo oCompare) {
        boolean hcidResult = false;
        boolean urlResult = false;
        boolean result = false;

        // Check whether the home community ids are equal
        if (NullChecker.isNullish(oCompare.homeCommunityId) &&
                NullChecker.isNullish(this.homeCommunityId)) {
            hcidResult = true;
        }
        else if (NullChecker.isNullish(oCompare.homeCommunityId) ||
                NullChecker.isNullish(this.homeCommunityId)) {
            hcidResult = false;
        }
        else {
            if (this.homeCommunityId.equalsIgnoreCase(oCompare.homeCommunityId)) {
                hcidResult = true;
            }
        }

        // Check whether the urls are equal
        if (NullChecker.isNullish(oCompare.url) &&
                NullChecker.isNullish(this.url)) {
            urlResult = true;
        }
        else if (NullChecker.isNullish(oCompare.url) ||
                NullChecker.isNullish(this.url)) {
            urlResult = false;
        }
        else {
            if (this.url.equalsIgnoreCase(oCompare.url)) {
                urlResult = true;
            }
        }

        // Check if all members of the class were equal
        if (hcidResult == true && urlResult == true) {
            result = true;
        }

        return result;
    }

    /**
     * Return the url of this servie.
     *
     * @return The url of this service.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the url of this service.
     *
     * @param The url of this service.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Return the homeCommunityId of this servie.
     *
     * @return The homeCommunityId of this service.
     */
    public String getHcid() {
        return homeCommunityId;
    }

    /**
     * Set the homeCommunityId of this service.
     *
     * @param The homeCommunityId of this service.
     */
    public void setHcid(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

}
