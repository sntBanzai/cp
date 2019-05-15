/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.entity.OrderStatus;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.service.OrderStatusTypeEnum;

/**
 *
 * @author jerzy.malyszko
 */
@Repository
@Component("orderStatusDao")
@Transactional
public class OrderStatusDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(propagation = Propagation.MANDATORY)
    public void persistStatus(OrderStatus os) {
        Session currentSession = sessionFactory.getCurrentSession();
        OrderStatus oldStatus = os.getOrders().getOrderStatus();
        currentSession.save(os);
        currentSession.flush();
        currentSession.refresh(os);
        Long ordersId = os.getOrders().getId();
        Long orderStatusId = os.getId();
        currentSession.doWork(new Work() {
            @Override
            public void execute(Connection con) throws SQLException {
                try ( Statement stmt = con.createStatement()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("UPDATE AUKCJASPRZETUIT.ORDERS SET ID_LAST_ORDER_STATUS = ");
                    sb.append(orderStatusId);
                    OrderStatusTypeEnum oste = oldStatus != null
                            ? OrderStatusTypeEnum.translate(os.getOrders().getOrderStatus().getOrderStatusType()) : null;
                    if (OrderStatusTypeEnum.BOUGHT.equals(oste)) {
                        sb.append(", ID_INVOICE_ORDER_STATUS = ").append(orderStatusId);
                    }
                    if(OrderStatusTypeEnum.PAYED.equals(oste)){
                        OrderStatusTypeEnum newOste = OrderStatusTypeEnum.translate(os.getOrderStatusType());
                        if(OrderStatusTypeEnum.BOUGHT.equals(newOste)){
                            sb.append(", ID_INVOICE_ORDER_STATUS = ").append((String) null);
                        }
                    }
                    sb.append(" WHERE ID = ");
                    sb.append(ordersId);
                    stmt.executeUpdate(sb.toString());
                }

            }
        });
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Date payDate(Long orderId, OrderStatusTypeEnum oste) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT max(os.statusChangeTime) FROM OrderStatus os WHERE os.orders.id = ")
                .append(orderId).append("AND os.orderStatusType.name = '").append(oste.getNamePl()).append("'");
        Date getDate = (Date) sessionFactory.getCurrentSession().createQuery(builder.toString()).uniqueResult();
        return getDate;
    }
}
