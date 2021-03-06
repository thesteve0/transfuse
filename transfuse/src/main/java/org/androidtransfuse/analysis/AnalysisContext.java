package org.androidtransfuse.analysis;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.AOPRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author John Ericksen
 */
public class AnalysisContext {

    private Map<ASTType, InjectionNode> dependents;
    private Stack<InjectionNode> dependencyHistory;
    private AnalysisRepository analysisRepository;
    private InjectionNodeBuilderRepository injectionNodeBuilders;
    private AOPRepository aopRepository;

    @Inject
    public AnalysisContext(@Assisted InjectionNodeBuilderRepository injectionNodeBuilders, AnalysisRepository analysisRepository, AOPRepository aopRepository) {
        this.dependents = Collections.emptyMap();
        this.dependencyHistory = new Stack<InjectionNode>();
        this.analysisRepository = analysisRepository;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.aopRepository = aopRepository;
    }

    private AnalysisContext(InjectionNode node, AnalysisContext previousContext, AnalysisRepository analysisRepository, InjectionNodeBuilderRepository injectionNodeBuilders, AOPRepository aopRepository) {
        this(injectionNodeBuilders, analysisRepository, aopRepository);
        this.dependents = new HashMap<ASTType, InjectionNode>();
        this.dependents.putAll(previousContext.dependents);
        this.dependents.put(node.getASTType(), node);
        this.dependencyHistory.addAll(previousContext.dependencyHistory);
        this.dependencyHistory.push(node);
    }

    public AnalysisContext addDependent(InjectionNode node) {
        return new AnalysisContext(node, this, analysisRepository, injectionNodeBuilders, aopRepository);
    }

    public boolean isDependent(ASTType astType) {
        return dependents.containsKey(astType);
    }

    public InjectionNode getInjectionNode(ASTType astType) {
        return dependents.get(astType);
    }

    public AnalysisRepository getAnalysisRepository() {
        return analysisRepository;
    }

    public InjectionNodeBuilderRepository getInjectionNodeBuilders() {
        return injectionNodeBuilders;
    }

    public AOPRepository getAOPRepository() {
        return aopRepository;
    }

    public Stack<InjectionNode> getDependencyHistory() {
        Stack<InjectionNode> dependencyHistoryCopy = new Stack<InjectionNode>();

        dependencyHistoryCopy.addAll(dependencyHistory);

        return dependencyHistoryCopy;
    }
}
