package org.easydarwin.easyplayer.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.easydarwin.easyplayer.entity.Const;
import org.easydarwin.easyplayer.entity.Video;

import java.lang.ref.WeakReference;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:bysd-2
 * Time:2021/12/2813:40
 */

public class getFilesServer extends AsyncTask<Void, Void, Void> {
    private WeakReference<Activity> mWeakReference;
    private final String TAG = getFilesServer.class.getSimpleName();
    private Activity myActivity;
    private List<Video> list;
    private Video video;
    public getFilesServer(Activity activity){
        mWeakReference = new WeakReference<>(activity);
        myActivity = mWeakReference.get();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .get()
                    .url(Const.chakanSD_file_url)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("okhttp:"+TAG,"请求成功");
                //loadResourcePage(response.body().toString());
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
    }
    /*private void loadResourcePage(String param1String){
        int i;
        XmlPullParser xmlPullParser;
        if (list == null) {
            list=new ArrayList();
        } else {
            list.clear();
        }
        if (param1String != null && !param1String.trim().equals("")) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(param1String.getBytes());
            xmlPullParser = Xml.newPullParser();
            try {
                xmlPullParser.setInput(byteArrayInputStream, "UTF-8");
                i = xmlPullParser.getEventType();
                byteArrayInputStream = null;
            } catch (Exception exception) {
                exception.printStackTrace();
                return;
            }
        } else {
            return;
        }
        while (true) {
            if (i != 1) {
                Exception exception1;
                String str;
                switch (i) {
                    case 2:
                        str = xmlPullParser.getName();
                        if ("File".equals(str)) {
                            StringBuffer stringBuffer = new StringBuffer();
                            //this.access$1702(this, new Video());
                        } else if ("NAME".equals(str)) {
                            String str1 = null;
                            try {
                                str1 = xmlPullParser.nextText();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            video.setName(str1);
                            exception.append(" " + str1 + " ");
                            str1 = str1.split("\\.")[0];
                            String str2 = str1.substring(str1.length() - 1);
                            Log.i("ResGroupFragment", str1 + "last" + str2);
                            if (!str2.equals("V")) {
                                video.setLittleVideo(false);
                            } else {
                                video.setLittleVideo(true);
                            }
                            if (str2.equals("A")) {
                                this.video.doubleVideoType = 1;
                                exception1 = exception;
                            } else {
                                exception1 = exception;
                                if (str2.equals("B")) {
                                    video.doubleVideoType = 2;
                                    exception1 = exception;
                                }
                            }
                        } else if ("FPATH".equals(str)) {
                            String str1 = xmlPullParser.nextText();
                            video.setFpath(str1);
                            exception.append(" " + str1 + " ");
                            exception1 = exception;
                        } else if ("SIZE".equals(str)) {
                            String str1 = xmlPullParser.nextText();
                            ResGroupFragment.this.video.setSize(str1);
                            exception.append(" " + str1 + " ");
                            exception1 = exception;
                        } else if ("TIMECODE".equals(str)) {
                            String str1 = xmlPullParser.nextText();
                            ResGroupFragment.this.video.setTimecode(str1);
                            exception.append(" " + str1 + " ");
                            exception1 = exception;
                        } else if ("TIME".equals(str)) {
                            String str1 = xmlPullParser.nextText();
                            ResGroupFragment.this.video.setTime(str1);
                            String str2 = ResGroupFragment.this.paserTimeToYM(str1);
                            ResGroupFragment.this.video.setDate(str2);
                            exception.append(" " + str1 + " ");
                            exception1 = exception;
                        } else {
                            exception1 = exception;
                            if ("ATTR".equals(str)) {
                                String str1 = xmlPullParser.nextText();
                                ResGroupFragment.this.video.setAttr(str1);
                                exception.append(" " + str1 + " ");
                                ResGroupFragment.this.list.add(ResGroupFragment.this.video);
                                exception1 = exception;
                            }
                        }
                        exception2 = exception1;
                        if ("Status".equals(str)) {
                            exception2 = exception1;
                            if (xmlPullParser.nextText() != Const.Success) {
                                ResGroupFragment.this.refreshEnd();
                                return;
                            }
                            // Byte code: goto -> 81
                        }
                        // Byte code: goto -> 81
                    case 3:
                        exception2 = exception;
                        if ("File".equals(xmlPullParser.getName())) {
                            Iterator<Item> iterator = ResGroupFragment.this.list.iterator();
                            while (true) {
                                exception2 = exception;
                                if (iterator.hasNext()) {
                                    if (((Item)iterator.next()).getName().contains(".JPG"));
                                    continue;
                                }
                                i = xmlPullParser.next();
                                exception = exception2;
                                break;
                            }
                            continue;
                        }
                        // Byte code: goto -> 81
                }
                Exception exception2 = exception;
                // Byte code: goto -> 81
            }
            ResGroupFragment.this.refreshEnd();
            if (ResGroupFragment.this.videoList == null) {
                ResGroupFragment.access$602(ResGroupFragment.this, new ArrayList());
            } else {
                ResGroupFragment.this.videoList.clear();
            }
            if (ResGroupFragment.this.lockVideoList == null) {
                ResGroupFragment.access$1902(ResGroupFragment.this, new ArrayList());
            } else {
                ResGroupFragment.this.lockVideoList.clear();
            }
            if (ResGroupFragment.this.littleVideolist == null) {
                ResGroupFragment.access$2002(ResGroupFragment.this, new ArrayList());
            } else {
                ResGroupFragment.this.littleVideolist.clear();
            }
            if (ResGroupFragment.this.imgList == null) {
                ResGroupFragment.access$1202(ResGroupFragment.this, new ArrayList());
            } else {
                ResGroupFragment.this.imgList.clear();
            }
            (DeviceSingleton.getSingleton()).pictureNamesList.clear();
            for (Item item : ResGroupFragment.this.list) {
                if (item.getName().contains(".MOV") || item.getName().contains(".MP4")) {
                    if (((Video)item).isLittleVideo()) {
                        ResGroupFragment.this.littleVideolist.add((Video)item);
                        continue;
                    }
                    if (item.getFpath().indexOf("EVENT") >= 0) {
                        ResGroupFragment.this.lockVideoList.add((Video)item);
                        continue;
                    }
                    ResGroupFragment.this.videoList.add((Video)item);
                    continue;
                }
                ResGroupFragment.this.imgList.add(new Photo(item));
            }
            Collections.sort(ResGroupFragment.this.videoList, (Comparator<?>)new YMComparator());
            Collections.reverse(ResGroupFragment.this.videoList);
            ListIterator<Video> listIterator = ResGroupFragment.this.videoList.listIterator();
            while (listIterator.hasNext()) {
                Video video = listIterator.next();
                String str = video.getDate();
                if (!ResGroupFragment.this.sectionMap.containsKey(str)) {
                    video.setSection(ResGroupFragment.section);
                    ResGroupFragment.this.sectionMap.put(str, Integer.valueOf(ResGroupFragment.section));
                    ResGroupFragment.access$2208();
                    continue;
                }
                video.setSection(((Integer)ResGroupFragment.this.sectionMap.get(str)).intValue());
            }
            if (ResGroupFragment.this.videoList.size() > 0) {
                if (ResGroupFragment.this.mVideoAdapter != null)
                    ResGroupFragment.this.mVideoAdapter.removeAllMessage();
                ResGroupFragment.access$102(ResGroupFragment.this, new RecVideoGridAdapter((Context)ResGroupFragment.this.getActivity(), ResGroupFragment.this.videoList, ResGroupFragment.this.mGridView, ResGroupFragment.this.itemCallBack));
                if (ResGroupFragment.this.littleVideolist != null)
                    ResGroupFragment.this.mVideoAdapter.littleVideos = ResGroupFragment.this.littleVideolist;
                ResGroupFragment.this.mGridView.setAdapter((ListAdapter)ResGroupFragment.this.mVideoAdapter);
            } else {
                Toast.makeText((Context)ResGroupFragment.this.getActivity(), ResGroupFragment.this.getResources().getString(2131099702), 0).show();
            }
            if (ResGroupFragment.this.videoList.size() > 0 && ResGroupFragment.this.curPosition == 0 && !ResGroupFragment.this.isEditMode)
                ResGroupFragment.this.mImg_right.setVisibility(0);
            if (ResGroupFragment.this.mDownloadFileService != null)
                ResGroupFragment.this.mVideoAdapter.setDownLoadService(ResGroupFragment.this.mDownloadFileService);
            ResGroupFragment.access$2602(ResGroupFragment.this, System.currentTimeMillis());
            return;
        }
    }*/
}