/**
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
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.proxy.ComponentProxyFactory;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zmelnick
 * 
 */
public class EventFactory {

    private static final String CONFIG_FILE_NAME = "EventFactoryConfig.xml";
    private static final String BEAN_NAME = "eventfactory";

    private static Log log = null;
    private Map<String, String> eventMap;
    
    
    /**
     * Getter method for the factory declared by the spring proxy bean.
     * 
     * @return an instance of the event factory
     */
    public static EventFactory getInstance() {
        return new ComponentProxyFactory(CONFIG_FILE_NAME).getInstance(BEAN_NAME, EventFactory.class);
    }

    /**
     * Creates the event class based on the passed in event type. Will construct the event type with the passed in
     * parameters.
     * 
     * @param eventType
     * @param messageID
     * @param transactionID
     * @param description
     * @return
     */
    public Event createEvent(EventType eventType, String messageID, String transactionID, String description) {
        return createEvent(eventType.toString(), messageID, transactionID, description);
    }

    /**
     * Creates the event class based on the passed in event type. Will construct the event type with the passed in
     * parameters.
     * 
     * @param eventType
     * @param messageID
     * @param transactionID
     * @param description
     * @return
     */
    public Event createEvent(String eventType, String messageID, String transactionID, String description) {
        try {
            String eventClassString = eventMap.get(eventType);
            Class<?> eventClass = Class.forName(eventClassString);

            Event event = (Event) eventClass.getConstructor(String.class, String.class, String.class).newInstance(
                    messageID, transactionID, description);

            return event;
        } catch (Exception e) {
            getLogger().error("Unknown event type received.  Is it registered in " + CONFIG_FILE_NAME + "?", e);
        }

        return null;
    }

    /**
     * Setter method for the event map. Used to configure the spring bean.
     * 
     * @param eventMap
     */
    public void setEventMap(Map<String, String> eventMap) {
        this.eventMap = eventMap;
    }

    protected Log getLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
    }
        return log;
    }

}
