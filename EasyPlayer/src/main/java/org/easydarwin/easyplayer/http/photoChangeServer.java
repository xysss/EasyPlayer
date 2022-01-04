package org.easydarwin.easyplayer.http;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bysd-2
 * Time:2021/12/2410:49
 */
public class photoChangeServer extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> mWeakReference;
    private final String TAG = photoChangeServer.class.getSimpleName();
    private Activity myActivity;


    public photoChangeServer(Activity activity) {
        mWeakReference = new WeakReference<>(activity);
        myActivity = mWeakReference.get();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String photoUrl = "http://192.168.1.254/?custom=1&cmd=1001";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .get()
                    .url(photoUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {

                //parseCaptureResultold(response.body().toString());

                parseCaptureResult(response.body().bytes());
                Log.e("okhttp:" + TAG, "请求成功");
            } else {
                Log.e("okhttp:" + TAG, "请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("okhttp:" + TAG, "e: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
    }

    private void parseCaptureResult(byte[] paramString) {
        try {

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                //xmlPullParser.setInput(new StringReader(paramString));

                //byte[] Picture_bt = response.body().bytes();
                InputStream is = new ByteArrayInputStream(paramString);
                xmlPullParser.setInput(is, "utf-8");


                /*ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(paramString.getBytes());
                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setInput(byteArrayInputStream, "UTF-8");*/

                int eventType = xmlPullParser.getEventType();
                String Status = "";
                String NAME = "";
                String FPATH = "";
                while (eventType != xmlPullParser.END_DOCUMENT) {
                    String nodeName = xmlPullParser.getName();
                    switch (eventType) {
                        //开始解析某个节点
                        case START_TAG: {
                            if ("Status".equals(nodeName)) {
                                Status = xmlPullParser.nextText();
                            } else if ("NAME".equals(nodeName)) {
                                NAME = xmlPullParser.nextText();
                            } else if ("FPATH".equals(nodeName)) {
                                FPATH = xmlPullParser.nextText();
                            }
                        }
                        break;
                        //完成解析某个节点
                        case END_TAG: {
                            if ("Function".equals(nodeName)) {
                                Log.e("okhttp: " + TAG, Status + ": " + NAME + ": " + FPATH);
                            }
                        }
                        break;
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                if (!FPATH.equals("")) {
                    String orderPath="";
                    String temp[] = FPATH.split("\\\\");
                    //orderPath=FPATH.substring(FPATH.lastIndexOf("\\\\"));
                    orderPath=temp[temp.length-1];
                    Log.e("okhttp: " + TAG, "orderPath: " + orderPath);
                    new pictureServer(myActivity,orderPath).execute();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}