package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a single address.
 * 
 * @author Les Westberg
 */
public class CMAddress
{
    private List<String> addressLineList = new ArrayList<String>();
    
    /**
     * Default Constructor.
     */
    public CMAddress()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        addressLineList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMAddress oCompare)
    {
        if (oCompare.addressLineList.size() != this.addressLineList.size())
        {
            return false;
        }
        
        int iCnt = this.addressLineList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.addressLineList.get(i).equals(oCompare.addressLineList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Returns the list of address lines.
     * 
     * @return The list of address lines.
     */
    public List<String> getAddressLine()
    {
        return addressLineList;
    }
    
    
    
}
