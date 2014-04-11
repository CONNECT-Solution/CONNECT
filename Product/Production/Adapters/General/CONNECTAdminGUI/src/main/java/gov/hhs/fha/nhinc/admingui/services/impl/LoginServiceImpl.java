/*******************************************************************************
 * Copyright 2013 The California Health and Human Services Agency (CHHS). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"), you may not use this file except in compliance with the License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, content (including but not limited to software, documentation, information, and all other works distributed under the License) is distributed on an "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE CONTENT OR THE USE OR OTHER DEALINGS IN THE CONTENT. IN NO EVENT SHALL CHHS HAVE ANY OBLIGATION TO PROVIDE SUPPORT, UPDATES, MODIFICATIONS, AND/OR UPGRADES FOR CONTENT. See the License for the specific language governing permissions and limitations under the License.
 * This publication/product was made possible by Award Number 90HT0029 from Office of the National Coordinator for Health Information Technology (ONC), U.S. Department of Health and Human Services. Its contents are solely the responsibility of the authors and do not necessarily represent the official views of ONC or the State of California.
 ******************************************************************************/
/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.model.User;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.PasswordService;
import gov.hhs.fha.nhinc.admingui.services.exception.PasswordServiceException;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.LoginPersistenceService;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * The Class DatabaseLoginServiceImpl.
 * 
 * @author msw
 */
@Stateless
public class LoginServiceImpl implements LoginService {

    private static Logger log = Logger.getLogger(LoginServiceImpl.class);

    /** The persistence service. */
    @EJB
    private LoginPersistenceService persistenceService;

    /** The password service. */
    private PasswordService passwordService = new SHA1PasswordService();

    public LoginServiceImpl() {

    }

    @Inject
    public LoginServiceImpl(LoginPersistenceService persistenceService, PasswordService passwordService) {
        this.persistenceService = persistenceService;
        this.passwordService = passwordService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.cahih.services.LoginService#login(org.cahih.model.Login)
     */
    @Override
    public boolean login(Login login) throws UserLoginException {
        boolean loggedIn = false;

        UserLogin user = persistenceService.login(login);
        log.debug("db user name: ".concat(user.getUserName()));
        log.debug("db salt: ".concat(user.getSalt()));
        log.debug("db password: ".concat(user.getSha1()));
        if (user != null && user.getSha1() != null && user.getSalt() != null && login.getPassword() != null) {
            try {
                loggedIn = passwordService.checkPassword(user.getSha1().getBytes(), login.getPassword().getBytes(),
                        user.getSalt().getBytes());
            } catch (PasswordServiceException e) {
                throw new UserLoginException("Error while trying to login.", e);
            }
        }

        return loggedIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.cahih.services.LoginService#addUser(org.cahih.model.User)
     */
    @Override
    public void addUser(User user) throws UserLoginException {
        throw new UserLoginException("Method not implemented.");
    }

}
