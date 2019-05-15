/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.service.OrderStatusTypeEnum;
import pl.pgnig.serwis.auction.service.OrdersService;

/**
 *
 * @author bartosz.szymborski
 */
@Component
@Scope("view")
public class AdminOrderView extends AbstractBrowseView {

    private Date date;
    private String status;

    @Autowired
    protected OrdersService ordersService;

    private List<Orders> userOrderAdmin = new ArrayList<>();
    private List<Orders> filteredOrders = new ArrayList<>();
    private Set<Date> orderDates;
    private Set<String> statusList;

    @PostConstruct
    public void get() {
        if(isAccount() && "bartekk".equals(getWidgetName())){
            return;
        }
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('" + getWidgetName() + "').filter()");
        refresh();
        orderDates = userOrderAdmin.stream()
                .map(ord -> ord.getPurchaseDate())
                .collect(Collectors.toCollection(TreeSet::new));
        statusList = Stream.of(OrderStatusTypeEnum.values()).map(val -> val.getNamePl())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void refresh() {
        if (getDate() != null && getStatus() != null) {
            userOrderAdmin = ordersService.getAdminOrderDate(getDate());
        } else {
            userOrderAdmin = ordersService.getAdminOrder();
        }
    }

    public void getPurchaseDate(Date date) {
        userOrderAdmin = ordersService.getAdminOrderDate(date);

    }

    public List<Orders> getUserOrderAdmin() {
        return userOrderAdmin;
    }

    public Set<Date> getOrderDates() {
        return orderDates;
    }

    public void setOrderDates(Set<Date> orderDates) {
        this.orderDates = orderDates;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Orders> getFilteredOrders() {
        return filteredOrders;
    }

    public void setFilteredOrders(List<Orders> filteredOrders) {
        this.filteredOrders = filteredOrders;
    }

    public OrdersService getOrdersService() {
        return ordersService;
    }

    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    public Set<String> getStatusList() {
        return statusList;
    }

    public String getStatus() {
        return status;
    }

    protected String getWidgetName() {
        return "bartekk";
    }

}
