package org.easydarwin.easyplayer.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Author:bysd-2
 * Time:2021/12/2911:30
 */
public class LocalVideo extends Video implements Serializable {
    private boolean bSelect;

    private Date begainDate;

    public boolean isClipVideo;

    private String formetFileSize(long paramLong) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return (paramLong == 0L) ? "0B" : ((paramLong < 1024L) ? (decimalFormat.format(paramLong) + "B") : ((paramLong < 1048576L) ? (decimalFormat.format(paramLong / 1024.0D) + "K") : ((paramLong < 1073741824L) ? (decimalFormat.format(paramLong / 1048576.0D) + "M") : (decimalFormat.format(paramLong / 1.073741824E9D) + "G"))));
    }

    public boolean equals(Object paramObject) {
        boolean bool2 = false;
        if (this == paramObject)
            return true;
        boolean bool1 = bool2;
        if (paramObject != null) {
            LocalVideo localVideo = (LocalVideo)paramObject;
            bool1 = bool2;
            if (isbSelect() == localVideo.isbSelect())
                return super.equals(paramObject);
        }
        return bool1;
    }

    public Date getBegainDate() {
        return this.begainDate;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean isbSelect() {
        return this.bSelect;
    }

    public void setBegainDate(Date paramDate) {
        this.begainDate = paramDate;
    }

    public void setFpath(String paramString) {
        super.setFpath(paramString);
        File file = new File(paramString);
        if (file.exists() && file.exists())
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    setSize(formetFileSize(fileInputStream.available()));
                    return;
                } catch (FileNotFoundException exception) {

                } catch (IOException iOException) {
                    return;
                }
                return;
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
                return;
            }
    }

    public void setbSelect(boolean paramBoolean) {
        this.bSelect = paramBoolean;
    }
}
