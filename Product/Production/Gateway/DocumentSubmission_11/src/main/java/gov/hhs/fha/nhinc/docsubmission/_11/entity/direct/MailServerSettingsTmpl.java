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
package gov.hhs.fha.nhinc.docsubmission._11.entity.direct;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Template for defining direct mail server settings (internal or external).
 */
public abstract class MailServerSettingsTmpl implements MailServerSettings {

    private static Log log = LogFactory.getLog(MailServerSettingsTmpl.class);
    
    private final Properties props;
    
    /**
     * Default constructor uses direct.mailserver.properties
     */
    public MailServerSettingsTmpl() {
        
        // props filename comes from implementations of the template.
        String filename = getPropsFilename();

        Properties props = null;
        try {
            props = PropertyAccessor.getInstance().getProperties(filename);
        } catch (Exception e) {
            log.error("Exception while reading properties file: " + filename + ".properties", e);
        }
        this.props = props;
    }

    /**
     * Constructor.
     * @param props properties used to define this mail server.
     */
    public MailServerSettingsTmpl(Properties props) {
        this.props = props;
    }
    
    /**
     * @return the props file name that is backing this mail server.
     */
    protected abstract String getPropsFilename();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Properties getSmtpProperties() {
        // passthru smtp props for javamail. todo - we could extract only the javamail props. 
        return props;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSender() {
        return props.getProperty("direct.sender");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImapHost() {        
        return props.getProperty("imap.host");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return props.getProperty("imap.username");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return props.getProperty("imap.password");
    }

}
