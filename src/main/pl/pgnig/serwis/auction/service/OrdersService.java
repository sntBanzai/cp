/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import pl.pgnig.serwis.auction.entity.Orders;

/**
 *
 * @author bartosz.szymborski
 */

public interface OrdersService {
    
    List<Orders> getAdminOrder();
    
    List<Orders> getAdminOrderDate(Date date);
    
    List<Orders> getMyOrder(String userId);
    
    public Orders getOrder(Long id);
    
    void makeOrder(Long auctionId, String userId, Map<String,Object> orderProps);
    
    Orders isAllowedToBuyItem(String userId, Long auctionItemId);

    public void notifyBuyerWithEmail(Long id);

}
