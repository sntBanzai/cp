/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view.validator;

import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author a6jmalyszko
 */
@FacesValidator
public class NipValidator implements Validator {

    static final String ORDERS_VALIDATION_WRONG_NIP_LENGTH = "orders_validation_wrong_nip_length";
    static final String ORDERS_VALIDATION_NIP_INVALID_CHARACTER = "orders_validation_nip_invalid_character";
    private static final Pattern DIGITS_ONLY_PATTERN = Pattern.compile("\\D");

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object t) throws ValidatorException {
        if (uic.isRendered()) {
            String enajpi = String.valueOf(t);
            enajpi = enajpi.replace("-", "").replace(" ", "");
            String prop = null;
            if (DIGITS_ONLY_PATTERN.matcher(enajpi).find()) {
                prop = ResourceBundle.getBundle("messages").getString(ORDERS_VALIDATION_NIP_INVALID_CHARACTER);
            }
            if (prop == null && enajpi.length() != 10) {
                prop = ResourceBundle.getBundle("messages").getString(ORDERS_VALIDATION_WRONG_NIP_LENGTH);
            }
            if (prop != null) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, prop, prop));
            }
        }
    }

}
