/*******************************************************************************
 * Copyright 2013 The California Health and Human Services Agency (CHHS). All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"), you may not use this file except in compliance with the License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, content (including but not limited to software, documentation, information, and all other works distributed under the License) is distributed on an "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE CONTENT OR THE USE OR OTHER DEALINGS IN THE CONTENT. IN NO EVENT SHALL CHHS HAVE ANY OBLIGATION TO PROVIDE SUPPORT, UPDATES, MODIFICATIONS, AND/OR UPGRADES FOR CONTENT. See the License for the specific language governing permissions and limitations under the License.
 * This publication/product was made possible by Award Number 90HT0029 from Office of the National Coordinator for Health Information Technology (ONC), U.S. Department of Health and Human Services. Its contents are solely the responsibility of the authors and do not necessarily represent the official views of ONC or the State of California.
 ******************************************************************************/
/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.services.persistence.jpa.impl;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.exception.UserLoginException;
import gov.hhs.fha.nhinc.admingui.services.persistence.LoginPersistenceService;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author msw
 * 
 */
@Stateless
public class LoginPersistenceServiceImpl implements LoginPersistenceService {

    @PersistenceContext(unitName = "login-unit")
    private EntityManager entityManager;

    public LoginPersistenceServiceImpl() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.cahih.services.persistence.LoginPersistenceService#login(org.cahih.model.Login)
     */
    @Override
    public UserLogin login(Login login) throws UserLoginException {

        Query query = entityManager.createNamedQuery("findByUserName");
        query.setParameter("userName", login.getUserName());
        query.setMaxResults(1);

        List<UserLogin> users = query.getResultList();
        if (users.isEmpty() || users.size() != 1) {
            throw new UserLoginException("Unable to find a unique user by username.");
        }

        entityManager.refresh(users.get(0));
        // We just checked that this is not null and only has one entry so I think it's safe to do this.
        return users.get(0);
    }

}
