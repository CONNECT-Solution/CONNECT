package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a URL for the business entity.
 * 
 * @author Les Westberg
 */
public class CMDiscoveryURLs
{
    private List<String> discoveryURLList = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public CMDiscoveryURLs()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        discoveryURLList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMDiscoveryURLs oCompare)
    {
        if (oCompare.discoveryURLList.size() != this.discoveryURLList.size())
        {
            return false;
        }
        
        int iCnt = this.discoveryURLList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.discoveryURLList.get(i).equals(oCompare.discoveryURLList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Returns the list of discovery URLs for this business entity.
     * 
     * @return The list of discovery URLs for this business entity.
     */
    public List<String> getDiscoveryURL()
    {
        return discoveryURLList;
    }
    
    
    
}
