/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.PrimeFaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.entity.Orders;
import pl.pgnig.serwis.auction.entity.Users;
import pl.pgnig.serwis.auction.service.UsersService;
import pl.pgnig.serwis.auction.view.validator.EqualityValidator;

/**
 *
 * @author a6jmalyszko
 */
@Component
@Scope("view")
public class OrderItemStageTwoView extends OrderItemStageView {

    public static final String FACESREDIRECTTRUE = "?faces-redirect=true";

    @Autowired
    @Qualifier("defaultUsersServiceImpl")
    private UsersService userService;

    private Users user;
    private String userAddress;
    private boolean checkBox;
    private String companyName;
    private String companyAddress;
    private String NIP;
    private String orderEmail;
    private String retypedEmail;
    private boolean entryCheckBox;

    private String messageToShow;

    @Override
    @PostConstruct
    public void init() {
        super.init();
        String userId = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        user = userService.getUser(userId);
        if (user == null) {
            userId = userId.replace("PGN", "").replace("\\", "").replace("/", "");
            user = userService.getUser(userId);
        }
    }

    public void makeOrder() throws IOException {
        if (!Objects.equals(getOrderEmail(), getRetypedEmail())) {
            String string = ResourceBundle.getBundle("messages").getString(EqualityValidator.ORDER_RETYPED_EMAIL_NOT_EQUAL);
            final FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, string, string);
            FacesContext.getCurrentInstance().addMessage(THE_FORMMAIL_INPUT_ONE, facesMessage);
            FacesContext.getCurrentInstance().addMessage(THE_FORMMAIL_INPUT_TWO, facesMessage);
            return;
        }
        Map<String, Object> props = new HashMap<>();
        props.put(Orders.USER_ADDRESS, getUserAddress());
        props.put(Orders.CHECKBOX, isCheckBox());
        props.put(Orders.COMPANY_NAME, getCompanyName());
        props.put(Orders.COMPANY_ADDRESS, getCompanyAddress());
        props.put(Orders.NIP_FIELD, getNIP());
        props.put(Orders.ORDER_EMAIL, getOrderEmail());
        Result res = null;

        try {
            ordersService.makeOrder(auctionItem.getId(), user.getLoginUser(), props);
        } catch (DataIntegrityViolationException dive) {
            if (dive.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cvo = (ConstraintViolationException) dive.getCause();
                if (cvo.getCause() instanceof SQLIntegrityConstraintViolationException && String.valueOf(cvo.getConstraintName()).contains(".UK_")) {
                    res = Result.ALREADY_BOUGHT;
                    String mess = new StringBuilder("Unsuccesful transaction - bought in meantime: auctionItem - ")
                            .append(auctionItem.getInventoryNumber())
                            .append(", buyer: ")
                            .append(user.getLoginUser()).toString();
                    Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE,
                            mess, dive);
                }
            }
            if (res == null) {
                logUnknownError(dive);
                res = Result.UNKNOWN_ERROR;
            }
        } catch (Exception e) {
            logUnknownError(e);
            res = Result.UNKNOWN_ERROR;
        }
        if (res == null) {
            res = Result.SUCCESS;
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO,
                    new StringBuilder("Succesful transaction: auctionItem - ")
                            .append(auctionItem.getInventoryNumber())
                            .append(", buyer: ")
                            .append(user.getLoginUser()).toString());
            try {
                ordersService.notifyBuyerWithEmail(auctionItem.getId());
            } catch (Exception e) {
                Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Attempt to send confirmation email resulted in error", e);
            }

        }
        setMessageToShow(ResourceBundle.getBundle("messages").getString(res.i18n));
    }
    static final String THE_FORMMAIL_INPUT_TWO = "theForm:mailInputTwo";
    static final String THE_FORMMAIL_INPUT_ONE = "theForm:mailInputOne";

    void logUnknownError(Exception e) {
        Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE,
                new StringBuilder("Unsuccesful transaction - unknown error: auctionItem - ")
                        .append(auctionItem.getInventoryNumber())
                        .append(", buyer: ")
                        .append(user.getLoginUser()).toString(), e);
    }

    public Users getUser() {
        return user;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
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
        return NIP != null ? NIP.replace("-", "").replace(" ", "") : NIP;
    }

    public void setNIP(String NIP) {
        this.NIP = NIP;
    }

    public static enum Result {
        SUCCESS("orders_success"), ALREADY_BOUGHT("orders_alreadyBoughtInMeantime"), UNKNOWN_ERROR("orders_unknownError");

        private final String i18n;

        private Result(String i18n) {
            this.i18n = i18n;
        }
    }

    public String getMessageToShow() {
        return messageToShow;
    }

    public void setMessageToShow(String messageToShow) {
        this.messageToShow = messageToShow;
    }

    public void collapseDialog() {
        PrimeFaces.current().dialog().closeDynamic(Boolean.TRUE);
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }

    public String getRetypedEmail() {
        return retypedEmail;
    }

    public void setRetypedEmail(String retypedEmail) {
        this.retypedEmail = retypedEmail;
    }

    public boolean isEntryCheckBox() {
        return entryCheckBox;
    }

    public void setEntryCheckBox(boolean entryCheckBox) {
        this.entryCheckBox = entryCheckBox;
    }
    
}
