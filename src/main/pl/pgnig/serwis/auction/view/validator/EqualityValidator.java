/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view.validator;

import java.util.Objects;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author a6jmalyszko
 */
@FacesValidator
public class EqualityValidator implements Validator {

    public static final String PARAM_NAME = "argument";
    public static final String ORDER_RETYPED_EMAIL_NOT_EQUAL = "order_retyped_email_not_equal";

    private String argument;
    
    @Override
    public void validate(FacesContext fc, UIComponent uic, Object t) throws ValidatorException {

        String confirmEmail = getArgument();
        boolean equal = Objects.equals(t, confirmEmail);
        if (!equal) {
            String string = ResourceBundle.getBundle("messages").getString(ORDER_RETYPED_EMAIL_NOT_EQUAL);
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, string, string));
        }
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    
    
}
