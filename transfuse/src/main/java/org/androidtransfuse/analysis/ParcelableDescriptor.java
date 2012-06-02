package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.GetterSetterMethodPair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class ParcelableDescriptor {

    private List<GetterSetterMethodPair> getterSetterPairs = new ArrayList<GetterSetterMethodPair>();
    private ASTType parcelableConverterType;

    public ParcelableDescriptor() {
    }

    public ParcelableDescriptor(ASTType parcelableConverterType) {
        this.parcelableConverterType = parcelableConverterType;
    }

    public List<GetterSetterMethodPair> getGetterSetterPairs() {
        return getterSetterPairs;
    }

    public ASTType getParcelableConverterType() {
        return parcelableConverterType;
    }
}
