/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 *
 * @author bartosz.szymborski
 */
@Entity
@Table(name = "auction_item", schema = "AukcjaSprzetuIT", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"inventoryNumber"})
})
public class AuctionItem {

    private static final SimpleDateFormat DATA = new SimpleDateFormat("dd-MMM-yyyy");
    private static final SimpleDateFormat DATA_US = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    private static final SimpleDateFormat DATA2 = new SimpleDateFormat("yyyy-MM-dd");
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String equipmentType;
    @Column(nullable = false, unique = true)
    private String inventoryNumber;
    @Column(nullable = false)
    private String serialNumber;
    private String model;
    private String parameters;
    private String additionalInfo;

    private String purchaseDate;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Date offerEndDate;

    @Version
    private Integer version;

    public AuctionItem(long l, String hp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getPurchaseDate() throws ParseException {
        try {
            java.util.Date date = null;
            try{
                date = DATA.parse(purchaseDate);
            } catch(ParseException pe){
                date = DATA_US.parse(purchaseDate);
            }
            return DATA2.format(date);
        } catch (Exception pe) {
            return purchaseDate;
        }

    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public AuctionItem() {
    }

    @Override
    public String toString() {
        return "AuctionItem{" + "id=" + id + ", equipmentType=" + equipmentType + ", inventoryNumber=" + inventoryNumber + ", serialNumber=" + serialNumber + ", model=" + model + ", parameters=" + parameters + ", additionalInfo=" + additionalInfo + ", purchaseDate=" + purchaseDate + ", version=" + version + '}';
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getOfferEndDate() {
        return offerEndDate;
    }

    public void setOfferEndDate(Date offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public String getPriceFormatted() {
        return FORMAT.format(getPrice());
    }

    static final NumberFormat FORMAT = NumberFormat.getCurrencyInstance();

    static {
        FORMAT.setMinimumFractionDigits(2);
    }

}
