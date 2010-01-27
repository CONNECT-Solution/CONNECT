package gov.hhs.fha.nhinc.connectmgr.data;

//import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Les Westberg
 */
public class CMInternalConnInfoService
{
    private String name;
    private String description;
    private String endpointURL;
    private boolean externalService;

    /**
     * Default constructor.
     */
    public CMInternalConnInfoService()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        name = "";
        description = "";
        endpointURL = "";
        externalService = false;
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnInfoService oCompare)
    {
        if ((!this.name.equalsIgnoreCase(oCompare.name)) ||
            (!this.description.equalsIgnoreCase(oCompare.description)) ||
            (!this.endpointURL.equalsIgnoreCase(oCompare.endpointURL)) ||
            (this.externalService != oCompare.externalService))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    
    
    /**
     * This method returns the description of the service.
     * 
     * @return The description of the service.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * This method sets the description of the service.
     * 
     * @param description The description of the service.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * This method returns the URL for the endpoint.
     * 
     * @return The URL for the endpoint.
     */
    public String getEndpointURL()
    {
        return endpointURL;
    }

    /**
     * This method sets the URL for the endpoint.
     * 
     * @param endpointURL The URL for the endpoint.
     */
    public void setEndpointURL(String endpointURL)
    {
        this.endpointURL = endpointURL;
    }

    /**
     * If this is true then this service is an external service meaning
     * that it is one that is exposed to the nhin.
     * 
     * @return Returns true if this service is exposed to the nhin.
     */
    public boolean isExternalService()
    {
        return externalService;
    }

    /**
     * Set this to true if this service will be exposed to the nhin.
     * 
     * @param externalService This is true if this service is exposed to the nhin.
     */
    public void setExternalService(boolean externalService)
    {
        this.externalService = externalService;
    }

    /**
     * Return the name of this servie.
     * 
     * @return The name of this service.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of this service.
     * 
     * @param name The name of this service.
     */
    public void setName(String name)
    {
        this.name = name;
    }

}