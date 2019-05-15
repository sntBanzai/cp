/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.entity.OrderStatusType;
import pl.pgnig.serwis.auction.service.OrderStatusTypeEnum;

/**
 *
 * @author jerzy.malyszko
 */
@Repository
@Component("orderStatusTypeDao")
@Transactional
public class OrderStatusTypeDao {

    @Autowired
    private SessionFactory sessionFactory;

    private List<OrderStatusType> cachedTypes;

    @Transactional(propagation = Propagation.MANDATORY)
    public OrderStatusType getOrderStatusType(OrderStatusTypeEnum eType) {
        if (cachedTypes == null) {
            cachedTypes = sessionFactory.getCurrentSession().createQuery("FROM OrderStatusType").list();
        }
        for (OrderStatusType ost : cachedTypes) {
            if (ost.getName().equals(eType.getNamePl())) {
                sessionFactory.getCurrentSession().merge(ost);
                return ost;
            }
        }
        throw new IllegalStateException();
    }
}
