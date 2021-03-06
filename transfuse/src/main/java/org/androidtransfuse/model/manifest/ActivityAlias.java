package org.androidtransfuse.model.manifest;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

/**
 * attributes
 * android:enabled=["true" | "false"]
 * android:exported=["true" | "false"]
 * android:icon="drawable resource"
 * android:label="string resource"
 * android:name="string"
 * android:permission="string"
 * android:targetActivity="string"
 * <p/>
 * can contain:
 * <intent-filter>
 * <meta-data>
 *
 * @author John Ericksen
 */
public class ActivityAlias {

    @XStreamAlias("android:enabled")
    @XStreamAsAttribute
    private Boolean enabled;
    @XStreamAlias("android:exported")
    @XStreamAsAttribute
    private Boolean exported;
    @XStreamAlias("android:icon")
    @XStreamAsAttribute
    private String icon;
    @XStreamAlias("android:label")
    @XStreamAsAttribute
    private String label;
    @XStreamAlias("android:name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("android:permission")
    @XStreamAsAttribute
    private String permission;
    @XStreamAlias("android:targetActivity")
    @XStreamAsAttribute
    private String targetActivity;

    @XStreamImplicit(itemFieldName = "intent-filter")
    private List<IntentFilter> intentFilters = new ArrayList<IntentFilter>();
    @XStreamImplicit(itemFieldName = "meta-data")
    private List<MetaData> metaData = new ArrayList<MetaData>();

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getExported() {
        return exported;
    }

    public void setExported(Boolean exported) {
        this.exported = exported;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(String targetActivity) {
        this.targetActivity = targetActivity;
    }

    public List<IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(List<IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    public List<MetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaData> metaData) {
        this.metaData = metaData;
    }
}
