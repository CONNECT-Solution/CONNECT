package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a set of descriptions for a contact.
 * 
 * @author Les Westberg
 */
public class CMContactDescriptions
{

    private List<String> descriptionList = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public CMContactDescriptions()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        descriptionList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMContactDescriptions oCompare)
    {
        if (oCompare.descriptionList.size() != this.descriptionList.size())
        {
            return false;
        }
        
        int iCnt = this.descriptionList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.descriptionList.get(i).equals(oCompare.descriptionList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Return a list of descriptions for this contact.
     * 
     * @return The list of descriptions for this contact.
     */
    public List<String> getDescription()
    {
        return descriptionList;
    }
    
}