package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is used to contain the set of business entities in the UDDI.
 * 
 * @author Les Westberg
 */
public class CMBusinessEntities
{

    private List<CMBusinessEntity> businessEntityList = new ArrayList<CMBusinessEntity>();

    /**
     * Default constructor.
     */
    public CMBusinessEntities()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        businessEntityList = new ArrayList<CMBusinessEntity>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessEntities oCompare)
    {
        if (oCompare.businessEntityList.size() != this.businessEntityList.size())
        {
            return false;
        }
        
        int iCnt = this.businessEntityList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.businessEntityList.get(i).equals(oCompare.businessEntityList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    /**
     * Returns the contents of the list of business entities..
     * 
     * @return The list of business entities.
     */
    public List<CMBusinessEntity> getBusinessEntity()
    {
        return businessEntityList;
    }
    
}