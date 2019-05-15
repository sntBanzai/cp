/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import pl.pgnig.serwis.auction.entity.Users;


/**
 *
 * @author a6jmalyszko
 */
public interface UsersService {
    
    Users getUser(String userId);
    
}
