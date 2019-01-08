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
package gov.hhs.fha.nhinc.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;

/**
 * The JMX MXBean Interface WebServicesMXBean. This interface is used to expose the management of WebServices
 * implementataions for the purposes of switching between standard and passthru orchestration implementations, as well
 * as providing a custom orchestration implementation.
 *
 * @author msw
 */
public interface WebServicesMXBean {

    /**
     * Configure inbound dependency for std orchestration implementation.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void configureInboundStdImpl() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Configure inbound dependency for passthru orchestration implementation.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void configureInboundPtImpl() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Checks if the inbound dependency is the passthru implementation. Given that custom implementations can be
     * provided via the {@link #configureOutboundImpl(String)} method, a return value of "false" does not necessarily
     * mean that the inbound standard orchestration dependency is active.
     *
     * @return true, if is inbound is in passthru mode
     */
    public boolean isInboundPassthru();

    /**
     * Configure outbound dependency for std orchestration implementation.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void configureOutboundStdImpl()
            throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Configure outbound dependency for passthru orchestration implementation.
     *
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    /**
     * Checks if outbound dependency is the passthru implementation. Given that custom implementations can be provided
     * via the {@link #configureInboundImpl(String)} method, a return value of "false" does not necessarily mean that
     * the outbound standard orchestration dependency is active.
     *
     * @return true, if outbound is in passthru mode
     */
    public boolean isOutboundPassthru();

    public serviceEnum getServiceName();

    /**
     *
     */
    public boolean isInboundStandard();

    /*
     * checks if the Gateway in Standard Mode
     */
    public boolean isOutboundStandard();

}
