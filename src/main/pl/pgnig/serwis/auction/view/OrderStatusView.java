/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.pgnig.serwis.auction.service.OrderStatusService;

/**
 *
 * @author jerzy.malyszko
 */
@Component
@Scope("view")
public class OrderStatusView extends AdminOrderView {
    
    @Autowired
    private OrderStatusService orderStatusService;

    public void openStatusChangeDialog(Long ordersId) {
        try{
            orderStatusService.throwExcWhenNotAllowedToChangeStatus(ordersId);
            
            Map<String, List<String>> params = new HashMap<>();
            params.put(OrderStatusChangeView.ORDERS_ID, Arrays.asList(ordersId.toString()));
            PrimeFaces.current().dialog().openDynamic("orderStatusChange",
                    DialogOptionsBuilder.of("modal")
                            .set("draggable", Boolean.FALSE)
                            .set("resizable", Boolean.FALSE).set("contentHeight", "445px").build(), params);
        
        } catch (OrderStatusService.OrderStatusChangeNotAllowedException ex){
            final ResourceBundle bundle = ResourceBundle.getBundle("messages");
            String tit = bundle.getString("orders_notAllowedToChangeStatus_title");
            String mess = bundle.getString("orders_notAllowedToChangeStatus_mess");
            mess = mess.replace("{0}", ex.getCurrStat().getNamePl());
            PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, tit, mess));
        }
        
    }

    public void onDialogReturn(SelectEvent event) throws IOException {
        if (event.getObject() != null && ((boolean) event.getObject())) {
            FacesContext context = FacesContext.getCurrentInstance();
            String refreshpage = context.getViewRoot().getViewId();
            ViewHandler handler = context.getApplication().getViewHandler();
            UIViewRoot root = handler.createView(context, refreshpage);
            root.setViewId(refreshpage);
            context.setViewRoot(root);
            context.getExternalContext().redirect("adminPanel.xhtml?category=4&faces-redirect=true");
        }
    }
    
    @Override
    protected String getWidgetName() {
        return "bartek";
    }

}
