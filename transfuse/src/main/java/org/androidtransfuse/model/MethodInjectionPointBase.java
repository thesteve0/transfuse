package org.androidtransfuse.model;

import org.androidtransfuse.analysis.adapter.ASTType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public abstract class MethodInjectionPointBase {

    private List<InjectionNode> injectionNodes = new ArrayList<InjectionNode>();
    private List<ASTType> throwsTypes = new ArrayList<ASTType>();

    public void addInjectionNode(InjectionNode injectionNode) {
        this.injectionNodes.add(injectionNode);
    }

    public List<InjectionNode> getInjectionNodes() {
        return injectionNodes;
    }

    public void addThrows(Collection<ASTType> types) {
        throwsTypes.addAll(types);
    }

    public List<ASTType> getThrowsTypes() {
        return throwsTypes;
    }
}
