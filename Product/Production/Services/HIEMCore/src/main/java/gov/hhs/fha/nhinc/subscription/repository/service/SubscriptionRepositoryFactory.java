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
package gov.hhs.fha.nhinc.subscription.repository.service;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for getting an instance of the subscription repository service
 * 
 * @author Neil Webb
 */
public class SubscriptionRepositoryFactory {
    private static Log log = LogFactory.getLog(SubscriptionRepositoryFactory.class);
    private static final String PROPERTIES_FILE_NAME = "gateway";
    private static final String IMPL_CLASS_NAME_KEY = "subscription.repository.implementation.class";

    /**
     * Create a subscription repository service object. The implementation class for the subscription repository service
     * is defined in the gateway.properties file using the key: "subscription.repository.implementation.class"
     * 
     * @return Subscription repository service instance.
     * @throws gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException
     */
    public SubscriptionRepositoryService getSubscriptionRepositoryService() throws SubscriptionRepositoryException {
        SubscriptionRepositoryService repositoryService = null;
        String errorMessage = "Unknown error creating subscription repository service";

        String implClassName = null;
        try {
            log.debug("Retrieving the subscription repository class name");
            implClassName = PropertyAccessor.getInstance().getProperty(PROPERTIES_FILE_NAME, IMPL_CLASS_NAME_KEY);
            log.debug("Retrieved the subscription repository class name: " + implClassName);
        } catch (Throwable t) {
            errorMessage = "An error occured locating the implementation class "
                    + "for the subscription repository service. Please ensure "
                    + "that the implementation class is defined in the " + "gateway.properties file using the key: "
                    + "\"subscription.repository.implementation.class\". The " + "error message was: " + t.getMessage();
            log.error("Error retrieving the subscription implementaion class name: " + t.getMessage(), t);
        }
        if ((implClassName == null) || ("".equals(implClassName.trim()))) {
            if (errorMessage == null) {
                errorMessage = "Unable to locate the implementation class "
                        + "for the subscription repository service. Please ensure "
                        + "that the implementation class is defined in the "
                        + "gateway.properties file using the key: "
                        + "\"subscription.repository.implementation.class\".";
            }
        } else {
            try {
                log.debug("Instantiating the subscription repository service using the class name: " + implClassName);
                repositoryService = (SubscriptionRepositoryService) Class.forName(implClassName).newInstance();
            } catch (Throwable t) {
                errorMessage = "Unable to instantiate the implementation class "
                        + "for the subscription repository service. The class " + "name provided was: " + implClassName
                        + ". Please ensure that this class exists and is " + "accessable. The exception was: "
                        + t.getMessage();

                log.error("Error instantiating the subscription implementaion class: " + t.getMessage(), t);
            }
        }

        if (repositoryService == null) {
            log.error(errorMessage);
            throw new SubscriptionRepositoryException(errorMessage);
        }

        return repositoryService;
    }
}
