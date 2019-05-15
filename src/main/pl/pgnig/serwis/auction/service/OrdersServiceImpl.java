/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.dao.AuctionItemDao;
import pl.pgnig.serwis.auction.dao.OrdersDao;
import pl.pgnig.serwis.auction.dao.UserDao;
import pl.pgnig.serwis.auction.entity.AuctionItem;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.entity.Users;
import pl.pgnig.serwis.auction.exception.DaoValidationException;
import pl.pgnig.serwis.auction.service.mail.MailSenderInput;
import pl.pgnig.serwis.auction.service.mail.MailSenderService;
import pl.pgnig.serwis.auction.view.EquipType;

/**
 *
 * @author a6jmalyszko
 */
@Service("defaultOrdersServiceImpl")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersDao orderDao;

    @Autowired
    private AuctionItemDao auctionItemDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailSenderService msservice;

    @Autowired
    private ImageResourceApplicationService iras;

    @Autowired
    private OrderStatusService orderStatusService;

    public static final String LOCATION = "\\\\kim\\SprzZdj\\";

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Orders> getMyOrder(String userId) {
        List<Orders> orders = new ArrayList<>(orderDao.getOrders(userId));
        return orders;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Orders getOrder(Long id) {
        Orders orders = orderDao.getOrder(id);
        return orders;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void makeOrder(Long auctionId, String userId, Map<String, Object> orderProps) {
        Orders newOrder = new Orders();
        AuctionItem auctionItem = auctionItemDao.getAuctionItem(auctionId);
        Users users = userDao.getUsers(userId);
        newOrder.setAuctionItem(auctionItem);
        newOrder.setUsers(users);
        newOrder.setUserAddress((String) orderProps.get(Orders.USER_ADDRESS));
        newOrder.setFirstName(users.getNameUser());
        newOrder.setLastName(users.getSurnameUser());
        newOrder.setCheckbox((boolean) orderProps.get(Orders.CHECKBOX));
        newOrder.setPurchaseDate(new Date());
        newOrder.setUserEmail((String) orderProps.get(Orders.ORDER_EMAIL));
        if (newOrder.isCheckbox()) {
            newOrder.setCompanyName((String) orderProps.get(Orders.COMPANY_NAME));
            newOrder.setCompanyAddress((String) orderProps.get(Orders.COMPANY_ADDRESS));
            newOrder.setNIP((String) orderProps.get(Orders.NIP_FIELD));
        }
        try {
            orderDao.persistOrder(newOrder);
            orderStatusService.setStatusForOrder(newOrder, OrderStatusTypeEnum.BOUGHT, null);
        } catch (DaoValidationException ex) {
            Logger.getLogger(OrdersServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Orders> getAdminOrder() {
        List<Orders> orders = new ArrayList<>(orderDao.getAdminOrders());
        return orders;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Orders isAllowedToBuyItem(String userId, Long auctionItemId) {
        int year = LocalDate.now().getYear();
        LocalDate dateStart = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate dateTo = LocalDate.of(year, Month.DECEMBER, 31);
        AuctionItem auctionItem = auctionItemDao.getAuctionItem(auctionItemId);
        List<AuctionItem> itemz = orderDao.getItemsOrderedByUserWithinADateRange(userId, dateStart, dateTo);
        Predicate<AuctionItem> pred = null;
        if (STARTS_WITH_COMPUTER.test(auctionItem)) {
            pred = STARTS_WITH_COMPUTER;
        } else {
            pred = ordItem -> Objects.equals(ordItem.getEquipmentType(), auctionItem.getEquipmentType());
        }
        List<AuctionItem> ais = itemz.stream().filter(pred).collect(Collectors.toList());
        Orders retVal = null;
        if (!ais.isEmpty()) {
            retVal = orderDao.getOrderForAuctionItem(ais.get(0).getId());
            if (retVal != null) {
                Hibernate.initialize(retVal);
                Hibernate.initialize(retVal.getAuctionItem());
                Hibernate.initialize(retVal.getUsers());
            }
        }
        return retVal;
    }

    private static final Predicate<AuctionItem> STARTS_WITH_COMPUTER = auctionItem -> auctionItem.getEquipmentType().toLowerCase().startsWith("komputer");

    @Override
    public List<Orders> getAdminOrderDate(Date date) {
        List<Orders> orderDate = new ArrayList<>(orderDao.getAdminOrdersDate());
        return orderDate;
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notifyBuyerWithEmail(Long id) {
        Orders orders = orderDao.getOrderForAuctionItem(id);
        AuctionItem auctionItem = orders.getAuctionItem();
        Hibernate.initialize(auctionItem);
        Users user = orders.getUsers();
        Hibernate.initialize(user);
        MailSenderInput.Builder builder = new MailSenderInput.Builder();
        builder.setSenderEmailAddress("komisja.likwidacyjna@serwis.pgnig.pl"); //ich mail
        builder.addRecipent(orders.getUserEmail());
        final ResourceBundle bundle = ResourceBundle.getBundle("messages");
        String endWp = "@wp.pl";
        if (orders.getUserEmail().endsWith(endWp)) {
            String tit = bundle.getString("subjectEmailWp");
            builder.setMailSubject(tit + " " + auctionItem.getSerialNumber().toString());
        }else{
             String tit = bundle.getString("subjectEmail");
            builder.setMailSubject(tit + " " + auctionItem.getSerialNumber().toString());
        }
       
        String mailContent = getMailContent(new StringBuilder("/mailContent").append(orders.isCheckbox() ? "_1" : "").append(".template").toString());
        mailContent = mailContent.replace("{0}", toUTF8(auctionItem.getSerialNumber().toUpperCase()));
        mailContent = mailContent.replace("{1}", toUTF8(user.getLoginUser()));
        mailContent = mailContent.replace("{2}", toUTF8(auctionItem.getPrice().toString()));
        mailContent = mailContent.replace("{3}", auctionItem.getParameters());
        mailContent = mailContent.replace("{12}", toUTF8(orders.getUserEmail()));

        StringBuilder linkBuilder = new StringBuilder(LOCATION);
        EquipType typeOf = null;
        if (STARTS_WITH_COMPUTER.test(auctionItem)) {
            typeOf = EquipType.K;
        } else if (auctionItem.getEquipmentType().toLowerCase().startsWith("laptop")) {
            typeOf = EquipType.L;
        } else {
            typeOf = EquipType.M;
        }
        linkBuilder.append("\\").append(typeOf.getPathChunk()).append("\\").append(auctionItem.getSerialNumber());
        mailContent = mailContent.replace("{10}", toUTF8(linkBuilder.toString()));
        if (orders.isCheckbox()) {
            mailContent = mailContent.replace("{5}", orders.getCompanyName());
            mailContent = mailContent.replace("{6}", orders.getCompanyAddress());
            mailContent = mailContent.replace("{7}", orders.getNIP());
        } else {
            mailContent = mailContent.replace("{4}", orders.getUserAddress());
        }
        builder.addAttachment(iras.getFileLogoPGNiG(), ImageResourceApplicationService.LOGOPGNiG_CID);
        builder.setMailContent(mailContent);

        try {
            msservice.sendMail(builder.build());
        } catch (IOException ex) {
            Logger.getLogger(OrdersServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    String toUTF8(String arg) {
        return arg != null ? new String(arg.getBytes(), Charset.forName("UTF-8")) : null;
    }

    private String getMailContent(String arg) {
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream(arg), "UTF-8")) {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
        }
        return sb.toString();
    }

}
