/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.hibernate;

import gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO;
import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.LoginService;
import gov.hhs.fha.nhinc.admingui.services.PasswordService;
import gov.hhs.fha.nhinc.admingui.services.exception.PasswordServiceException;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.impl.SHA1PasswordService;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;
import java.io.IOException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author msw
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
    
    private static Logger log = Logger.getLogger(LoginServiceImpl.class);
    
    @Autowired
    private UserLoginDAO userLoginDAO;
    
    /** The password service. */
    private PasswordService passwordService = new SHA1PasswordService();

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.admingui.services.LoginService#login(gov.hhs.fha.nhinc.admingui.model.Login)
     */
    @Override
    @Transactional
    public boolean login(Login login) throws UserLoginException {
        boolean loggedIn = false;

        UserLogin user = userLoginDAO.login(login);
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

     /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.admingui.services.LoginService#addUser(gov.hhs.fha.nhinc.admingui.model.User)
     */
    @Override
    public boolean addUser(Login user) throws UserLoginException {
        System.out.println("Inside loginserviceimpl of adduser()");
        boolean isCreateUser = false;
        String passwordHash = null;
        String saltValue = null;
        System.out.println("before the dao call"+isCreateUser);
        try{           
   
            saltValue = passwordService.generateRandomSalt();   
            System.out.println("salt value from the loginserviceimpl" + saltValue);
            passwordHash = new String(passwordService.calculateHash(saltValue.getBytes(), user.getPassword().getBytes()));
            System.out.println("After generating password hash from impl class-------"); 
        }catch(PasswordServiceException e){
            throw new UserLoginException("Error while calculating hash.", e);            
        }
        catch (IOException ex) {
                java.util.logging.Logger.getLogger(LoginServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        System.out.println("generated passwordhash from loginserviceimpl...." + passwordHash);
        UserLogin userLoginEntity = new UserLogin();
        System.out.println("user name for userlogin entity---"+user.getUserName());
        userLoginEntity.setUserName(user.getUserName());
        System.out.println("saltvalue for userlogin entity---"+saltValue);
        userLoginEntity.setSha1(passwordHash);
        userLoginEntity.setSalt(saltValue);
        isCreateUser = userLoginDAO.createUser(userLoginEntity);
        System.out.println("after the dao call"+isCreateUser);
        
        return isCreateUser;
    
    }


}