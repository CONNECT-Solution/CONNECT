package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * @author Les Westberg
 */
public class CMInternalConnectionInfo 
{
    private String homeCommunityId;
    private String name;
    private String description;
    private CMInternalConnInfoServices services;

    /**
     * Default Constructor.
     */
    public CMInternalConnectionInfo()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        homeCommunityId = "";
        name = "";
        description = "";
        services = null;
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfo oCompare)
    {
        if ((!this.homeCommunityId.equals(oCompare.homeCommunityId)) ||
            (!this.name.equals(oCompare.name)) ||
            (!this.description.equals(oCompare.description)) ||
            (!this.services.equals(oCompare.services)))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Return the description of the connection.
     * 
     * @return The description of the connection.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description of the connection.
     * 
     * @param description  The description of the connection.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Return the home community ID associated with this connection.
     * 
     * @return The home community ID associated with this connection.
     */
    public String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    /**
     * Set the home community ID associated with this connection.
     * 
     * @param homeCommunityId The home community ID associated with this connection.
     */
    public void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Return the name of this home community.
     * 
     * @return The name of this home community.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this home community.
     * 
     * @param name The name of this home community.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Return the services associated with this home community.
     * 
     * @return The services associated with this home community.
     */
    public CMInternalConnInfoServices getServices()
    {
        return services;
    }

    /**
     * Sets the services associated with this home community.
     * 
     * @param services The services associated with this home community.
     */
    public void setServices(CMInternalConnInfoServices services)
    {
        this.services = services;
    }

}