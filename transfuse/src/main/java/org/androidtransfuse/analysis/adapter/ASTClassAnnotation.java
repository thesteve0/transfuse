package org.androidtransfuse.analysis.adapter;

import org.androidtransfuse.analysis.TransfuseAnalysisException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class specific AST Annotation
 *
 * @author John Ericksen
 */
public class ASTClassAnnotation implements ASTAnnotation {

    private Annotation annotation;
    private ASTClassFactory astClassFactory;

    public ASTClassAnnotation(Annotation annotation, ASTClassFactory astClassFactory) {
        this.annotation = annotation;
        this.astClassFactory = astClassFactory;
    }

    @Override
    public <T> T getProperty(String name, Class<T> type) {
        try {
            Method annotationParameter = annotation.annotationType().getMethod(name);

            Class convertedType = type;
            boolean converttoASTType = false;

            if (type.equals(ASTType.class)) {
                convertedType = Class.class;
                converttoASTType = true;
            }

            if (!annotationParameter.getReturnType().isAssignableFrom(convertedType)) {
                throw new TransfuseAnalysisException("Type not expected: " + convertedType);
            }

            Object invocationResult = annotationParameter.invoke(annotation);

            if (converttoASTType) {
                return (T) astClassFactory.buildASTClassType((Class) invocationResult);
            }
            return (T) invocationResult;

        } catch (IllegalAccessException e) {
            throw new TransfuseAnalysisException("IllegalAccessException Exception while accessing annotation method: " + name, e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("Annotation method not present: " + name, e);
        } catch (InvocationTargetException e) {
            throw new TransfuseAnalysisException("InvocationTargetException Exception while accessing annotation method: " + name, e);
        }
    }

    @Override
    public String getName() {
        return annotation.annotationType().getName();
    }
}
