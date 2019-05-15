/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import pl.pgnig.serwis.auction.dao.TermsAcknowledgementDao;
import pl.pgnig.serwis.auction.entity.TermsAcknowledgement;
import pl.pgnig.serwis.auction.entity.Users;

/**
 *
 * @author a6jmalyszko
 */
@Service
@SessionScope
public class AuctionTermsAcknowledgmentService {

    @Autowired
    private TermsAcknowledgementDao dao;

    public static final String ACKNOWLEDGE_AUCTION_TERMSXHTML = "acknowledgeAuctionTerms.xhtml";
    public static final String FACESREDIRECTTRUE = "?faces-redirect=true";

    private boolean acknowledged;
    private boolean requiresPersistance;
    private String userId;
    private String callingViewId;
    private Date acknowledgementDate;
    private String userPresentableName;

    @PostConstruct
    public void init() {
        userId = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName().replace("PGN", "").replace("\\", "").replace("/", "");
        Optional<TermsAcknowledgement> ta = dao.getAcknowledgement(userId);
        setAcknowledged(ta.isPresent());
        requiresPersistance = !acknowledged;
        if (ta.isPresent()) {
            setAcknowledgementDate(ta.get().getAcknowledgementDate());
            Users usr = ta.get().getUsers();
            setUserPresentableName(usr.getNameUser() + " " + usr.getSurnameUser());
        }

    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public void ensureAcknowledgement(String callingViewId) throws IOException {
        this.callingViewId = callingViewId;
        if (!isAcknowledged()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.redirect(ACKNOWLEDGE_AUCTION_TERMSXHTML);
        }
    }

    public String proceedAfterAcknowledgement() {
        if (isAcknowledged()) {
            if (requiresPersistance) {
                String userPresentable = dao.persistAcknowledgement(userId);
                setUserPresentableName(userPresentable);
                acknowledgementDate = new Date();

            }
            String appdx = FACESREDIRECTTRUE;
            if (callingViewId.contains("?")) {
                appdx = appdx.replace("?", "&");
            }
            return callingViewId + appdx;
        }
        throw new IllegalStateException();
    }

    public String getAcknowledgementDate() {
        return new SimpleDateFormat("dd-MM-yyyy").format(acknowledgementDate);
    }

    public void setAcknowledgementDate(Date acknowledgementDate) {
        this.acknowledgementDate = acknowledgementDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPresentableName() {
        return userPresentableName;
    }

    public void setUserPresentableName(String userPresentableName) {
        this.userPresentableName = userPresentableName;
    }

}
