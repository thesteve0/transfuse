package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ContextVariableInjectionNodeBuilder implements InjectionNodeBuilder {
    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        injectionNode.addAspect(VariableBuilder.class, new ContextVariableBuilder());

        return injectionNode;
    }
}