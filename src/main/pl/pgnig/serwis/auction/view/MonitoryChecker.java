/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.pgnig.serwis.auction.view;

import java.util.function.Predicate;
import pl.pgnig.serwis.auction.entity.AuctionItem;

/**
 *
 * @author bartosz.szymborski
 */
public class MonitoryChecker {
private static final Predicate<AuctionItem> STARTS_WITH_MONITOR = auctionItem -> auctionItem.getEquipmentType().toLowerCase().startsWith("monitor");
    
    public static boolean isMonitory(AuctionItem auctionItem){
        return !STARTS_WITH_MONITOR.test(auctionItem);
        
    }
}
