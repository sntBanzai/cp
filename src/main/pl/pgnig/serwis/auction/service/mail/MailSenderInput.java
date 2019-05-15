/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author a6jmalyszko
 */
public class MailSenderInput {

    private static final String RECIPIENT_EMAIL_ADDRESSES = "recipientEmailAddresses";
    private static final String MAIL_CONTENT = "mailContent";
    private static final String SENDER_EMAIL_ADDRESS = "senderEmailAddress";
    private static final String MAIL_SUBJECT = "mailSubject";
    private static final String ATTACHMENTS = "attachments";

    private MailSenderInput(Map<String, Object> propsMap) {
        this.mailSubject = (String) propsMap.get(MAIL_SUBJECT);
        this.senderEmailAddress = (String) propsMap.get(SENDER_EMAIL_ADDRESS);
        this.mailContent = (String) propsMap.get(MAIL_CONTENT);
        this.recipientEmailAddresses = propsMap.get(RECIPIENT_EMAIL_ADDRESSES).toString();
        Optional<Map<String, File>> opt = Optional.ofNullable((Map<String, File>) propsMap.get(ATTACHMENTS));
        this.attachments = opt.orElse(new HashMap<>());

    }

    private final String mailSubject;

    private final String senderEmailAddress;

    private final String mailContent;

    private final String recipientEmailAddresses;

    private Map<String,File> attachments;

    public String getMailSubject() {
        return mailSubject;
    }

    public String getSenderEmailAddress() {
        return senderEmailAddress;
    }

    public String getMailContent() {
        return mailContent;
    }

    public String getRecipientEmailAddresses() {
        return recipientEmailAddresses;
    }

    public Map<String, File> getAttachments() {
        return attachments;
    }

    public static class Builder {

        private final Map<String, Object> propsMap = new HashMap<>();

        public Builder() {
            propsMap.put(RECIPIENT_EMAIL_ADDRESSES, new StringBuilder());
            propsMap.put(ATTACHMENTS, new HashMap<String, File>());
        }

        public void setSenderEmailAddress(String senderEmailAddress) {
            propsMap.put(SENDER_EMAIL_ADDRESS, senderEmailAddress);
        }

        public void setMailContent(String mailContent) {
            propsMap.put(MAIL_CONTENT, mailContent);
        }

        public void addRecipent(String... recipients) {
            if (recipients != null) {
                StringBuilder rcps = (StringBuilder) propsMap.get(RECIPIENT_EMAIL_ADDRESSES);
                for (String rcp : recipients) {
                    rcps.append(" ").append(rcp);
                }
            }
        }

        public void setMailSubject(String mailSubject) {
            propsMap.put(MAIL_SUBJECT, mailSubject);
        }

        public void addAttachment(File attachment, String cid) {
            Map<String, File> rcps = (Map<String, File>) propsMap.get(ATTACHMENTS);
            rcps.put(cid, attachment);
        }

        public MailSenderInput build() {
            return new MailSenderInput(propsMap);
        }

    }
}
