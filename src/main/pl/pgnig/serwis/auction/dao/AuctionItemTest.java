/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.dao;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import pl.pgnig.serwis.auction.entity.AuctionItem;

/**
 *
 * @author bartosz.szymborski
 */

/*
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AuctionItemTest {

    @Autowired
    private AuctionItemDao auctionItemDao;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager txManager;

    //@PostConstruct
    public void crudTest() {

        TransactionTemplate tmpl = new TransactionTemplate(txManager);
        tmpl.execute(new TransactionCallback<AuctionItem>() {

            @Override
            public AuctionItem doInTransaction(TransactionStatus ts) {
                System.out.println(auctionItemDao.getAuctionItems());
                System.out.println("----------------------------------");

                //AuctionItem auctionItem100 = new AuctionItem(13L, "KompAAA", "00/00/00Z", "1234567890", "Z00", "Brak", "Nie kupuj go", "2010-01-01");
                //auctionItemDao.persistAuctionItem(auctionItem100);
                return auctionItemDao.getAuctionItem(13L);

            }

        });

        System.out.println(auctionItemDao.getAuctionItems());

        tmpl.execute(new TransactionCallback<Boolean>() {

            @Override
            public Boolean doInTransaction(TransactionStatus ts) {
                AuctionItem auctionItem = auctionItemDao.getAuctionItem(13L);
               // auctionItem.setAdditionalInfo("cos"); zmiana dokonana
               // auctionItemDao.persistAuctionItem(auctionItem);
                return true;

            }

        });
        System.out.println(auctionItemDao.getAuctionItems());

        tmpl.execute(new TransactionCallback<AuctionItem>() {

            @Override
            public AuctionItem doInTransaction(TransactionStatus ts) {
                AuctionItem auctionItem = auctionItemDao.getAuctionItem(13L);
               // auctionItemDao.deleteAuctiontItem(13L); ju¿ usuniêty
                return auctionItemDao.getAuctionItem(13L);

            }

        });
        System.out.println("------------------------------");
        System.out.println(auctionItemDao.getAuctionItems());
    }

}
*/
