/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.primefaces.context.PrimeFacesContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 *
 * @author a6jmalyszko
 */
@Component
@RequestScope
public class ExcelImportOutcomeReportView {

    static ResourceBundle rb = ResourceBundle.getBundle("messages");

    Map<ExcelImportOutcomeTypes, List<String>> messages = new HashMap<>();

    public Map<ExcelImportOutcomeTypes, List<String>> getMessages() {
        return messages;
    }

    @PostConstruct
    public void init() {
        Map<String, String> requestParameterMap = PrimeFacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        for (Map.Entry<String, String> ent : requestParameterMap.entrySet()) {
            try {
                ExcelImportOutcomeTypes typ = ExcelImportOutcomeTypes.valueOf(ent.getKey());

                String[] split = ent.getValue().split("\\|");
                for (String str : split) {
                    if ("".equals(str)) {
                        continue;
                    }
                    List<String> list = messages.get(typ);
                    if (list == null) {
                        messages.put(typ, new LinkedList<>());
                        list = messages.get(typ);
                    }
                    list.add(str);
                }
            } catch (IllegalArgumentException iae) {
                continue;
            }
        }
    }

}
