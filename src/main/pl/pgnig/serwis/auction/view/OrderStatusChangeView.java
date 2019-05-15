/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import javax.annotation.PostConstruct;
import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.security.UserUtil;
import pl.pgnig.serwis.auction.service.OrderStatusService;
import pl.pgnig.serwis.auction.service.OrderStatusTypeEnum;
import pl.pgnig.serwis.auction.service.OrdersService;
import pl.pgnig.serwis.auction.service.SessionAuthorizationService;

/**
 *
 * @author jerzy.malyszko
 */
@Component
@Scope("view")
public class OrderStatusChangeView {

    public static final String ORDERS_ID = "ordersId";

    private OrderStatusChangeStrategy strategy;

    private Orders orders;
    
    private String information;
    
    @Autowired
    @Qualifier("defaultOrdersServiceImpl")
    protected OrdersService ordersService;
    
    @Autowired
    private OrderStatusService statusService;
    
    @Autowired
    protected SessionAuthorizationService authService;

    @PostConstruct
    private void init() {
        String ordersIdParam = PrimeFacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap().get(ORDERS_ID);
        if (ordersIdParam == null) {
            return;
        }
        this.orders = ordersService.getOrder(Long.valueOf(ordersIdParam));
        boolean isAccount = authService.adminType(UserUtil.getUserId());
        this.strategy = OrderStatusChangeStrategy.get(orders, isAccount);
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
    
    public OrderStatusTypeEnum getNewStatusAvailable() {
        return strategy.getNewStatusAvailable();
    }

    public boolean isInfoFieldRequired(){
        return strategy.isInfoFieldRequired();
    }
    
    public String getInfoFieldLabel(){
        return strategy.getInfoFieldLabel();
    }
    
    public void changeStatus(){
        try{
            statusService.setStatusForOrder(orders, strategy.getNewStatusAvailable(), getInformation());
            PrimeFaces.current().dialog().closeDynamic(Boolean.TRUE);
        } catch (Exception e){
            throw e;
        }
        
        
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

}
