/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import pl.pgnig.serwis.auction.service.mail.MailSenderService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.service.mail.MailSenderInput;

/**
 *
 * @author jerzy.malyszko
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MailSenderServiceTest {

    @Autowired
    public MailSenderService msservice;

    //@PostConstruct
    public void init() {
        MailSenderInput.Builder builder = new MailSenderInput.Builder();
        builder.setSenderEmailAddress("test.aplikacje@serwis.pgnig.pl");
        builder.addRecipent("jerzy.malyszko@serwis.pgnig.pl");
        builder.setMailSubject("Mail numer ");
        builder.setMailContent("Mail testowy - nie realizowaæ");
       // msservice.sendMail(builder.build());

    }

}
