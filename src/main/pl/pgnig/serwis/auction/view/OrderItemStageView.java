/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import javax.annotation.PostConstruct;
import org.primefaces.context.PrimeFacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import pl.pgnig.serwis.auction.entity.AuctionItem;
import pl.pgnig.serwis.auction.service.AuctionItemService;
import pl.pgnig.serwis.auction.service.OrdersService;

/**
 *
 * @author a6jmalyszko
 */
public abstract class OrderItemStageView {

    public static final String AUCTION_ITEM_ID = "auctionItemId";

    @Autowired
    protected AuctionItemService auctionItemService;

    @Autowired
    @Qualifier("defaultOrdersServiceImpl")
    protected OrdersService ordersService;

    protected AuctionItem auctionItem;

    @PostConstruct
    public void init() {
        String auctionItemIdParam = PrimeFacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap().get(AUCTION_ITEM_ID);
        if (auctionItemIdParam == null) {
            return;
        }
        auctionItem = auctionItemService.getAuctionItem(Long.valueOf(auctionItemIdParam));
    }

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

}
