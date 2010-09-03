/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:50 PM
 */
public class CMBusinessEntity {

    private String businessKey = "";
    private CMDiscoveryURLs discoveryURLs = null;
    private CMBusinessNames names = null;
    private CMBusinessDescriptions descriptions = null;
    private CMContacts contacts = null;
    private String homeCommunityId = "";
    private CMStates states = null;
    private boolean federalHIE = false;
    private String publicKeyURI = "";
    private String publicKey = "";
    private CMBusinessServices businessServices = null;

    /**
     * Default constructor.
     */
    public CMBusinessEntity() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        homeCommunityId = "";
        federalHIE = false;
        publicKeyURI = "";
        publicKey = "";
        businessKey = "";
        discoveryURLs = null;
        names = null;
        descriptions = null;
        contacts = null;
        states = null;
        businessServices = null;
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessEntity oCompare) {
        if (!this.homeCommunityId.equals(oCompare.homeCommunityId)) {
            return false;
        }

        if (this.federalHIE != oCompare.federalHIE) {
            return false;
        }

        if (!this.publicKeyURI.equals(oCompare.publicKeyURI)) {
            return false;
        }

        if (!this.businessKey.equals(oCompare.businessKey)) {
            return false;
        }

        if (!this.discoveryURLs.equals(oCompare.discoveryURLs)) {
            return false;
        }

        if (!this.names.equals(oCompare.names)) {
            return false;
        }

        if (!this.descriptions.equals(oCompare.descriptions)) {
            return false;
        }

        if (!this.contacts.equals(oCompare.contacts)) {
            return false;
        }

        if (!this.states.equals(oCompare.states)) {
            return false;
        }

        if (!this.businessServices.equals(oCompare.businessServices)) {
            return false;
        }

        if (!this.publicKey.equals(oCompare.publicKey)) {
            return false;
        }

        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Return the business key associated with this entity.
     * 
     * @return The business key associated with this entity.
     */
    public String getBusinessKey() {
        return businessKey;
    }

    /**
     * Sets the business key associated with this entity.
     * 
     * @param businessKey The business key associated with this entity.
     */
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    /**
     * Returns the business services assocaited with this business entity.
     * 
     * @return The business services associated with this business entity.
     */
    public CMBusinessServices getBusinessServices() {
        return businessServices;
    }

    /**
     * Sets the business services assocaited with this business entity.
     * 
     * @param businessServices The business services associated with this business entity.
     */
    public void setBusinessServices(CMBusinessServices businessServices) {
        this.businessServices = businessServices;
    }

    /**
     * Return the contacts associated with this business entity.
     * 
     * @return The contacts associated with this business entity.
     */
    public CMContacts getContacts() {
        return contacts;
    }

    /**
     * Sets the contacts associated with this business entity.
     * 
     * @param contacts The contacts associated with this business entity.
     */
    public void setContacts(CMContacts contacts) {
        this.contacts = contacts;
    }

    /**
     * Return the description of this business entity.
     * 
     * @return The description of this business entity.
     */
    public CMBusinessDescriptions getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the description of this business entity.
     * 
     * @return The description of this business entity.
     */
    public void setDescriptions(CMBusinessDescriptions descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * Returns the dicovery URLs for this business entity.
     * 
     * @return The discovery URLs for this business entity.
     */
    public CMDiscoveryURLs getDiscoveryURLs() {
        return discoveryURLs;
    }

    /**
     * Sets the dicovery URLs for this business entity.
     * 
     * @return The discovery URLs for this business entity.
     */
    public void setDiscoveryURLs(CMDiscoveryURLs discoveryURLs) {
        this.discoveryURLs = discoveryURLs;
    }

    /**
     * Returns true if this business entity represents a federal HIE.
     * 
     * @return True if this business entity represents a federal HIE.
     */
    public boolean isFederalHIE() {
        return federalHIE;
    }

    /**
     * Set to true if this business entity represents a federal HIE.
     * 
     * @param federalHIE True if this business entity represents a federal HIE.
     */
    public void setFederalHIE(boolean federalHIE) {
        this.federalHIE = federalHIE;
    }

    /**
     * Returns the home community ID for this business entity.
     * 
     * @return The home community ID for this business entity.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Sets the home community ID for this business entity.
     * 
     * @param homeCommunityId The home community ID for this business entity.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Returns the business names associated with this business entity.
     * 
     * @return The business names associated with this business entity.
     */
    public CMBusinessNames getNames() {
        return names;
    }

    /**
     * Sets the business names associated with this business entity.
     * 
     * @param names The business names associated with this business entity.
     */
    public void setNames(CMBusinessNames names) {
        this.names = names;
    }

    /**
     * Returns the PKI public key for this business entity.
     * 
     * @return The PKI public key for this business entity.
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * sets the PKI public key for this business entity.
     * 
     * @param publicKey The PKI public key for this business entity.
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Returns the URL for the location to retrieve the business entity's PKI public key.
     * 
     * @return The URL for the location to retrieve the business entity's PKI public key.
     */
    public String getPublicKeyURI() {
        return publicKeyURI;
    }

    /**
     * Sets the URL for the location to retrieve the business entity's PKI public key.
     * 
     * @param publicKeyURI The URL for the location to retrieve the business entity's PKI public key.
     */
    public void setPublicKeyURI(String publicKeyURI) {
        this.publicKeyURI = publicKeyURI;
    }

    /**
     * Returns the states associated with this business entity.
     * 
     * @return The states associated with this business entity.
     */
    public CMStates getStates() {
        return states;
    }

    /**
     * Sets the states associated with this business entity.
     * 
     * @param states The states associated with this business entity.
     */
    public void setStates(CMStates states) {
        this.states = states;
    }
}
