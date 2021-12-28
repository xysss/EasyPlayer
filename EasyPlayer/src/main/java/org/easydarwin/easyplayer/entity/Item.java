package org.easydarwin.easyplayer.entity;

import android.util.Log;

import java.io.Serializable;

/**
 * Author:bysd-2
 * Time:2021/12/2717:34
 */

public class Item implements Serializable {
    private static final long serialVersionUID = -1282514108309243354L;

    private String date;

    private String fpath;

    private String name;

    private int section;

    private String time;

    public String getDate() {
        return this.date;
    }

    public String getFpath() {
        return this.fpath;
    }

    public String getName() {
        return this.name;
    }

    public int getSection() {
        return this.section;
    }

    public String getTime() {
        return this.time;
    }

    public String serverFilePath() {
        String str1 = Const.hostString;
        String[] arrayOfString = getFpath().split(":");
        String str2 = arrayOfString[arrayOfString.length - 1].replaceAll("\\\\", "/");
        str1 = str1 + str2;
        Log.i("picture", "picture path = " + str1);
        return str1;
    }

    public void setDate(String paramString) {
        this.date = paramString;
    }

    public void setFpath(String paramString) {
        this.fpath = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setSection(int paramInt) {
        this.section = paramInt;
    }

    public void setTime(String paramString) {
        this.time = paramString;
    }
}
