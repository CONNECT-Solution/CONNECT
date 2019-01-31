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
package gov.hhs.fha.nhinc.admingui.hibernate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.PasswordService;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import gov.hhs.fha.nhinc.util.SHA2PasswordUtil;
import gov.hhs.fha.nhinc.util.UtilException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author sadusumilli, msw
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

    /**
     * The login service.
     */
    @InjectMocks
    private LoginServiceImpl loginService;


    @Mock
    private UserLoginDAO userDAO;

    @Mock
    private PasswordService passwordService;

    private String salt = "SALT";

    @Before
    public void setup() {

        Mockito.when(passwordService.generateRandomSalt()).thenReturn("SALT".getBytes());
    }

    @Test
    public void testAdduser_Pass() throws UserLoginException, UtilException {
        Login login = new Login();
        login.setUserName("ABCD");
        login.setPassword("ABCDEFGH");
        Mockito.when(userDAO.createUser(Mockito.any(UserLogin.class))).thenReturn(true);
        UserLogin addedUser = loginService.addUser(login, 1, "firstName", "middleName", "lastName", "role description");
        assertNotNull(addedUser);

        Mockito.verify(passwordService).generateRandomSalt();

        String expectedHash = new String(SHA2PasswordUtil.calculateHash(salt.getBytes(), login.getPassword().getBytes()));
        assertEquals(expectedHash, addedUser.getSha2());

        assertEquals("firstName", addedUser.getFirstName());
        assertEquals("middleName", addedUser.getMiddleName());
        assertEquals("lastName", addedUser.getLastName());
        assertEquals(salt, addedUser.getSalt());

    }


    @Test
    public void testLogin(){
        Login login = new Login();
        login.setUserName("ABCD");
        login.setPassword("ABCDEFGH");

        UserLogin returnedUser = new UserLogin();
        returnedUser.setSalt(salt);
        returnedUser.setSha2("osoSSNvkHcYvZs0+Y54qW9oGVOVz6WB9UVn15NRAq0x4doJ3pjvlT9hlu9bFFxy71YELxql5O+w6+0UctMRP6Q==");

        Mockito.when(userDAO.login(Mockito.isA(Login.class))).thenReturn(returnedUser);
        UserLogin loggedInUser = loginService.login(login);
        assertEquals(returnedUser, loggedInUser);
    }

    @Test
    public void testLogin_badPassword(){
        Login login = new Login();
        login.setUserName("ABCD");
        login.setPassword("ABCDEFGH");

        UserLogin returnedUser = new UserLogin();
        returnedUser.setSalt(salt);
        returnedUser.setSha2("ThIsIsThEwRoNgHaSh");

        Mockito.when(userDAO.login(Mockito.isA(Login.class))).thenReturn(returnedUser);
        UserLogin loggedInUser = loginService.login(login);
        assertEquals(null, loggedInUser);
    }

    @Test
    public void testLogin_noPassword(){
        Login login = new Login();
        login.setUserName("ABCD");
        login.setPassword("");

        UserLogin returnedUser = new UserLogin();
        returnedUser.setSalt(salt);
        returnedUser.setSha2("");

        Mockito.when(userDAO.login(Mockito.isA(Login.class))).thenReturn(returnedUser);
        UserLogin loggedInUser = loginService.login(login);
        assertEquals(null, loggedInUser);
    }

    @Test
    public void testLogin_noDBPassword(){
        Login login = new Login();
        login.setUserName("ABCD");
        login.setPassword("1234");

        UserLogin returnedUser = new UserLogin();
        returnedUser.setSalt(salt);
        returnedUser.setSha2("");

        Mockito.when(userDAO.login(Mockito.isA(Login.class))).thenReturn(returnedUser);
        UserLogin loggedInUser = loginService.login(login);
        assertEquals(null, loggedInUser);
    }
}
