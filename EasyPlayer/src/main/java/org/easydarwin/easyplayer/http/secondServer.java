package org.easydarwin.easyplayer.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bysd-2
 * Time:2021/12/2215:22
 */
public class secondServer extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> mWeakReference;
    private final String TAG = secondServer.class.getSimpleName();
    public secondServer(Activity activity){
        mWeakReference = new WeakReference<>(activity);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Date date = new Date();
            DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            String secondTime = format2.format(date);
            Log.e("okhttp:"+TAG,"secondTime: "+secondTime);
            //secondTime="14:14:14";
            String secondUrl="http://192.168.1.254/?custom=1&cmd=3006&str="+secondTime;
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .get()
                    .url(secondUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
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
        Activity myActivity = mWeakReference.get();
    }
}
