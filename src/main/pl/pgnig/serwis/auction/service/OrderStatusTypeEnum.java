/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.util.stream.Stream;
import pl.pgnig.serwis.auction.entity.OrderStatusType;

/**
 *
 * @author jerzy.malyszko
 */
public enum OrderStatusTypeEnum {
    
    BOUGHT("ZAKUPIONO"), PAYED("ZAP£ACONO"), DELIVERED("WYDANO");

    public static OrderStatusTypeEnum translate(OrderStatusType orderStatusType) {
        return Stream.of(values()).filter(val -> val.getNamePl().equals(orderStatusType.getName())).findAny().get();
    }
    
    private final String namePl;
    
    private OrderStatusTypeEnum(String namePl){
        this.namePl = namePl;
    }

    public String getNamePl() {
        return namePl;
    }

    @Override
    public String toString() {
        return getNamePl();
    }
    
    
    
}
