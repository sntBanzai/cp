/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.entity.TermsAcknowledgement;
import pl.pgnig.serwis.auction.entity.Users;

/**
 *
 * @author a6jmalyszko
 */
@Repository
@Component("termsAcknowledgementDao")
@Transactional
public class TermsAcknowledgementDao {

    static final String WEB_XML_TERMS_ACKNOWLEDGEMENT_PARAM = "pl.pgnig.serwis.auction.TERMS_ACKNOWLEDGEMENT_CHECK_DATE";
    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private Date latestTermsDate;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDao usrDao;

    @PostConstruct
    public void init() {
        try {
            latestTermsDate = FORMAT.parse("21-03-2019");
        } catch (ParseException ex) {
            Logger.getLogger(TermsAcknowledgementDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<TermsAcknowledgement> getAcknowledgement(String userId) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("SELECT ta FROM TermsAcknowledgement ta JOIN ta.users usr "
                + "WHERE lower(usr.loginUser) LIKE :userLogin AND ta.acknowledgementDate >= :termsDate");
        query.setParameter("termsDate", latestTermsDate);
        query.setParameter("userLogin", "%" + userId.toLowerCase());
        return ((List<TermsAcknowledgement>) query.list()).stream().max((ta1, ta2) -> ta1.getAcknowledgementDate().compareTo(ta2.getAcknowledgementDate()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String persistAcknowledgement(String userId) {
        Users users = usrDao.getUsers(userId);
        TermsAcknowledgement newTa = new TermsAcknowledgement();
        newTa.setUsers(users);
        newTa.setAcknowledgementDate(new Date());
        sessionFactory.getCurrentSession().persist(newTa);
        return users.getNameUser() + " " + users.getSurnameUser();
    }

}
