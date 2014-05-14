  /* 
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 *
 * @author sadusumilli
 */
public class CreateuserBeanTest {

    private CreateuserBean createuserBean = null;
    Login login = new Login();

    private final HttpSession session = mock(HttpSession.class);

    public CreateuserBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws UserLoginException {

        login.setUserName("username");
        login.setPassword("password");

        LoginService loginservice = new LoginService() {

            @Override
            public boolean login(Login login) throws UserLoginException {
                return true;
            }

            @Override
            public boolean addUser(Login user) throws UserLoginException {
                return true;
            }
        };

        loginservice.addUser(login);
        createuserBean = new CreateuserBean(loginservice) {
            @Override
            protected HttpSession getHttpSession() {

                return session;
            }

        };

    }

    @After
    public void tearDown() {
    }

    @Test
    @SuppressWarnings("empty-statement")
    public void testCreateUser_Pass1() throws UserLoginException {
        assertTrue(createuserBean.createUser());
        verify(session).setAttribute(Mockito.anyString(), Mockito.any(Login.class));

    }

}
