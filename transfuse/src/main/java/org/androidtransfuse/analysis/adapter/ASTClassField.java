package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.util.AccessibleElementPrivilegedAction;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Collection;

/**
 * Class specific AST Field
 *
 * @author John Ericksen
 */
public class ASTClassField implements ASTField {

    private Field field;
    private ASTType astType;
    private ASTAccessModifier modifier;
    private Collection<ASTAnnotation> annotations;

    public ASTClassField(Field field, ASTType astType, ASTAccessModifier modifier, Collection<ASTAnnotation> annotations) {
        this.field = field;
        this.astType = astType;
        this.modifier = modifier;
        this.annotations = annotations;
    }

    @Override
    public ASTType getASTType() {
        return astType;
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotation) {
        return field.isAnnotationPresent(annotation);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return field.getAnnotation(annotation);
    }

    @Override
    public Collection<ASTAnnotation> getAnnotations() {
        return annotations;
    }

    public ASTAccessModifier getAccessModifier() {
        return modifier;
    }

    @Override
    public ASTAnnotation getASTAnnotation(Class annotation) {
        return ASTUtils.getInstance().getAnnotation(annotation, getAnnotations());
    }

    @Override
    public Object getConstantValue() {
        try {
            //tricky code to access constant value from the current field
            //see PrivateConstantFieldAccessPrivilegedAction below for behaviour
            return AccessController.doPrivileged(
                    new PrivateConstantFieldAccessPrivilegedAction(field));
        } catch (NullPointerException e) {
            return null;
        } catch (PrivilegedActionException e) {
            throw new TransfuseAnalysisException("PrivilegedActionException when trying to set field: " + field, e);
        }
    }

    private static final class PrivateConstantFieldAccessPrivilegedAction extends AccessibleElementPrivilegedAction<Object, Field> {

        protected PrivateConstantFieldAccessPrivilegedAction(Field accessibleObject) {
            super(accessibleObject);
        }

        @Override
        public Object run(Field classField) throws IllegalAccessException {
            return classField.get(null);
        }


    }
}
