package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * This class is the root node of the uddiConnectionInfo.xml file.
 * 
 * @author Les Westberg
 */
public class CMUDDIConnectionInfo
{

    private CMBusinessEntities businessEntities = null;

    /**
     * Default Constructor.
     */
    public CMUDDIConnectionInfo()
    {
        clear();
    }    

    /**
     * Clears the contents of this object.
     */
    public void clear()
    {
        businessEntities = null;
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMUDDIConnectionInfo oCompare)
    {
        if (!this.businessEntities.equals(oCompare.businessEntities))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Return the entire set of business entities.
     * 
     * @return The entire set of business entities.
     */
    public CMBusinessEntities getBusinessEntities()
    {
        return businessEntities;
    }

    /**
     * Set the entire set of business entities.
     * 
     * @param businessEntities The entire set of business entities.
     */
    public void setBusinessEntities(CMBusinessEntities businessEntities)
    {
        this.businessEntities = businessEntities;
    }
    
}