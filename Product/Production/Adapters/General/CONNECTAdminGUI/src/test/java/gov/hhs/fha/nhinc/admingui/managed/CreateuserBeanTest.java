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
package gov.hhs.fha.nhinc.admingui.managed;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author sadusumilli
 */
public class CreateuserBeanTest {

    private ManageUserBean createuserBean = null;
    Login login = new Login();

    private final HttpSession session = mock(HttpSession.class);

    @Before
    public void setUp() {

        LoginService loginservice = new LoginService() {

            @Override
            public UserLogin login(Login login) {
                return new UserLogin();
            }

            @Override
            public UserLogin addUser(Login user, long role, String firstName, String middleName,
            String lastName, String transRoleDesc) throws UserLoginException {
                return new UserLogin();
            }

            @Override
            public List<UserLogin> getAllUsers() {
                return new ArrayList<>();
            }

            @Override
            public void deleteUser(UserLogin user) throws UserLoginException {
                // do nothing
            }

            @Override
            public Properties getUserRoleList() throws PropertyAccessException {
                return new Properties();
            }
        };

        createuserBean = new ManageUserBean(loginservice) {
            @Override
            protected HttpSession getHttpSession() {

                return session;
            }

        };

        createuserBean.setRole("1");

    }

    @Test
    public void testCreateUser_Pass1() {
        assertTrue(createuserBean.createUser());
    }

}
