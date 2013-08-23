package com.spydiko.rotationmanager;

import android.graphics.drawable.Drawable;

/**
 * Created by PuR3v1L on 7/8/2013.
 */
public class Model {

    private String name;
    private boolean selectedPortrait;
    private boolean selectedLandscape;
    private Drawable label;
    private String packageName;

    public Model(String name) {
        this.name = name;
        selectedPortrait = false;
        selectedLandscape = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectedPortrait() {
        return selectedPortrait;
    }

    public void setSelectedPortrait(boolean selected) {
        this.selectedPortrait = selected;
    }

    public boolean isSelectedLandscape() {
        return selectedLandscape;
    }

    public void setSelectedLandscape(boolean selected) {
        this.selectedLandscape = selected;
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