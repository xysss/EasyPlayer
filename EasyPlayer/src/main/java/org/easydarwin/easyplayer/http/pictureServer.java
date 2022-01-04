package org.easydarwin.easyplayer.http;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bysd-2
 * Time:2021/12/2217:17
 */

public class pictureServer extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> mWeakReference;
    private final String TAG = pictureServer.class.getSimpleName();
    private static Activity myActivity;
    private String filePath="";
    public pictureServer(Activity activity,String FPATH){
        mWeakReference = new WeakReference<>(activity);
        myActivity = mWeakReference.get();
        filePath=FPATH;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //String picUrl="http://192.168.1.254/MateCam/PHOTO/20211125091911_000015.JPG";
            String picUrl="http://192.168.1.254/MateCam/PHOTO/"+filePath;
            Log.e("okhttp: " + TAG, "picUrl: " + picUrl);
            /*URL url = new URL(picUrl);
            //打开输入流
            InputStream inputStream = url.openStream();
            //对网上资源进行下载转换位图图片
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            saveFile(bitmap);
            Log.e("okhttp:"+TAG,"请求成功");*/

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(picUrl)//设置请求网址
                    .build();//建立request对象
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                //得到从网上获取资源,转换成我们想要的类型
                byte[] Picture_bt = response.body().bytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                saveFile(bitmap,filePath);

                Log.e("okhttp:"+TAG,"请求成功");

            } else {
                Log.e("okhttp:"+TAG,"请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("okhttp:"+TAG,"e: "+e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        /*byte[] Picture = (byte[]) msg.obj;
        //使用BitmapFactory工厂,把字节数组转换为bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
        //通过ImageView,设置图片
        mImageView_okhttp.setImageBitmap(bitmap);*/
    }

    public static void saveFile(Bitmap bm,String mfilePath) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName =mfilePath;
        //String fileName = UUID.randomUUID().toString() + ".JPG";
        // /storage/emulated/0/EasyPlayerRTSP/rtsp1921681254/picture/21_12_29 10_24_14.jpg
        String pactureUrlPath=Environment.getExternalStorageDirectory().getPath() + "/EasyPlayerRTSP/rtsp1921681254"+"/picture/";
        File myCaptureFile = new File(pactureUrlPath + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        //把图片保存后声明这个广播事件通知系统相册有新图片到来
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        myActivity.sendBroadcast(intent);
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


}