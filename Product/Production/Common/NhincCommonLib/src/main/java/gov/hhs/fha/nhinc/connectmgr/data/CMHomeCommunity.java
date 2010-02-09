package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * This class is used to contain the basic information about a home community.
 * 
 * @author Les Westberg
 */
public class CMHomeCommunity
{
    private String homeCommunityId = "";
    private String name = "";
    private String description = "";

    /**
     * Default constructor.
     */
    public CMHomeCommunity()
    {
        clear();
    }
    
    /**
     * Set this object to its default state.
     */
    public void clear()
    {
        homeCommunityId = "";
        name = "";
        description = "";
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMHomeCommunity oCompare)
    {
        if (!this.homeCommunityId.equals(oCompare.homeCommunityId))
        {
            return false;
        }
        
        if (!this.name.equals(oCompare.name))
        {
            return false;
        }

        if (!this.description.equals(oCompare.description))
        {
            return false;
        }
        
        return true;
    }

    /**
     * Returns the description of the home community.
     * 
     * @return The description of the home community.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description of the home community.
     * 
     * @param description The description of the home community.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Returns the home community Id for this home community.
     * 
     * @return The home community Id for this home community.
     */
    public String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    /**
     * Sets the home community Id for this home community.
     * 
     * @param homeCommunityId The home community Id for this home community.
     */
    public void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Returns the name of this home community.
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
    
}
