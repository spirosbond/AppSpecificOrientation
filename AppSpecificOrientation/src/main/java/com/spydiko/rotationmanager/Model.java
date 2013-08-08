package com.spydiko.rotationmanager;

import android.graphics.drawable.Drawable;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class Model {

    private String name;
    private boolean selected;
    private Drawable label;
    private String packageName;

    public Model(String name) {
        this.name = name;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Drawable getLabel() {
        return label;
    }

    public void setLabel(Drawable label) {
        this.label = label;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}