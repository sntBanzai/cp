/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.dao.UserDao;
import pl.pgnig.serwis.auction.entity.Users;

/**
 *
 * @author a6jmalyszko
 */
@Service("defaultUsersServiceImpl")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserDao userDao;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Users getUser(String userId) {
        return userDao.getUsers(userId);
    }

}
