/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.entity.AuctionItem;

/**
 *
 * @author a6jmalyszko
 */
@Component
@Scope("view")
public class OrderItemStageOneView extends OrderItemStageView {

    public void proceedToStageTwo() {
        Map<String, List<String>> params = new HashMap<>();
        params.put(OrderItemStageView.AUCTION_ITEM_ID, Arrays.asList(auctionItem.getId().toString()));
        PrimeFaces.current().dialog().openDynamic("orderItemStageTwoView",
                DialogOptionsBuilder.of("modal")
                        .set("draggable", Boolean.TRUE)
                        .set("resizable", Boolean.TRUE).set("contentHeight", "580px").build(), params);
    }

    public void onDialogReturn(SelectEvent event) {
        PrimeFaces.current().dialog().closeDynamic(event.getObject());
    }
    
    private static final Predicate<AuctionItem> STARTS_WITH_MONITOR = auctionItem -> auctionItem.getEquipmentType().toLowerCase().startsWith("monitor");
    
    public boolean isMonitory(){
        return !STARTS_WITH_MONITOR.test(auctionItem);
        
    }
}
