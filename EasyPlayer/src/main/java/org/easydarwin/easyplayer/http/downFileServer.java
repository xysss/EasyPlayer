package org.easydarwin.easyplayer.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bysd-2
 * Time:2022/1/614:41
 */

public class downFileServer extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> mWeakReference;
    private final String TAG = downFileServer.class.getSimpleName();
    private static Activity myActivity;
    private String saveFilePath=Environment.getExternalStorageDirectory().getPath() + "/EasyPlayerRTSP/rtsp1921681254"+"/movie/";
    public downFileServer(Activity activity){
        mWeakReference = new WeakReference<>(activity);
        myActivity = mWeakReference.get();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String picUrl="http://192.168.1.254/MateCam/VIDEO/20220104144801_000063.MP4";
            //String MP4Url="http://192.168.1.254/MateCam/VIDEO/"+filePath;
            Log.e("okhttp: " + TAG, "MP4Url: " + picUrl);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(picUrl)//设置请求网址
                    .build();//建立request对象
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveFilePath, "1001.MP4");
                    Log.w(TAG,"最终路径："+file);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        Log.e("okhttp","progress: "+progress);
                    }
                    fos.flush();
                    // 下载完成
                    Log.e("okhttp","down: success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

}