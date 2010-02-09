package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the lines in an address.
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:39 PM
 */
public class CMAddresses
{
    public List<CMAddress> addressList = new ArrayList<CMAddress>();

    /**
     * Default constructor.
     */
    public CMAddresses()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        addressList = new ArrayList<CMAddress>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMAddresses oCompare)
    {
        if (oCompare.addressList.size() != this.addressList.size())
        {
            return false;
        }
        
        int iCnt = this.addressList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.addressList.get(i).equals(oCompare.addressList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    

    /**
     * Returns the list of addresses.
     * 
     * @return The list of addresses.
     */
    public List<CMAddress> getAddress()
    {
        return addressList;
    }
    

}