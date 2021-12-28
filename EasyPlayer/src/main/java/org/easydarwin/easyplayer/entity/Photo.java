package org.easydarwin.easyplayer.entity;

import java.io.Serializable;

/**
 * Author:bysd-2
 * Time:2021/12/2717:34
 */

public class Photo extends Item implements Serializable {
    private static final long serialVersionUID = 7328493133206853070L;

    public Photo() {}

    public Photo(Item paramItem) {
        setName(paramItem.getName());
        setFpath(paramItem.getFpath());
        setDate(paramItem.getDate());
        setTime(paramItem.getTime());
        setSection(paramItem.getSection());
    }


    public int hashCode() {
        if (getFpath() == null) {
            byte b = 0;
            return b + 31;
        }
        int i = getFpath().hashCode();
        return i + 31;
    }
}
