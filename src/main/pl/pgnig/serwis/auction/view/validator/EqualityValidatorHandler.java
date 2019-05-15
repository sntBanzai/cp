/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pgnig.serwis.auction.view.validator;

import java.io.IOException;
import javax.el.ValueExpression;
import javax.el.ValueReference;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.ValidatorConfig;
import javax.faces.view.facelets.ValidatorHandler;

/**
 *
 * @author a6jmalyszko
 */
public class EqualityValidatorHandler extends ValidatorHandler {

    public EqualityValidatorHandler(ValidatorConfig config) {
        super(config);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        super.apply(ctx, parent);
    }

    @Override
    public void setAttributes(FaceletContext ctx, Object instance) {
        super.setAttributes(ctx, instance);
        EqualityValidator valid = (EqualityValidator) instance;
        TagAttribute tagAttribute = getTagAttribute("argument");
        ValueExpression valueExpression = tagAttribute.getValueExpression(ctx, String.class);
        ValueReference valueReference = valueExpression.getValueReference(ctx.getFacesContext().getELContext());
        valid.setArgument((String) valueReference.getProperty());
    }

    @Override
    public TagAttribute getTagAttribute(String localName) {
        final TagAttribute tagAttribute = super.getTagAttribute(localName);
        return tagAttribute;
    }

    @Override
    public Tag getTag() {
        final Tag tag1 = super.getTag();
        return tag1;
    }

    @Override
    public TagAttribute getBinding() {
        final TagAttribute binding = super.getBinding();
        return binding;
    }

}
