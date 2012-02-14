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
package gov.hhs.fha.nhinc.assemblymanager.dao.persistence;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 * 
 * @author kim
 */
public class PersistentServiceFactory {

    @PersistenceContext(unitName = "docassemblyPU")
    protected EntityManager em;

    @PersistenceUnit(unitName = "docassemblyPU")
    protected EntityManagerFactory emf;

    private String puName = null;
    private static Map instances = new HashMap();

    public static PersistentServiceFactory getInstance(String puName) {
        synchronized (PersistentServiceFactory.class) {
            if (!instances.containsKey(puName)) {
                try {
                    PersistentServiceFactory instance = new PersistentServiceFactory(puName);
                    instance.puName = puName;
                    instances.put(puName, instance);
                    return instance;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }

        return (PersistentServiceFactory) instances.get(puName);
    }

    private PersistentServiceFactory(String puName) throws Exception {
        try {
            this.emf = Persistence.createEntityManagerFactory(puName);
        } catch (Exception e) {
            throw e;
        }
    }

    public EntityManager getEntityManager() {
        if (this.emf == null || !this.emf.isOpen()) {
            this.emf = Persistence.createEntityManagerFactory(this.puName);
        }

        if (this.em == null || !this.em.isOpen()) {
            this.em = this.emf.createEntityManager();
        }
        return this.em;
    }

    public void closeEntityManagerFactory() {
        if (this.emf != null) {
            this.emf.close();
            this.emf = null;
        }
    }

    public void closeEntityManager() {
        if (this.em != null) {
            this.em.close();
            this.em = null;
        }
    }

    public void closeAll() {
        closeEntityManagerFactory();
        closeEntityManager();
    }
}
