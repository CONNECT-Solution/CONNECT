/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author JHOPPESC
 */
public class CMInternalConnectionInfoState {

    private String name;

    /**
     * Default constructor.
     */
    public CMInternalConnectionInfoState() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        name = "";
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     *
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfoState oCompare) {
        boolean result = false;

        if (NullChecker.isNullish(oCompare.name) &&
                NullChecker.isNullish(this.name)) {
            result = true;
        }
        else if (NullChecker.isNullish(oCompare.name) ||
                NullChecker.isNullish(this.name)) {
            result = false;
        }
        else {
            if (this.name.equalsIgnoreCase(oCompare.name)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * Return the name of this servie.
     *
     * @return The name of this service.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this service.
     *
     * @param name The name of this service.
     */
    public void setName(String name) {
        this.name = name;
    }
}
