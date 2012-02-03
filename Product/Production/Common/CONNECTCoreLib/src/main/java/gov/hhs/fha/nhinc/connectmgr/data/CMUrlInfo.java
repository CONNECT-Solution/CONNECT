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
