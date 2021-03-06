package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTAccessModifier;
import org.androidtransfuse.analysis.adapter.ASTType;

/**
 * @author John Ericksen
 */
public class MethodInjectionPoint extends MethodInjectionPointBase {

    private ASTType containingType;
    private String name;
    private ASTAccessModifier accessModifier;

    public MethodInjectionPoint(ASTType containingType, ASTAccessModifier accessModifier, String name) {
        this.name = name;
        this.accessModifier = accessModifier;
        this.containingType = containingType;
    }

    public String getName() {
        return name;
    }

    public ASTAccessModifier getAccessModifier() {
        return accessModifier;
    }

    public ASTType getContainingType() {
        return containingType;
    }
}
