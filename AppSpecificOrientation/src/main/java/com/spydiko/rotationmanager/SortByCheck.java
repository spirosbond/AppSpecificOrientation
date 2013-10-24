package com.spydiko.rotationmanager;

import java.util.Comparator;

/**
 * Created by PuR3v1L on 28/8/2013.
 */
public class SortByCheck implements Comparator {

	public int compare(Object o1, Object o2) {
		Model p1 = (Model) o1;
		Model p2 = (Model) o2;
		int i = p1.getName().compareToIgnoreCase(p2.getName());
		if ((p1.isSelectedLandscape() | p1.isSelectedPortrait()) && (!p2.isSelectedLandscape() && !p2.isSelectedPortrait())) return -1;
		else if ((p2.isSelectedLandscape() | p2.isSelectedPortrait()) && (!p1.isSelectedLandscape() && !p1.isSelectedPortrait())) return 1;
		else return 0;
	}
}