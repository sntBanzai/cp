/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.entity.AuctionItem;

import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.exception.DaoValidationException;
import pl.pgnig.serwis.auction.service.OrderStatusTypeEnum;

/**
 *
 * @author bartosz.szymborski
 */
@Repository
@Component("ordersDao")
@Transactional
public class OrdersDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private OrderStatusDao orderStatusDao;

    @SuppressWarnings("unchecked")
    public Collection<? extends Orders> getAdminOrders() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM Orders o JOIN FETCH o.auctionItem ai ORDER BY o.purchaseDate DESC");
        final List list = query.list();
        initTransientDate(list);
        return list;
    }

    @SuppressWarnings("unchecked")
    public Collection<? extends Orders> getAdminOrdersDate() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("SELECT purchaseDate FROM Orders");
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public Collection<? extends Orders> getOrders(String userId) {
        Session session = sessionFactory.getCurrentSession();
        userId = userId.toLowerCase();
        Query query = session.createQuery("FROM Orders o JOIN FETCH o.auctionItem ai WHERE lower(o.users.loginUser) LIKE '%" + userId + "' ORDER BY o.purchaseDate DESC");
        final List list = query.list();
        initTransientDate(list);
        return list;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void persistOrder(Orders orders) throws DaoValidationException {
        Session session = sessionFactory.getCurrentSession();
        if (orders.getUserAddress() == null || "".equals(orders.getUserAddress())) {
            orders.setUserAddress(" ");
        }

        if (orders.isCheckbox()) {

            if (orders.getCompanyAddress() == null || orders.getCompanyName() == null || orders.getNIP() == null) {

                throw new DaoValidationException("Walidacja nie przeszla, uzupe³nij poprawnie wszystkie dane !");
            }
        } else if (!orders.isCheckbox()) {
            orders.setCompanyAddress(null);
            orders.setCompanyName(null);
            orders.setNIP(null);

            //throw new DaoValidationException("Brak prowadzonej dzia³lnoœci gospodarczej - nie dotyczy");
        }
        session.saveOrUpdate(Orders.class.getSimpleName(), orders); //create and update

    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Orders getOrder(Long idOrder) {
        Session session = sessionFactory.getCurrentSession();
        Orders order = session.load(Orders.class, idOrder);
        Hibernate.initialize(order);
        initTransientDate(order);
        return order;//read
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteOrder(Long id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(Orders.class.getSimpleName(), getOrder(id));//delete
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<AuctionItem> getItemsOrderedByUserWithinADateRange(String userId, LocalDate dateStart, LocalDate dateTo) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("SELECT ai FROM Orders o JOIN o.auctionItem ai JOIN o.users usr "
                + "WHERE lower(usr.loginUser) LIKE :userId AND o.purchaseDate BETWEEN :dateStart AND :dateEnd");
        query.setParameter("userId", "%" + userId.toLowerCase());
        query.setParameter("dateStart", Date.valueOf(dateStart));
        query.setParameter("dateEnd", Date.valueOf(dateTo));
        return query.list();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Orders getOrderForAuctionItem(Long auctionItemId) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("SELECT o FROM Orders o JOIN o.auctionItem ai WHERE ai.id = :auctionItemId");
        query.setParameter("auctionItemId", auctionItemId);
        final Orders order = (Orders) query.uniqueResult();
        initTransientDate(order);
        return order;
    }

    private void initTransientDate(Orders order) {
        java.util.Date deliveryDate = orderStatusDao.payDate(order.getId(), OrderStatusTypeEnum.DELIVERED);
        order.setDeliveryDate(deliveryDate);

    }

    private void initTransientDate(List<Orders> orders) {
        orders.stream()
                .forEach(ord -> initTransientDate(ord));
    }


}
