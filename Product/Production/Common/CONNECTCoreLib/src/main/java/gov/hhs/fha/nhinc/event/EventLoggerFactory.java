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
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.proxy.ComponentProxyFactory;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for bootstrapping event loggers.
 */
public class EventLoggerFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EventLoggerFactory.class);

    private static final String CONFIG_FILE_NAME = "EventLoggerFactoryConfig.xml";
    private static final String BEAN_NAME = "eventLoggerFactory";

    private final EventManager eventManager;
    private List<EventLogger> loggers;

    /**
     * @return an instance of the event logger factory using the component proxy object factory.
     */
    public static EventLoggerFactory getInstance() {
        return new ComponentProxyFactory(CONFIG_FILE_NAME).getInstance(BEAN_NAME, EventLoggerFactory.class);
    }

    /**
     * Constructor.
     * 
     * @param eventManager Event Manager used to create and register loggers.
     */
    public EventLoggerFactory(final EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Register Loggers.
     */
    public void registerLoggers() {
        LOG.debug("Registering loggers...");
        for (EventLogger logger : loggers) {
            LOG.info("Registering logger: " + logger.getClass().getName());
            eventManager.addObserver(logger);
        }
    }

    /**
     * @return the loggers
     */
    public List<EventLogger> getLoggers() {
        return loggers;
    }

    /**
     * @param loggers the loggers to set
     */
    public void setLoggers(List<EventLogger> loggers) {
        this.loggers = loggers;
    }

}
