/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.entity.AuctionItem;

/**
 *
 * @author a6jmalyszko
 */
@Component
@Scope("view")
public class AvailableAuctionItemBrowseView extends AbstractBrowseView{

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy");

//    @Autowired
//    private OrdersService ordersService;
    private EquipType category;

    private List<AuctionItem> auctionItems = new ArrayList<>();

    public void openItemDetailsDialog(Long auctionItemId) {
        // Orders itemBought = ordersService.isAllowedToBuyItem(UserUtil.getUserId(), auctionItemId);
        // if (itemBought == null) {
        Map<String, List<String>> params = new HashMap<>();
        params.put(OrderItemStageView.AUCTION_ITEM_ID, Arrays.asList(auctionItemId.toString()));
        PrimeFaces.current().dialog().openDynamic("orderItemStageOneView",
                DialogOptionsBuilder.of("modal")
                        .set("draggable", Boolean.FALSE)
                        .set("resizable", Boolean.FALSE).set("contentHeight", "445px").build(), params);
//        } else {
//            final ResourceBundle bundle = ResourceBundle.getBundle("messages");
//            String tit = bundle.getString("orders_notAllowedToOrderItem_title");
//            String mess = bundle.getString("orders_notAllowedToOrderItem_messOne");
//            mess = mess.replace("{0}", SDF.format(itemBought.getPurchaseDate()))
//                    .replace("{1}", itemBought.getUsers().getNameUser() + " " + itemBought.getUsers().getSurnameUser())
//                    .replace("{2}", itemBought.getAuctionItem().getSerialNumber());
//            mess = mess + "<br/>" + bundle.getString("orders_notAllowedToOrderItem_messTwo").replace("{0}", String.valueOf(LocalDate.now().plusYears(1).getYear()));
//            mess = "<center>" + mess + "</center>";
//            PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, tit, mess));
//        }

    }

    public void openPhotosDialog(String serialNumber, Character equipType) {
        Map<String, List<String>> params = new HashMap<>();
        params.put(AuctionItemPhotosView.SERIAL_NUMBER, Arrays.asList(serialNumber));
        params.put(EquipType.class.getSimpleName(), Arrays.asList(String.valueOf(equipType)));
        PrimeFaces.current().dialog().openDynamic("showPhotosView",
                DialogOptionsBuilder.of("modal")
                        .set("draggable", Boolean.FALSE)
                        .set("resizable", Boolean.FALSE).set("contentWidth", "1050px")
                        .set("contentHeight", "900px").build(), params);
    }

    public void openPhotosDialog(AuctionItem auctionItem) {
        String serialNumber = auctionItem.getSerialNumber();
        EquipType et = EquipType.translate(auctionItem);
        openPhotosDialog(serialNumber, et.name().charAt(0));
    }

    @PostConstruct
    public void refresh() {
        this.auctionItems = itemService.getAvailableAuctionItems();

    }

    public void onDialogReturn(SelectEvent event) throws IOException {
        if (event.getObject() != null && ((boolean) event.getObject())) {
            FacesContext context = FacesContext.getCurrentInstance();
            String refreshpage = context.getViewRoot().getViewId();
            ViewHandler handler = context.getApplication().getViewHandler();
            UIViewRoot root = handler.createView(context, refreshpage);
            root.setViewId(refreshpage);
            context.setViewRoot(root);
            context.getExternalContext().redirect("purchaseHist.xhtml?category=3");
        }

    }

    public List<AuctionItem> getAuctionItems() {
        return this.auctionItems.stream().filter(ai -> EquipType.translate(ai).equals(getCategory())).collect(Collectors.toList());
    }

    public void setAuctionItems(List<AuctionItem> auctionItems) {
        this.auctionItems = auctionItems;
    }

    public void setCategory(Integer equipTypeOrdinal) {
        try {
            this.category = EquipType.values()[equipTypeOrdinal];
        } catch (Exception e) {
            if (this.category == null) {
                this.category = EquipType.K;
            }
        }
    }

    public EquipType getCategory() {
        return category;
    }

}
