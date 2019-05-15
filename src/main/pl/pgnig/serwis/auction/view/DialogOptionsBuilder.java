/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author a6jmalyszko
 */
public class DialogOptionsBuilder {

    private DialogOptionsBuilder() {

    }

    private Map<String, Object> innerMap = new HashMap<>();

    public DialogOptionsBuilder set(String opt) {
        innerMap.put(opt, Boolean.TRUE);
        return this;
    }

    public DialogOptionsBuilder set(String opt, Object val) {
        innerMap.put(opt, val);
        return this;
    }

    public Map<String, Object> build() {
        return innerMap;
    }

    public static DialogOptionsBuilder of(String... opts) {
        DialogOptionsBuilder retVal = new DialogOptionsBuilder();
        if (opts != null) {
            for (String opt : opts) {
                retVal.set(opt);
            }
        }
        return retVal;
    }

}
