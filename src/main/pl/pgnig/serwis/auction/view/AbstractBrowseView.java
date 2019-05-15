/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import org.springframework.beans.factory.annotation.Autowired;
import pl.pgnig.serwis.auction.entity.AuctionItem;
import pl.pgnig.serwis.auction.security.UserUtil;
import pl.pgnig.serwis.auction.service.AuctionItemService;
import pl.pgnig.serwis.auction.service.SessionAuthorizationService;

/**
 *
 * @author a6jmalyszko
 */
public abstract class AbstractBrowseView {

    public static final String LOCATION = "\\\\kim\\SprzZdj\\";
    
    @Autowired
    protected AuctionItemService<?> itemService;
    
    @Autowired
    protected SessionAuthorizationService authService;
    
      public boolean isShowAdminTab() {
        String name = UserUtil.getUserId();
        return authService.isIsAdmin(name);
    }
    
    public boolean isAccount(){
        String name = UserUtil.getUserId();
        return authService.adminType(name);
    }
    
     public String prepareLinkToPhotos(AuctionItem ai) {
        if (ai == null || ai.getEquipmentType() == null) {
            return "";
        }
        EquipType et = EquipType.valueOf(ai.getEquipmentType().toUpperCase().substring(0, 1));
        return LOCATION + et.getPathChunk() + "\\" + ai.getSerialNumber();
    }
}
