/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pl.pgnig.serwis.auction.entity.AuctionItem;
import pl.pgnig.serwis.auction.view.ExcelImportOutcomeTypes;

/**
 *
 * @author a6jmalyszko
 */
public interface AuctionItemService<T> {

    Map<ExcelImportOutcomeTypes, Set<String>> persistExcelInput(T input);

    List<AuctionItem> getAuctionItems(Temporal borderDate);

    List<AuctionItem> getAvailableAuctionItems();

    public AuctionItem getAuctionItem(Long valueOf);
}
