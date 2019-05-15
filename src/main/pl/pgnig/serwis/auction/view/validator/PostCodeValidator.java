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
public class PostCodeValidator implements Validator {

    static final String ORDERS_VALIDATION_ADDRESS_CONTAINS_NO_POS = "orders_validation_address_contains_no_post_code";
    private static final Pattern POST_CODE_PATTERN = Pattern.compile("\\d{2}-\\d{3}");

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object t) throws ValidatorException {
        if (uic.isRendered()) {
            String val = String.valueOf(t);
            if (!POST_CODE_PATTERN.matcher(val).find()) {
                String mess = ResourceBundle.getBundle("messages").getString(ORDERS_VALIDATION_ADDRESS_CONTAINS_NO_POS);
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, mess, mess));
            }
        }
    }
    

}
