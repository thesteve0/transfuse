package org.androidtransfuse.analysis;

import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.adapter.ASTTypeBuilderVisitor;
import org.androidtransfuse.analysis.repository.BindingRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.annotations.Fragment;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.gen.componentBuilder.ComponentBuilderFactory;
import org.androidtransfuse.gen.componentBuilder.MethodCallbackGenerator;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.PackageClass;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.util.TypeMirrorRunnable;
import org.androidtransfuse.util.TypeMirrorUtil;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.lang.model.type.TypeMirror;

/**
 * @author John Ericksen
 */
public class FragmentAnalysis implements Analysis<ComponentDescriptor> {

    private ASTClassFactory astClassFactory;
    private TypeMirrorUtil typeMirrorUtil;
    private AnalysisContextFactory analysisContextFactory;
    private InjectionNodeBuilderRepository injectionNodeBuilderRepository;
    private InjectionBindingBuilder injectionBindingBuilder;
    private ASTTypeBuilderVisitor astTypeBuilderVisitor;
    private InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory;
    private ComponentBuilderFactory componentBuilderFactory;
    private BindingRepositoryFactory bindingRepositoryFactory;

    @Inject
    public FragmentAnalysis(ASTClassFactory astClassFactory,
                            TypeMirrorUtil typeMirrorUtil,
                            AnalysisContextFactory analysisContextFactory,
                            InjectionNodeBuilderRepository injectionNodeBuilderRepository,
                            InjectionBindingBuilder injectionBindinBuilder,
                            ASTTypeBuilderVisitor astTypeBuilderVisitor,
                            InjectionNodeBuilderRepositoryFactory injectionNodeBuilderRepositoryFactory,
                            ComponentBuilderFactory componentBuilderFactory,
                            BindingRepositoryFactory bindingRepositoryFactory) {
        this.astClassFactory = astClassFactory;
        this.typeMirrorUtil = typeMirrorUtil;
        this.analysisContextFactory = analysisContextFactory;
        this.injectionNodeBuilderRepository = injectionNodeBuilderRepository;
        this.injectionBindingBuilder = injectionBindinBuilder;
        this.astTypeBuilderVisitor = astTypeBuilderVisitor;
        this.injectionNodeBuilderRepositoryFactory = injectionNodeBuilderRepositoryFactory;
        this.componentBuilderFactory = componentBuilderFactory;
        this.bindingRepositoryFactory = bindingRepositoryFactory;
    }

    @Override
    public ComponentDescriptor analyze(ASTType astType) {
        Fragment fragmentAnnotation = astType.getAnnotation(Fragment.class);
        PackageClass fragmentClassName;
        ComponentDescriptor fragmentDescriptor = null;

        if (!astType.extendsFrom(astClassFactory.buildASTClassType(android.support.v4.app.Fragment.class))) {
            //generated Android fragment
            fragmentClassName = buildPackageClass(astType, fragmentAnnotation.name());

            Layout layoutAnnotation = astType.getAnnotation(Layout.class);
            //todo: fragment specific layout handler

            TypeMirror type = typeMirrorUtil.getTypeMirror(new FragmentTypeMirrorRunnable(fragmentAnnotation));

            ASTType fragmentType = type == null ? astClassFactory.buildASTClassType(android.support.v4.app.Fragment.class)
                    : type.accept(astTypeBuilderVisitor, null);


            Integer layout = layoutAnnotation == null ? null : layoutAnnotation.value();

            AnalysisContext context = analysisContextFactory.buildAnalysisContext(buildVariableBuilderMap(type));

            fragmentDescriptor = new ComponentDescriptor(fragmentType.getName(), fragmentClassName);

            //application generation profile
            setupFragmentProfile(fragmentDescriptor, astType, fragmentType, context, layout);
        }


        return fragmentDescriptor;

    }

    private void setupFragmentProfile(ComponentDescriptor fragmentDescriptor, ASTType astType, ASTType fragmentType, AnalysisContext context, Integer layout) {

        ASTMethod onCreateViewMethod = getASTMethod("onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class);

        fragmentDescriptor.setMethodBuilder(componentBuilderFactory.buildFragmentMethodBuilder(layout, onCreateViewMethod));

        fragmentDescriptor.setInjectionNodeFactory(componentBuilderFactory.buildInjectionNodeFactory(astType, context));

        //onActivityCreated
        fragmentDescriptor.addGenerators(buildEventMethod("onActivityCreated", Bundle.class));
        //onStart
        fragmentDescriptor.addGenerators(buildEventMethod("onStart"));
        //onResume
        fragmentDescriptor.addGenerators(buildEventMethod("onResume"));
        //onPause
        fragmentDescriptor.addGenerators(buildEventMethod("onPause"));
        //onStop
        fragmentDescriptor.addGenerators(buildEventMethod("onStop"));
        //onDestroyView
        fragmentDescriptor.addGenerators(buildEventMethod("onDestroyView"));
        //onDestroy
        fragmentDescriptor.addGenerators(buildEventMethod("onDestroy"));
        //onDetach
        fragmentDescriptor.addGenerators(buildEventMethod("onDetach"));
        //onLowMemory
        fragmentDescriptor.addGenerators(buildEventMethod("onLowMemory"));

        //onConfigurationChanged
        fragmentDescriptor.addGenerators(buildEventMethod("onConfigurationChanged", "onConfigurationChanged", Configuration.class));

        if (fragmentType.extendsFrom(astClassFactory.buildASTClassType(ListFragment.class))) {
            ASTMethod onListItemClickMethod = getASTMethod(ListActivity.class, "onListItemClick", ListView.class, View.class, Integer.TYPE, Long.TYPE);
            fragmentDescriptor.addGenerators(
                    componentBuilderFactory.buildMethodCallbackGenerator("onListItemClick",
                            componentBuilderFactory.buildMirroredMethodGenerator(onListItemClickMethod, false)));
        }

    }

    private MethodCallbackGenerator buildEventMethod(String name, Class... args) {
        return buildEventMethod(name, name, args);
    }

    private MethodCallbackGenerator buildEventMethod(String eventName, String methodName, Class... args) {
        ASTMethod method = getASTMethod(methodName, args);

        return componentBuilderFactory.buildMethodCallbackGenerator(eventName, componentBuilderFactory.buildMirroredMethodGenerator(method, true));
    }

    private ASTMethod getASTMethod(String methodName, Class... args) {
        return getASTMethod(android.support.v4.app.Fragment.class, methodName, args);
    }

    private ASTMethod getASTMethod(Class type, String methodName, Class... args) {
        try {
            return astClassFactory.buildASTClassMethod(type.getDeclaredMethod(methodName, args));
        } catch (NoSuchMethodException e) {
            throw new TransfuseAnalysisException("NoSuchMethodException while trying to reference method " + methodName, e);
        }
    }

    private InjectionNodeBuilderRepository buildVariableBuilderMap(TypeMirror type) {

        injectionNodeBuilderRepository.putType(android.support.v4.app.Fragment.class, injectionBindingBuilder.buildThis(android.support.v4.app.Fragment.class));
        injectionNodeBuilderRepository.putType(Activity.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(Activity.class, "getActivity").build());
        injectionNodeBuilderRepository.putType(Context.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(Context.class, "getActivity").build());
        injectionNodeBuilderRepository.putType(FragmentManager.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(FragmentManager.class, "getFragmentManager").build());
        injectionNodeBuilderRepository.putType(Application.class, injectionBindingBuilder.dependency(Activity.class).invoke(Application.class, "getApplication").build());
        injectionNodeBuilderRepository.putType(ContextScopeHolder.class, injectionBindingBuilder.dependency(android.support.v4.app.Fragment.class).invoke(Activity.class, "getActivity").build());

        if (type != null) {
            ASTType fragmentASTType = type.accept(astTypeBuilderVisitor, null);
            injectionNodeBuilderRepository.putType(fragmentASTType, injectionBindingBuilder.buildThis(fragmentASTType));
        }

        bindingRepositoryFactory.addBindingAnnotations(injectionNodeBuilderRepository);
        //bind views
        bindingRepositoryFactory.addFragmentViewBindingAnnotation(injectionNodeBuilderRepository);

        injectionNodeBuilderRepositoryFactory.addApplicationInjections(injectionNodeBuilderRepository);

        return injectionNodeBuilderRepository;
    }

    private PackageClass buildPackageClass(ASTType input, String fragmentName) {

        PackageClass inputPackageClass = new PackageClass(input.getName());

        if (StringUtils.isBlank(fragmentName)) {
            return inputPackageClass.append("Fragment");
        } else {
            return inputPackageClass.replaceName(fragmentName);
        }
    }

    private static final class FragmentTypeMirrorRunnable extends TypeMirrorRunnable<Fragment> {
        public FragmentTypeMirrorRunnable(Fragment fragmentAnnotation) {
            super(fragmentAnnotation);
        }

        @Override
        public void run(Fragment annotation) {
            annotation.type();
        }


    }
}
