/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.entity.Users;

/**
 *
 * @author a6jmalyszko
 */
@Repository
@Component("userDao")
@Transactional
public class UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isPriviledgedUser(String userId) {
        Session currentSession = sessionFactory.getCurrentSession();
        List list = currentSession.createQuery("FROM PrivilegesUsers WHERE lower(loginUser) LIKE '%" + userId.toLowerCase() + "'").list();
        return !list.isEmpty();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Users getUsers(String userId) {
        userId = userId.toLowerCase();
        Users retVal = getUsersLazy(userId);
        Hibernate.initialize(retVal);
        return retVal;
    }
    
    @Transactional(propagation = Propagation.MANDATORY)
    public Users getUsersLazy(String userId){
        Session currentSession = sessionFactory.getCurrentSession();
        return (Users) currentSession.createQuery("FROM Users WHERE lower(loginUser) LIKE '%"+userId.toLowerCase()+"'").uniqueResult();
    }

}
