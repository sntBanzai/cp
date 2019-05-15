/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ADMIN",schema = "AukcjaSprzetuIT")
public class Admin implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ADMIN")
    private Long idAdmin;
    @JoinColumn(name = "ID_ADMIN_TYPE", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AdminType adminType;

    private static final long serialVersionUID = 1L;
    @Size(max = 60)
    @Column(name = "NAME_ADMIN")
    private String nameAdmin;
    @Size(max = 80)
    @Column(name = "SURNAME_ADMIN")
    private String surnameAdmin;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "LOGIN_ADMIN")
    private String loginAdmin;

    public Admin() {
    }

    public Admin(String loginAdmin) {
        this.loginAdmin = loginAdmin;
    }

    public Admin(String loginAdmin, Long idAdmin) {
        this.loginAdmin = loginAdmin;
        this.idAdmin = idAdmin;
    }

    public Long getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Long idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNameAdmin() {
        return nameAdmin;
    }

    public void setNameAdmin(String nameAdmin) {
        this.nameAdmin = nameAdmin;
    }

    public String getSurnameAdmin() {
        return surnameAdmin;
    }

    public void setSurnameAdmin(String surnameAdmin) {
        this.surnameAdmin = surnameAdmin;
    }

    public String getLoginAdmin() {
        return loginAdmin;
    }

    public void setLoginAdmin(String loginAdmin) {
        this.loginAdmin = loginAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginAdmin != null ? loginAdmin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.loginAdmin == null && other.loginAdmin != null) || (this.loginAdmin != null && !this.loginAdmin.equals(other.loginAdmin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.pgnig.serwis.auction.entity.Admin[ loginAdmin=" + loginAdmin + " ]";
    }


    public AdminType getAdminType() {
        return adminType;
    }

    public void setAdminType(AdminType idAdminType) {
        this.adminType = idAdminType;
    }
    
}
