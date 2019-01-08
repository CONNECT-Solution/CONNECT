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
package gov.hhs.fha.nhinc.auditrepository.hibernate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author drfernan
 *
 *         Factory class to load the AuditRepositoryCore spring configuration.
 *
 */
public class HibernateUtilFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtilFactory.class);

    private static final String AUDIT_REPO_HIBERNATE = "auditRepoHibernateUtil";

    private static HibernateUtil hibernateUtil;

    /**
     * Private constructor to hide the public one.
     */
    private HibernateUtilFactory() {

    }

    /**
     * Private class that holds that loads the spring-beans.xml into the Classpath application context.
     *
     * @author drfernan
     *
     */
    private static class ClassPathSingleton {

        public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
                new String[] { "classpath:spring-audit-beans.xml" });

        private ClassPathSingleton() {
        }
    }

    /**
     * Method that returns the Audit Repository HibernateUtil.
     *
     * @return
     */
    public static HibernateUtil getAuditRepoHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getAuditRepoHibernateUtil {}", context.getId());
        if (hibernateUtil == null) {
            hibernateUtil = context.getBean(AUDIT_REPO_HIBERNATE,
                    gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtil.class);
        }
        return hibernateUtil;
    }

}
