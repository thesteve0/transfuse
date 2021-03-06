package org.androidtransfuse.model;

import org.androidtransfuse.gen.componentBuilder.ExpressionVariableDependentGenerator;
import org.androidtransfuse.gen.componentBuilder.InjectionNodeFactory;
import org.androidtransfuse.gen.componentBuilder.MethodBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ComponentDescriptor {

    private PackageClass packageClass;
    private String type;
    private MethodBuilder methodBuilder;
    private List<ExpressionVariableDependentGenerator> generators = new ArrayList<ExpressionVariableDependentGenerator>();
    private InjectionNodeFactory injectionNodeFactory;

    public ComponentDescriptor(String type, PackageClass packageClass) {
        this.type = type;
        this.packageClass = packageClass;
    }

    public PackageClass getPackageClass() {
        return packageClass;
    }

    public String getType() {
        return type;
    }

    public MethodBuilder getMethodBuilder() {
        return methodBuilder;
    }

    public List<ExpressionVariableDependentGenerator> getGenerators() {
        return generators;
    }

    public void addGenerators(Collection<ExpressionVariableDependentGenerator> generators) {
        this.generators.addAll(generators);
    }

    public void addGenerators(ExpressionVariableDependentGenerator... generators) {
        if (generators != null) {
            addGenerators(Arrays.asList(generators));
        }
    }

    public void setMethodBuilder(MethodBuilder methodBuilder) {
        this.methodBuilder = methodBuilder;
    }

    public InjectionNodeFactory getInjectionNodeFactory() {
        return injectionNodeFactory;
    }

    public void setInjectionNodeFactory(InjectionNodeFactory injectionNodeFactory) {
        this.injectionNodeFactory = injectionNodeFactory;
    }
}
