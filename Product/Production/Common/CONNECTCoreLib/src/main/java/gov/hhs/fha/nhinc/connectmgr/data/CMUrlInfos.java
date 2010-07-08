/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jhoppesc
 */
public class CMUrlInfos {

    private List<CMUrlInfo> urlInfoList = new ArrayList<CMUrlInfo>();

    /**
     * Default constructor.
     */
    public CMUrlInfos() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        urlInfoList = new ArrayList<CMUrlInfo>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMUrlInfos oCompare) {
        if (oCompare.urlInfoList.size() != this.urlInfoList.size()) {
            return false;
        }

        int iCnt = this.urlInfoList.size();
        for (int i = 0; i < iCnt; i++) {
            if (!this.urlInfoList.get(i).equals(oCompare.urlInfoList.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return the list of url information associated with this service.
     *
     * @return The list of url information associated with this service.
     */
    public List<CMUrlInfo> getUrlInfo() {
        return urlInfoList;
    }
}
