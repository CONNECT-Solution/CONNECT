/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.data;

//import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Les Westberg
 */
public class CMInternalConnInfoService
{
    private String name;
    private String description;
    private boolean supportsLIFT;
    private CMInternalConnectionInfoLiftProtocols liftProtocols;
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
        supportsLIFT = false;
        liftProtocols = null;
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
        boolean supportLiftMatch = false;
        boolean protocolMatch = false;
        boolean headerMatch = false;
        boolean resultMatch = false;

        if ((this.name.equalsIgnoreCase(oCompare.name)) &&
            (this.description.equalsIgnoreCase(oCompare.description)) &&
            (this.endpointURL.equalsIgnoreCase(oCompare.endpointURL)) &&
            (this.externalService == oCompare.externalService))
        {
            headerMatch = true;
        }

        // Compare the supportsLIFT flag
        if (oCompare.supportsLIFT == this.supportsLIFT) {
            supportLiftMatch = true;
        }
        else {
            supportLiftMatch = false;
        }

        // Compare the protocols
        if (oCompare.liftProtocols == null && this.liftProtocols == null) {
            protocolMatch = true;
        }
        else if (oCompare.liftProtocols == null || this.liftProtocols == null) {
            protocolMatch = false;
        }
        else {
            if (this.liftProtocols.equals(oCompare.liftProtocols)) {
                protocolMatch = true;
            }
        }

        if (protocolMatch == true && supportLiftMatch == true && headerMatch == true) {
            resultMatch = true;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return resultMatch;
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

    /**
     * Return the value of the supportsLIFT flag.
     *
     * @return The value of the supportsLIFT flag.
     */
    public boolean getSupportsLIFTFlag()
    {
        return supportsLIFT;
    }

    /**
     * Sets the supportsLIFT flag .
     *
     * @param flag The value of the supportsLIFT flag.
     */
    public void setSupportsLIFTFlag(boolean flag)
    {
        this.supportsLIFT = flag;
    }

    /**
     * Return the LIFT protocols associated with this home community.
     *
     * @return The LIFT protocols associated with this home community.
     */
    public CMInternalConnectionInfoLiftProtocols getLiftProtocols()
    {
        return liftProtocols;
    }

    /**
     * Sets the LIFT protocols associated with this home community.
     *
     * @param services The LIFT protocols associated with this home community.
     */
    public void setLiftProtocols(CMInternalConnectionInfoLiftProtocols liftProtocols)
    {
        this.liftProtocols = liftProtocols;
    }

}