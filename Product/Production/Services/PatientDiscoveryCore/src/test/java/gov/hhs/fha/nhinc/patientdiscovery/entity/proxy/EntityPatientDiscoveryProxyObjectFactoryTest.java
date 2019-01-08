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
package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoveryProxyObjectFactoryTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final EntityPatientDiscoveryProxy mockProxy = context.mock(EntityPatientDiscoveryProxy.class);
    final ApplicationContext appContext = new FileSystemXmlApplicationContext() {
        @Override
        public Object getBean(String beanName) {
            return mockProxy;
        }
    };

    @Test
    public void testGetConfigFileName() {
        try {
            final ApplicationContext mockContext = context.mock(ApplicationContext.class);
            EntityPatientDiscoveryProxyObjectFactory proxyFactory = new EntityPatientDiscoveryProxyObjectFactory() {
                @Override
                protected String getConfigFileName() {
                    return "TEST_CONFIG_FILE_NAME";
                }

                @Override
                protected ApplicationContext getContext() {
                    return mockContext;
                }
            };
            assertEquals("Config file name", "TEST_CONFIG_FILE_NAME", proxyFactory.getConfigFileName());
        } catch (Throwable t) {
            System.out.println("Error running testGetConfigFileName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetConfigFileName test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEntityPatientDiscoveryProxyHappy() {
        EntityPatientDiscoveryProxyObjectFactory proxyFactory = new EntityPatientDiscoveryProxyObjectFactory() {

            @Override
            protected <T extends Object> T getBean(String beanName, Class<T> type) {
                return type.cast(mockProxy);
            }
        };

        try {
            EntityPatientDiscoveryProxy proxy = proxyFactory.getEntityPatientDiscoveryProxy();
            assertNotNull("EntityPatientDiscoveryProxy was null", proxy);
        } catch (Throwable t) {
            System.out.println("Error running testGetEntityPatientDiscoveryProxyHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEntityPatientDiscoveryProxyHappy test: " + t.getMessage());
        }
    }

}
