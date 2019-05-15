/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 *
 * @author a6jmalyszko
 */
@Component
@RequestScope
public class PriviledgedSecurityFilter extends AbstractFilter {

    @Override
    protected void actualFiltering(String userName) throws FilteringFailedException {
        if (!authService.isIsAdmin(userName)) {
            throw new FilteringFailedException();
        }
    }

}
