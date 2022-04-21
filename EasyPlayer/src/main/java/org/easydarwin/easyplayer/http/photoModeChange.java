package org.easydarwin.easyplayer.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bysd-2
 * Time:2021/12/2716:12
 */

public class photoModeChange extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> mWeakReference;
    private final String TAG = photoModeChange.class.getSimpleName();
    private Activity myActivity;

    public photoModeChange(Activity activity){
        mWeakReference = new WeakReference<>(activity);
        myActivity = mWeakReference.get();
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String photoUrl="http://192.168.1.254/?custom=1&cmd=3001&par=0";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .get()
                    .url(photoUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("okhttp:"+TAG,"请求成功");
                new photoChangeServer(myActivity).execute();
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