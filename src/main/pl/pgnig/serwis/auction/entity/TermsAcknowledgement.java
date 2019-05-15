/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author a6jmalyszko
 */
@Entity
@Table(name = "TERMS_ACKNOWLEDGEMENT", schema = "AukcjaSprzetuIT")
public class TermsAcknowledgement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date acknowledgementDate;
    
    @ManyToOne
    @JoinColumn(name = "users_id_user", nullable = false)
    private Users users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAcknowledgementDate() {
        return acknowledgementDate;
    }

    public void setAcknowledgementDate(Date acknowledgementDate) {
        this.acknowledgementDate = acknowledgementDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
    
    
    
}
