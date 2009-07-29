package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class contains a list of email addresses.
 * 
 * @author Les Westberg
 */
public class CMEmails
{

    private List<String> emailList = new ArrayList<String>();

    /**
     * Default Constructor.
     */
    public CMEmails()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        emailList = new ArrayList<String>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMEmails oCompare)
    {
        if (oCompare.emailList.size() != this.emailList.size())
        {
            return false;
        }
        
        int iCnt = this.emailList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.emailList.get(i).equals(oCompare.emailList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    /**
     * Returns the list of email addresses.
     * 
     * @return The list of email addresses.
     */
    public List<String> getEmail()
    {
        return emailList;
    }
    
    
}