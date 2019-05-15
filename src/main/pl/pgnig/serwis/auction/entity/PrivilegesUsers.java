/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author a6jmalyszko
 */
@Entity
@Table(name = "PRIVILEGES_USERS", schema = "AukcjaSprzetuIT")
public class PrivilegesUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ID_USER")
    private String idUser;
    @Size(max = 50)
    @Column(name = "NAME_USER")
    private String nameUser;
    @Size(max = 80)
    @Column(name = "SURNAME_USER")
    private String surnameUser;
    @Size(max = 120)
    @Column(name = "POSITION_USER")
    private String positionUser;
    @Size(max = 20)
    @Column(name = "MPK_USER")
    private String mpkUser;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "LOGIN_USER")
    private String loginUser;
    @Size(max = 100)
    @Column(name = "EMAIL_USER")
    private String emailUser;
    @Size(max = 50)
    @Column(name = "JEDN_ORG_ID")
    private String jednOrgId;
    @Size(max = 150)
    @Column(name = "JEDN_ORG_NAME")
    private String jednOrgName;
    @Size(max = 20)
    @Column(name = "MANAGER_ID")
    private String managerId;
    @Size(max = 50)
    @Column(name = "LOGIN_MANAGER")
    private String loginManager;
    @Size(max = 50)
    @Column(name = "NAME_MANAGER")
    private String nameManager;
    @Size(max = 80)
    @Column(name = "SURNAME_MANAGER")
    private String surnameManager;
    @Size(max = 100)
    @Column(name = "EMAIL_MANAGER")
    private String emailManager;

    public PrivilegesUsers() {
    }

    public PrivilegesUsers(String loginUser) {
        this.loginUser = loginUser;
    }

    public PrivilegesUsers(String loginUser, String idUser) {
        this.loginUser = loginUser;
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getSurnameUser() {
        return surnameUser;
    }

    public void setSurnameUser(String surnameUser) {
        this.surnameUser = surnameUser;
    }

    public String getPositionUser() {
        return positionUser;
    }

    public void setPositionUser(String positionUser) {
        this.positionUser = positionUser;
    }

    public String getMpkUser() {
        return mpkUser;
    }

    public void setMpkUser(String mpkUser) {
        this.mpkUser = mpkUser;
    }

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getJednOrgId() {
        return jednOrgId;
    }

    public void setJednOrgId(String jednOrgId) {
        this.jednOrgId = jednOrgId;
    }

    public String getJednOrgName() {
        return jednOrgName;
    }

    public void setJednOrgName(String jednOrgName) {
        this.jednOrgName = jednOrgName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getLoginManager() {
        return loginManager;
    }

    public void setLoginManager(String loginManager) {
        this.loginManager = loginManager;
    }

    public String getNameManager() {
        return nameManager;
    }

    public void setNameManager(String nameManager) {
        this.nameManager = nameManager;
    }

    public String getSurnameManager() {
        return surnameManager;
    }

    public void setSurnameManager(String surnameManager) {
        this.surnameManager = surnameManager;
    }

    public String getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(String emailManager) {
        this.emailManager = emailManager;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginUser != null ? loginUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrivilegesUsers)) {
            return false;
        }
        PrivilegesUsers other = (PrivilegesUsers) object;
        if ((this.loginUser == null && other.loginUser != null) || (this.loginUser != null && !this.loginUser.equals(other.loginUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.auction.entity.PrivilegesUsers[ loginUser=" + loginUser + " ]";
    }
    
}
