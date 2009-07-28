package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a list of states.
 * 
 * @author Les Westberg
 */
public class CMStates
{

    private List<String> stateList = new ArrayList<String>();

    /**
     * Default Constructor.
     */
    public CMStates()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        stateList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMStates oCompare)
    {
        if (oCompare.stateList.size() != this.stateList.size())
        {
            return false;
        }
        
        int iCnt = this.stateList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.stateList.get(i).equals(oCompare.stateList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Returns the list of states.
     * 
     * @return The list of states.
     */
    public List<String> getState()
    {
        return stateList;
    }
    
}