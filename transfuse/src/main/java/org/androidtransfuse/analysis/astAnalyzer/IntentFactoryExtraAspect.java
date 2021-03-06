package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Aspect representing an extra parameter required by a Component.  This will trigger the code generator to
 * build an IntentFactoryStrategy containing the appropriate extras in the constructor or setters.
 *
 * @author John Ericksen
 */
public class IntentFactoryExtraAspect implements Comparable<IntentFactoryExtraAspect> {

    private boolean required;
    private String name;
    private ASTType type;

    public IntentFactoryExtraAspect(boolean required, String name, ASTType type) {
        this.required = required;
        this.name = name;
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        //todo: require simple extra names for Intent Factory parameters
        return name;
    }

    public ASTType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntentFactoryExtraAspect)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        IntentFactoryExtraAspect rhs = (IntentFactoryExtraAspect) obj;
        return new EqualsBuilder()
                .append(name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(IntentFactoryExtraAspect intentFactoryExtra) {
        return getName().compareTo(intentFactoryExtra.getName());
    }
}
