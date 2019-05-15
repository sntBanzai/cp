/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author bartosz.szymborski
 */
@Entity
@Table(name = "orders", schema = "AukcjaSprzetuIT")
public class Orders {

    private static final SimpleDateFormat DATA2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String userAddress;
    private boolean checkbox;
    private String companyName;
    private String companyAddress;
    @Column(length = 10, nullable = false)
    private String NIP;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;
    @Email
    @Column(nullable = false, name = "userEmail")
    private String userEmail;
    @OneToOne
    @JoinColumn(name = "AUCTION_ITEM_ID", unique = true, nullable = false)
    private AuctionItem auctionItem;
    @ManyToOne
    @JoinColumn(name = "users_id_user", nullable = false)
    private Users users;
    @OneToOne
    @JoinColumn(name = "ID_LAST_ORDER_STATUS", unique = true, nullable = true)
    private OrderStatus orderStatus;
    @OneToOne
    @JoinColumn(name = "ID_INVOICE_ORDER_STATUS", unique = true, nullable = true)
    private OrderStatus invoiceOrderStatus;
    
    @Transient
    private Date deliveryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getNIP() {
        return NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public AuctionItem getAuctionItem() {
        return auctionItem;
    }

    public void setAuctionItem(AuctionItem auctionItem) {
        this.auctionItem = auctionItem;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Orders() {
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getInvoiceOrderStatus() {
        return invoiceOrderStatus;
    }

    public void setInvoiceOrderStatus(OrderStatus invoiceOrderStatus) {
        this.invoiceOrderStatus = invoiceOrderStatus;
    }

    public String getDeliveryDate() {
        if (deliveryDate == null) {
            return "";
        }
        return DATA2.format(deliveryDate);
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "Orders{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", userAddress=" + userAddress + ", checkbox=" + checkbox + ", companyName=" + companyName + ", companyAddress=" + companyAddress + ", NIP=" + NIP + ", purchaseDate=" + purchaseDate + ", userEmail=" + userEmail + ", auctionItem=" + auctionItem + ", users=" + users + '}';
    }

    public static final String COMPANY_NAME = "companyName";
    public static final String USER_ADDRESS = "userAddress";
    public static final String CHECKBOX = "checkbox";
    public static final String COMPANY_ADDRESS = "companyAddress";
    public static final String NIP_FIELD = "NIP";
    public static final String ORDER_EMAIL = "orderEmail";
}
