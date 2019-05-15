/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.ResourceBundle;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.service.OrderStatusTypeEnum;

/**
 *
 * @author jerzy.malyszko
 */
public interface OrderStatusChangeStrategy {

    public OrderStatusTypeEnum getNewStatusAvailable();

    public boolean isInfoFieldRequired();

    public String getInfoFieldLabel();

    public static OrderStatusChangeStrategy get(Orders orders, boolean account) {
        OrderStatusTypeEnum oste = OrderStatusTypeEnum.translate(orders.getOrderStatus().getOrderStatusType());
        switch (oste) {
            case BOUGHT:
                if (!account) {
                    throw new IllegalStateException("Must be an account");
                }
                return ACCOUNT_BOUGHT_STRATEGY;
            case PAYED:
                if (account) {
                    return ACCOUNT_PAYED_STRATEGY;
                } else {
                    return ADMIN_PAYED_STRATEGY;
                }
            case DELIVERED:
                if (account) {
                    throw new IllegalStateException("Must not be an account");
                }
                return ADMIN_DELIVERED_STRATEGY;
        }
        throw new IllegalStateException();
    }

    static final OrderStatusChangeStrategy ACCOUNT_BOUGHT_STRATEGY = new OrderStatusChangeStrategy() {

        @Override
        public OrderStatusTypeEnum getNewStatusAvailable() {
            return OrderStatusTypeEnum.PAYED;
        }

        @Override
        public boolean isInfoFieldRequired() {
            return true;
        }

        @Override
        public String getInfoFieldLabel() {
            return ResourceBundle.getBundle("messages").getString("statusChange_invoiceNumber");
        }

    };

    static final OrderStatusChangeStrategy ACCOUNT_PAYED_STRATEGY = new OrderStatusChangeStrategy() {

        @Override
        public OrderStatusTypeEnum getNewStatusAvailable() {
            return OrderStatusTypeEnum.BOUGHT;
        }

        @Override
        public boolean isInfoFieldRequired() {
            return true;
        }

        @Override
        public String getInfoFieldLabel() {
            return ResourceBundle.getBundle("messages").getString("statusChange_reasonOfChange");
        }

    };

    static final OrderStatusChangeStrategy ADMIN_PAYED_STRATEGY = new OrderStatusChangeStrategy() {

        @Override
        public OrderStatusTypeEnum getNewStatusAvailable() {
            return OrderStatusTypeEnum.DELIVERED;
        }

        @Override
        public boolean isInfoFieldRequired() {
            return false;
        }

        @Override
        public String getInfoFieldLabel() {
            return ResourceBundle.getBundle("messages").getString("statusChange_optionalInfo");
        }

    };

    static final OrderStatusChangeStrategy ADMIN_DELIVERED_STRATEGY = new OrderStatusChangeStrategy() {

        @Override
        public OrderStatusTypeEnum getNewStatusAvailable() {
            return OrderStatusTypeEnum.PAYED;
        }

        @Override
        public boolean isInfoFieldRequired() {
            return true;
        }

        @Override
        public String getInfoFieldLabel() {
             return ResourceBundle.getBundle("messages").getString("statusChange_reasonOfChange");
        }

    };

}
