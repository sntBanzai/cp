/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;
import pl.pgnig.serwis.auction.dao.AdminDao;
import pl.pgnig.serwis.auction.dao.UserDao;

/**
 *
 * @author a6jmalyszko
 */
@Component
@SessionScope
public class SessionAuthorizationService {

    private Boolean isUser;
    private Boolean isAdmin;
    private Boolean isAccount;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isIsUser(String userId) {
        if (isUser == null) {
            isUser = userDao.isPriviledgedUser(userId);
        }
        return isUser;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean isIsAdmin(String userId) {
        if (isAdmin == null) {
            isAdmin = adminDao.isAdmin(userId);
        }
        return isAdmin;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean adminType(String userId) {
        if (isAccount == null) {
            if (!isAdmin) {
                isAccount = false;
            } else {
                long adminType = adminDao.adminType(userId);
                isAccount = adminType == 2;
            }
        }
        return isAccount;
    }

    public boolean isUser() {
        return isUser;
    }

    public Boolean getIsAccount() {
        return isAccount;
    }

}
