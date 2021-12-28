package org.easydarwin.easyplayer.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileUtil {

    private static String path = Environment.getExternalStorageDirectory() +"/EasyPlayerRTSP";

    public static String getPicturePath(String url) {
        return path + "/" + urlDir(url) + "/picture";
    }

    public static File getPictureName(String url) {
        File file = new File(getPicturePath(url));
        file.mkdirs();

        File res = new File(file, new SimpleDateFormat("yy_MM_dd HH_mm_ss").format(new Date()) + ".jpg");
        return res;
    }

    public static String getMoviePath(String url) {
        return path + "/" + urlDir(url) + "/movie";
    }

    public static File getMovieName(String url) {
        File file = new File(getMoviePath(url));
        file.mkdirs();

        File res = new File(file, new SimpleDateFormat("yy_MM_dd HH_mm_ss").format(new Date()) + ".mp4");
        return res;
    }

    private static String urlDir(String url) {
        url = url.replace("://", "");
        url = url.replace("/", "");
        url = url.replace(".", "");

        if (url.length() > 64) {
            url.substring(0, 63);
        }

        return url;
    }

    /*
     * 截屏
     * */
    public static File getSnapFile(String url) {
        File file = new File(getPicturePath(url));
        file.mkdirs();

        File res = new File(file, "snap.jpg");
        return res;
    }

    //遍历文件夹下所有文件
    public static ArrayList<String> getFileName(String fileAbsolutePath) {
        ArrayList<String> vecFile = new ArrayList<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        vecFile.clear();
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                vecFile.add("/Mylibrary/Peak/" + filename);
            }
        }
        return vecFile;
    }
}
