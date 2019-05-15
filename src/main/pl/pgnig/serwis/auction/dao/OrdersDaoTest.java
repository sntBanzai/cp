/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.exception.DaoValidationException;

/**
 *
 * @author bartosz.szymborski
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OrdersDaoTest {

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    @PostConstruct
    public void OrdersBooleanCheckTest() {
        /*TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallback<Orders>() {

            @Override
            public Orders doInTransaction(TransactionStatus ts) {
                System.out.println(ordersDao.getOrders());
                System.out.println("---------------------");
                Orders orders = new Orders();
                orders.setFirstName("Hubers");
                orders.setLastName("Rzygad³o");
                orders.setUserAddress("Konna 15");
                orders.setCheckbox(false);
                orders.setCompanyAddress("Wyœcigowa 20");
                orders.setCompanyName("Konekx");
                orders.setNIP("0123456789");

                try {
                    ordersDao.persistOrder(orders);
                } catch (DaoValidationException ex) {
                    Logger.getLogger(OrdersDaoTest.class.getName()).log(Level.SEVERE, null, ex);
                }

                return ordersDao.getOrder(1L);
            }
        });
        /*
        System.out.println(ordersDao.getOrders());

        tmpl.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus ts) {
                Orders orders = ordersDao.getOrder(1L);
                orders.setFirstName("Dawid");
                try {
                    ordersDao.persistOrder(orders);
                } catch (DaoValidationException ex) {
                    Logger.getLogger(OrdersDaoTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;

            }

        });
        
        System.out.println(ordersDao.getOrders());

        tmpl.execute(new TransactionCallback<Orders>() {

            @Override
            public Orders doInTransaction(TransactionStatus ts) {
                Orders orders = ordersDao.getOrder(1L);
                ordersDao.deleteOrder(1L);
                return ordersDao.getOrder(1L);

            }

        });*/
     //   System.out.println("------------------------------");
      //  System.out.println(ordersDao.getOrders());
    }

}
