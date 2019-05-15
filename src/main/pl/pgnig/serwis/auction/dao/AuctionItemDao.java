/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Hibernate;
import org.hibernate.Query;

import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.entity.AuctionItem;

/**
 *
 * @author bartosz.szymborski
 */
@Repository
@Component("auctionItemDao")
@Transactional
public class AuctionItemDao {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.SUPPORTS)
    public Collection<? extends AuctionItem> getAuctionItems() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM AuctionItem");
        return query.list();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void persistAuctionItem(AuctionItem auctionItem) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(AuctionItem.class.getSimpleName(), auctionItem); //create and update 
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void convertAndPersistAuctionItem(Row excelFileRow) {
        AuctionItem newItem = new AuctionItem();
        newItem.setEquipmentType(extractCellValue(excelFileRow.getCell(0)));
        newItem.setInventoryNumber(extractCellValue(excelFileRow.getCell(1)));
        newItem.setSerialNumber(extractCellValue(excelFileRow.getCell(2)));
        newItem.setModel(extractCellValue(excelFileRow.getCell(3)));
        newItem.setParameters(extractCellValue(excelFileRow.getCell(4)));
        newItem.setAdditionalInfo(extractCellValue(excelFileRow.getCell(5)));
        newItem.setPurchaseDate(extractCellValue(excelFileRow.getCell(6)));
        double parsedPrice = excelFileRow.getCell(7).getNumericCellValue();
        newItem.setPrice(new BigDecimal(parsedPrice));
        Date dateCellValue = excelFileRow.getCell(8).getDateCellValue();
        newItem.setOfferEndDate(new java.sql.Date(dateCellValue.getTime()));
        persistAuctionItem(newItem);
    }

    private String extractCellValue(Cell cell) {
        return cell != null ? cell.toString() : null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public AuctionItem getAuctionItem(Long idAutionItem) {
        Session session = sessionFactory.getCurrentSession();
        AuctionItem retVal = session.load(AuctionItem.class, idAutionItem);
        Hibernate.initialize(retVal);
        return retVal;//read
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteAuctiontItem(Long id) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(AuctionItem.class.getSimpleName(), getAuctionItem(id));//delete 

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<AuctionItem> getAuctionItemsNotReserved() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("FROM AuctionItem WHERE id NOT IN(SELECT ai.id FROM Orders o JOIN o.auctionItem ai) AND offerEndDate >= :currDate");
        final Date date = new Date();
        query.setParameter("currDate", date);
        return query.list();
    }

}
