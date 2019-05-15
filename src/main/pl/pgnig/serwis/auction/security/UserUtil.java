/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.security;

import javax.faces.context.FacesContext;

/**
 *
 * @author a6jmalyszko
 */
public class UserUtil {
    
    public static String getUserId(){
        String name = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        return name.replace(PGN, "").replace(STRING, "").replace(STRING1, "");
    }
    static final String STRING1 = "\\";
    static final String STRING = "/";
    static final String PGN = "PGN";
    
}
