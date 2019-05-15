/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.service;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pgnig.serwis.auction.dao.AuctionItemDao;
import pl.pgnig.serwis.auction.entity.AuctionItem;
import pl.pgnig.serwis.auction.view.ExcelImportOutcomeTypes;

/**
 *
 * @author a6jmalyszko
 */
@Service("defaultAuctionItemServiceImpl")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DefaultAuctionItemServiceImpl implements AuctionItemService<Workbook> {

    @Autowired
    private AuctionItemDao auctionItemDao;

    @Override
    public Map<ExcelImportOutcomeTypes, Set<String>> persistExcelInput(Workbook wbk) {
        Map<ExcelImportOutcomeTypes, Set<String>> retVal = new EnumMap<>(ExcelImportOutcomeTypes.class);
        for(ExcelImportOutcomeTypes type : ExcelImportOutcomeTypes.values()){
            retVal.put(type, new HashSet<>());
        }
        Sheet sheet = wbk.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            final String inventoryNum = row.getCell(1).getRichStringCellValue().toString();
            try {
                auctionItemDao.convertAndPersistAuctionItem(row);
                retVal.get(ExcelImportOutcomeTypes.SUCCESSFULL).add(inventoryNum);
            } catch (DataIntegrityViolationException dive) {
                Logger.getLogger(DefaultAuctionItemServiceImpl.class.getName()).log(Level.SEVERE, null, dive);
                boolean messAdded = false;
                if (dive.getCause() instanceof ConstraintViolationException) {
                    ConstraintViolationException cve = (ConstraintViolationException) dive.getCause();
                    if (cve.getConstraintName() != null && cve.getConstraintName().contains(".UK_")) {
                        
                        retVal.get(ExcelImportOutcomeTypes.INVENTORY_NUMBER_DUPLICATION).add(inventoryNum);
                        messAdded = true;
                    }
                }
                if (!messAdded) {
                     retVal.get(ExcelImportOutcomeTypes.UNSUCCESSFUL_UNKNOWN).add(inventoryNum);
                }

            } catch (Exception ex) {
                Logger.getLogger(DefaultAuctionItemServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                retVal.get(ExcelImportOutcomeTypes.UNSUCCESSFUL_UNKNOWN).add(inventoryNum);
            }
        }
        return retVal;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<AuctionItem> getAuctionItems(Temporal borderDate) {
        return new ArrayList<>(auctionItemDao.getAuctionItems());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<AuctionItem> getAvailableAuctionItems() {
        List<AuctionItem> items = new ArrayList<>(auctionItemDao.getAuctionItemsNotReserved());
        return items;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public AuctionItem getAuctionItem(Long valueOf) {
        return auctionItemDao.getAuctionItem(valueOf);
    }
   

}
