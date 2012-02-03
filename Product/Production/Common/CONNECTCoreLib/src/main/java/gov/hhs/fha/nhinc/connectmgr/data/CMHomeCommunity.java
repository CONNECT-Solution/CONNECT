/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
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
