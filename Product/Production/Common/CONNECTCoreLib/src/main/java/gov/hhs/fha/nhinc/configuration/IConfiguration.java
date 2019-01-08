/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.configuration;

/**
 * The Interface IConfiguration.
 *
 * @author msw
 */
public interface IConfiguration {

    /**
     * Gets a property.
     *
     * @param propertyFileName the property file name
     * @param key the property key
     * @return the property value
     */
    public String getProperty(String propertyFileName, String key);

    /**
     * Sets a property value.
     *
     * @param propertyFileName the property file name
     * @param key the property key
     * @param value the property value
     */
    public void setProperty(String propertyFileName, String key, String value);

    /**
     * Persist configuration.
     */
    public void persistConfiguration();

    /**
     * Sets the gateway in passthru orchestration mode.
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void setPassthruMode() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Sets the gateway in standard orchestration mode.
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void setStandardMode() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /*
     * Set the Direction Parameter
     */
    public enum directionEnum {
        Outbound, Inbound
    }

    public enum serviceEnum {
        PatientDiscoveryDeferredRequest, PatientDiscoveryDeferredResponse, PatientDiscovery,
        DocumentSubmissionDeferredRequest, DocumentSubmissionDeferredResponse, DocumentSubmission, QueryForDocuments,
        RetrieveDocuments, AdminDistribution, DocumentDataSubmission
    }

    /*
     * Return Specific Service /Direction is in Passthru Mode
     *
     */
    public boolean isPassthru(serviceEnum serviceName, directionEnum direction);

    /*
     * Return Specific Service /Direction is in Standard Mode
     *
     */

    public boolean isStandard(serviceEnum serviceName, directionEnum direction);

    /*
     * Sets the gateway in Standard Mode for Specific Service in Specific Direction which is required for Testing
     * Purposes
     *
     * @throws ClassNotFoundException
     *
     * @throws IllegalAccessException
     *
     * @throws InstantiationException
     */
    public void setStandardMode(serviceEnum serviceName, directionEnum direction)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /*
     * Sets the gateway in Passthru Mode for Specific Service in Specific Direction which is required for Testing
     * Purposes
     *
     * @throws ClassNotFoundException
     *
     * @throws IllegalAccessException
     *
     * @throws InstantiationException
     */
    public void setPassthruMode(serviceEnum serviceName, directionEnum direction)
        throws InstantiationException, IllegalAccessException, ClassNotFoundException;

}
