/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

/**
 *
 * @author a6jmalyszko
 */
public enum ExcelImportOutcomeTypes {
    
    SUCCESSFULL("auctionItem_successfulMessagee"), 
    UNSUCCESSFUL_UNKNOWN("auctionItem_unsuccesfulUnknownMessage"), 
    INVENTORY_NUMBER_DUPLICATION("auctionItem_nonUniqueInventoryNumberMessage");

    private final String messKey;

    private ExcelImportOutcomeTypes(String messageKey) {
        this.messKey = messageKey;
    }

    public String getMessKey() {
        return messKey;
    }


}
