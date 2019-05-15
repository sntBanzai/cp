/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.util.Date;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.dao.OrderStatusDao;
import pl.pgnig.serwis.auction.dao.OrderStatusTypeDao;
import pl.pgnig.serwis.auction.dao.OrdersDao;
import pl.pgnig.serwis.auction.entity.OrderStatus;
import pl.pgnig.serwis.auction.entity.OrderStatusType;
import pl.pgnig.serwis.auction.entity.Orders;

/**
 *
 * @author jerzy.malyszko
 */
@Service("orderStatusService")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OrderStatusService {

    @Autowired
    private OrderStatusTypeDao ostDao;

    @Autowired
    private OrderStatusDao osDao;

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private SessionAuthorizationService sas;

    @Transactional(propagation = Propagation.REQUIRED)
    public void setStatusForOrder(Orders orders, OrderStatusTypeEnum oste, String infoColumn) {
        OrderStatusType orderStatusType = ostDao.getOrderStatusType(oste);
        OrderStatus os = new OrderStatus();
        os.setOrderStatusType(orderStatusType);
        os.setOrders(orders);
        os.setStatusChangeTime(new Date());
        os.setOriginator(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
        os.setInfo(infoColumn);
        osDao.persistStatus(os);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void throwExcWhenNotAllowedToChangeStatus(Long ordersId) throws OrderStatusChangeNotAllowedException {
        Orders order = ordersDao.getOrder(ordersId);
        Boolean isAccount = sas.getIsAccount();
        if (isAccount && order.getOrderStatus().getOrderStatusType().getName().equals(OrderStatusTypeEnum.DELIVERED.getNamePl())) {
            throw new OrderStatusChangeNotAllowedException(OrderStatusTypeEnum.DELIVERED);
        }
        if (!isAccount && order.getOrderStatus().getOrderStatusType().getName().equals(OrderStatusTypeEnum.BOUGHT.getNamePl())) {
            throw new OrderStatusChangeNotAllowedException(OrderStatusTypeEnum.BOUGHT);
        }
    }

    public static class OrderStatusChangeNotAllowedException extends Exception {

        private final OrderStatusTypeEnum currStat;

        public OrderStatusChangeNotAllowedException(OrderStatusTypeEnum currStat) {
            this.currStat = currStat;
        }

        public OrderStatusTypeEnum getCurrStat() {
            return currStat;
        }

    }

}
