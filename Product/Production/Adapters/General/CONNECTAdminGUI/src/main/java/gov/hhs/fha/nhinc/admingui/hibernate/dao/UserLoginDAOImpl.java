/**
 * 
 */
package gov.hhs.fha.nhinc.admingui.hibernate.dao;

import gov.hhs.fha.nhinc.admingui.model.Login;
import gov.hhs.fha.nhinc.admingui.services.persistence.jpa.entity.UserLogin;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author msw
 * 
 */
@Service
public class UserLoginDAOImpl implements UserLoginDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.admingui.hibernate.dao.UserLoginDAO#login(gov.hhs.fha.nhinc.admingui.model.Login)
     */
    @Override
    public UserLogin login(Login login) {
        Query query = this.sessionFactory.getCurrentSession().createQuery("from UserLogin where userName = :userName");
        query.setParameter("userName", login.getUserName());
        return (UserLogin) query.list().get(0);
    }

}
