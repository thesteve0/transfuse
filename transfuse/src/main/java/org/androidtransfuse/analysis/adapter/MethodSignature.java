package org.androidtransfuse.analysis.adapter;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

/**
 * @author John Ericksen
 */
public class MethodSignature {

    private String methodSignature;
    private ASTMethod method;

    public MethodSignature(ASTMethod method) {
        methodSignature = makeDescriptor(method);
        this.method = method;
    }

    public ASTMethod getMethod() {
        return method;
    }

    /**
     * Makes a descriptor for a given method.
     *
     * @param method
     * @return descriptor
     */
    private String makeDescriptor(ASTMethod method) {
        List<ASTParameter> params = method.getParameters();
        return method.getName() + ':' + makeDescriptor(params, method.getReturnType());
    }

    /**
     * Makes a descriptor for a given method.
     *
     * @param params  parameter types.
     * @param retType return type.
     * @return method descriptor
     */
    private String makeDescriptor(List<ASTParameter> params, ASTType retType) {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        for (ASTParameter param : params) {
            makeDesc(builder, param.getASTType());
        }

        builder.append(')');
        makeDesc(builder, retType);
        return builder.toString();
    }

    private void makeDesc(StringBuilder builder, ASTType type) {
        if (type.isArray()) {
            builder.append('[');
        }
        makeTypeDesc(builder, type);
    }

    private void makeTypeDesc(StringBuilder builder, ASTType type) {
        if (type instanceof ASTPrimitiveType) {
            builder.append(type.getName());
        } else {
            builder.append('L').append(type.getName().replace('.', '/')).append(';');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodSignature)) {
            return false;
        }

        MethodSignature that = (MethodSignature) o;

        return new EqualsBuilder().append(methodSignature, that.methodSignature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(methodSignature).hashCode();
    }
}
