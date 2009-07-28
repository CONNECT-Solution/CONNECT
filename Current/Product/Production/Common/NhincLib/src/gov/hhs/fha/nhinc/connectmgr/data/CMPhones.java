package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is used to contain phone numbers.
 * 
 * @author Les Westberg
 */
public class CMPhones
{

    private List<String> phoneList = new ArrayList<String>();

    /**
     * Default Constructor.
     */
    public CMPhones()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        phoneList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMPhones oCompare)
    {
        if (oCompare.phoneList.size() != this.phoneList.size())
        {
            return false;
        }
        
        int iCnt = this.phoneList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.phoneList.get(i).equals(oCompare.phoneList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Returns the list of phone numbers.
     * 
     * @return The list of phone numbers.
     */
    public List<String> getPhone()
    {
        return phoneList;
    }
    
    
    
}