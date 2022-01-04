package org.easydarwin.easyplayer.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Author:bysd-2
 * Time:2021/12/2911:31
 */

public class Video extends Item implements Serializable {
    private static final long serialVersionUID = 172717033758067495L;

    private String attr;

    private boolean bComplete = false;

    private boolean bSelect;

    private Bitmap bitmap;

    public int doubleVideoType = 0;

    private boolean isLittleVideo;

    private int position;

    private int progress = -1;

    private String size;

    private String timecode;

    public boolean equals(Item paramObject) {
        if (this != paramObject) {
            if (paramObject == null)
                return false;
            if (getClass() != paramObject.getClass())
                return false;
            paramObject = paramObject;
            if (getFpath() == null)
                return !(paramObject.getFpath() != null);
            if (!getFpath().equals(paramObject.getFpath()))
                return false;
        }
        return true;
    }

    public String getAttr() {
        return this.attr;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public int getPosition() {
        return this.position;
    }

    public int getProgress() {
        return this.progress;
    }

    public String getSize() {
        return this.size;
    }

    public String getTimecode() {
        return this.timecode;
    }

    public int hashCode() {
        if (getFpath() == null) {
            byte b = 0;
            return b + 31;
        }
        int i = getFpath().hashCode();
        return i + 31;
    }

    public boolean isComplete() {
        return this.bComplete;
    }

    public boolean isLittleVideo() {
        return this.isLittleVideo;
    }

    public boolean isbSelect() {
        return this.bSelect;
    }

    public String originName() {
        String str1 = null;
        String str2 = getName().split("\\.")[0];
        if (str2.substring(str2.length() - 1).equals("V"))
            str1 = str2.replace("V", "") + ".MOV";
        return str1;
    }

    public String pathOnSever() {
        String[] arrayOfString = getFpath().split("\\:");
        String str = arrayOfString[arrayOfString.length - 1].replaceAll("\\\\", "/");
        return "http://192.168.1.254" + str;
    }

    public void setAttr(String paramString) {
        this.attr = paramString;
    }

    public void setBitmap(Bitmap paramBitmap) {
        this.bitmap = paramBitmap;
    }

    public void setComplete(boolean paramBoolean) {
        this.bComplete = paramBoolean;
    }

    public void setLittleVideo(boolean paramBoolean) {
        this.isLittleVideo = paramBoolean;
    }

    public void setPosition(int paramInt) {
        this.position = paramInt;
    }

    public void setProgress(int paramInt) {
        this.progress = paramInt;
    }

    public void setSize(String paramString) {
        this.size = paramString;
    }

    public void setTimecode(String paramString) {
        this.timecode = paramString;
    }

    public void setbSelect(boolean paramBoolean) {
        this.bSelect = paramBoolean;
    }
}
