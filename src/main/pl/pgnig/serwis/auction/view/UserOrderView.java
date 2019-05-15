/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.service.OrdersService;

/**
 *
 * @author bartosz.szymborski
 */
@Component
@RequestScope
public class UserOrderView extends AbstractBrowseView{

    @Autowired
    protected OrdersService ordersService;

    private List<Orders> userOrder = new ArrayList<>();

    @PostConstruct
    public void get() {
        String userId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
        userId = userId.replace("PGN", "").replace("/", "").replace("\\", "");
        userOrder = ordersService.getMyOrder(userId);

    }

    public List<Orders> getUserOrder() {
        return userOrder;
    }
    
    

}
