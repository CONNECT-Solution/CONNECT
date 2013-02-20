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
package gov.hhs.fha.nhinc.docrepository.adapter.service;

import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;

import org.hibernate.SessionFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

/**
 * Test case for the DocumentService class
 * 
 * @author Neil Webb
 */
public class DocumentServiceTest {

    @Test
    public void confirmTestSubjectFindByIdReturnsDocumentWithDocumentId() {
        Mockery mockery = new Mockery();
        final SessionFactory mockSessionFactory = mockery.mock(SessionFactory.class);
        final org.hibernate.classic.Session mockSession = mockery.mock(org.hibernate.classic.Session.class);

        DocumentDao testSubject = new DocumentDao() {

            protected SessionFactory getSessionFactory() {
                return mockSessionFactory;
            }
        };

        mockery.checking(new Expectations() {
            {
                one(mockSessionFactory).openSession();
                will(returnValue(mockSession));
                one(mockSession).get(Document.class, 1L);
                will(returnValue(with(any(Document.class))));
                one(mockSession).close();
                will(returnValue(with(any(Document.class))));
            }
        });
        testSubject.findById(1L);

        mockery.assertIsSatisfied();
    }

    @Test
    public void confirmTestSubjectFindByIdFAILSToReturnDocumentWithDocumentId() {
        Mockery mockery = new Mockery();
        DocumentDao testSubject = new DocumentDao() {

            protected SessionFactory getSessionFactory() {
                return null;
            }
        };

        testSubject.findById(1L);

        mockery.assertIsSatisfied();
    }

    @Test
    public void confirmTestSubjectFindByIdFAILSBecauseSessionIsNULL() {
        Mockery mockery = new Mockery();
        final SessionFactory mockSessionFactory = mockery.mock(SessionFactory.class);

        DocumentDao testSubject = new DocumentDao() {

            protected SessionFactory getSessionFactory() {
                return mockSessionFactory;
            }
        };

        mockery.checking(new Expectations() {
            {
                one(mockSessionFactory).openSession();
                will(returnValue(null));
            }
        });
        testSubject.findById(1L);

        mockery.assertIsSatisfied();
    }
}