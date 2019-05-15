/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author a6jmalyszko
 */
@Repository
@Component("adminDao")
@Transactional
public class AdminDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isAdmin(String userId) {
        userId = userId.toLowerCase();
        List list = sessionFactory.getCurrentSession().createQuery("FROM Admin WHERE lower(loginAdmin) LIKE '%" + userId + "'").list();
        return !list.isEmpty();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public long adminType(String userId) {
        userId = userId.toLowerCase();
        Long id = (Long) sessionFactory.getCurrentSession().createQuery("SELECT a.adminType.id FROM Admin a WHERE lower(a.loginAdmin) LIKE '%" + userId + "' ").uniqueResult();
        return id;
    }

}
