/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author a6jmalyszko
 */
@Entity
@Table(name = "USERS", schema = "AukcjaSprzetuIT")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
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
    @Size(max = 50)
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
    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private List<Users> users = new ArrayList<>();
    
    public Users() {
    }

    public Users(String idUser) {
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

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.auction.entity.Users[ idUser=" + idUser + " ]";
    }
    
}
