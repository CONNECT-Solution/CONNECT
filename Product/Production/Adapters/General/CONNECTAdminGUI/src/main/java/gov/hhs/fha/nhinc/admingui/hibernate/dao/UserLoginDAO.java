/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.hibernate.dao;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;

/**
 * @author msw
 * 
 */
public interface UserLoginDAO {
    public UserLogin login(Login login);

}
