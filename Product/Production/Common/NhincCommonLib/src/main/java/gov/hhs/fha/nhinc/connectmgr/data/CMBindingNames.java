package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This represents the binding names for the service.
 * 
 * @author Les Westberg
 */
public class CMBindingNames
{
    private List<String> nameList = new ArrayList<String>();

    /**
     * Default constructor.
     */
    public CMBindingNames()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        nameList = new ArrayList<String>();
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBindingNames oCompare)
    {
        if (oCompare.nameList.size() != this.nameList.size())
        {
            return false;
        }
        
        int iCnt = this.nameList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.nameList.get(i).equals(oCompare.nameList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * 
     * @return
     */
    public List<String> getName()
    {
        return nameList;
    }
    
    /**
     * Creates a deep copy of this object.
     *
     * @return A copy of this CMBindingNames object
     */
    public CMBindingNames createCopy() {
        CMBindingNames namesCopy = new CMBindingNames();
        namesCopy.getName().addAll(nameList);
        return namesCopy;
    }
    
}