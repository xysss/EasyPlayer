package org.easydarwin.easyplayer.activity;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.data.VideoSource;
import org.easydarwin.easyplayer.databinding.ActivityMainBinding;
import org.easydarwin.easyplayer.entity.Const;
import org.easydarwin.easyplayer.entity.LocalVideo;
import org.easydarwin.easyplayer.entity.Photo;
import org.easydarwin.easyplayer.fragments.ImageFragment;
import org.easydarwin.easyplayer.fragments.PlayFragment;
import org.easydarwin.easyplayer.http.photoModeChange;
import org.easydarwin.easyplayer.util.FileUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ?????????
 */
public class PlayActivity extends AppCompatActivity implements PlayFragment.OnDoubleTapListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0x111;

    private PlayFragment mRenderFragment;

    private SoundPool mSoundPool;
    private String url;

    private int mTalkPictureSound;
    //    private int mActionStartSound;
//    private int mActionStopSound;
    private float mAudioVolumn;
//    private float mMaxVolume;

    private ActivityMainBinding mBinding;

    private long mLastReceivedLength;

    private final Handler mHandler = new Handler();

    // ?????????
    private ExecutorService mThreadPool;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ServerSocket serverSocket = null;
    private Socket mSocket;
    private Boolean isSocketConnect=false;

    private final Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            long length = mRenderFragment.getReceivedStreamLength();

            if (length == 0) {
                mLastReceivedLength = 0;
            }

            if (length < mLastReceivedLength) {
                mLastReceivedLength = 0;
            }

            mBinding.streamBps.setText((length - mLastReceivedLength) / 1024 + "Kbps");
            mLastReceivedLength = length;

            mHandler.postDelayed(this, 1000);
        }
    };

    private Runnable mResetRecordStateRunnable = new Runnable() {
        @Override
        public void run() {
            ImageView mPlayAudio = mBinding.liveVideoBarRecord;
            mPlayAudio.setImageState(new int[]{}, true);
            mPlayAudio.removeCallbacks(mResetRecordStateRunnable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        url = getIntent().getStringExtra("play_url");
        int transportMode = getIntent().getIntExtra(VideoSource.TRANSPORT_MODE, 0);
        int sendOption = getIntent().getIntExtra(VideoSource.SEND_OPTION, 0);
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        // ??????????????????
        mThreadPool = Executors.newCachedThreadPool();

        //?????????Socket??????
        startServerSocket();

        // ???????????????????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (savedInstanceState == null) {
            ResultReceiver rr = getIntent().getParcelableExtra("rr");

            if (rr == null) {
                rr = new ResultReceiver(new Handler()) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        super.onReceiveResult(resultCode, resultData);

                        if (resultCode == PlayFragment.RESULT_REND_START) {
                            onPlayStart();
                        } else if (resultCode == PlayFragment.RESULT_REND_STOP) {
                            onPlayStop();
                        } else if (resultCode == PlayFragment.RESULT_REND_VIDEO_DISPLAY) {
                            onVideoDisplayed();
                        }
                    }
                };
            }

            PlayFragment fragment = PlayFragment.newInstance(url, transportMode, sendOption, rr);
            fragment.setOnDoubleTapListener(this);

            getSupportFragmentManager().beginTransaction().add(R.id.render_holder, fragment).commit();
            mRenderFragment = fragment;
        } else {
            mRenderFragment = (PlayFragment) getSupportFragmentManager().findFragmentById(R.id.render_holder);
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initSoundPool();

        mBinding.playerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSingleTab(null);
            }
        });

        mBinding.toolbarTitle.setText("????????????");
        //mBinding.toolbarTitle.setText(url);
        mBinding.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });

        mBinding.liveVideoBarEnableAudio.setEnabled(false);
        mBinding.liveVideoBarTakePicture.setEnabled(false);
        mBinding.liveVideoBarRecord.setEnabled(false);

        // ??????TextView?????????
        mBinding.msgTxt.setMovementMethod(new ScrollingMovementMethod());

        if (isLandscape()) {
            landscape();
        } else {
            vertical();
        }


    }

    @Override
    protected void onDestroy() {
        if (dis!=null)
            dis=null;
        if (dos!=null)
            dos=null;
        if (serverSocket!=null)
            serverSocket=null;

        releaseSoundPool();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE + 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.

                    if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
                        onTakePicture(mBinding.liveVideoBarTakePicture);
                    } else {
                        onRecordOrStop(mBinding.liveVideoBarRecord);
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }

                return;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            landscape();
        } else {
            vertical();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return true;
        //return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (isLandscape()) {
            finish();
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//??????
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* ====================== OnDoubleTapListener ====================== */

    @Override
    public void onDoubleTab(PlayFragment f) {

    }

    @Override
    public void onSingleTab(PlayFragment f) {
        /*if (mBinding.toolbar.getVisibility() == View.GONE) {
            mBinding.toolbar.setVisibility(View.VISIBLE);
        } else {
            mBinding.toolbar.setVisibility(View.GONE);
        }*/

        if (mBinding.liveVideoBar.getVisibility() == View.GONE) {
            mBinding.liveVideoBar.setVisibility(View.VISIBLE);
        } else {
            mBinding.liveVideoBar.setVisibility(View.GONE);
        }
    }

    /* ====================== private method ====================== */

    private void requestWriteStorage(final boolean toTakePicture) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

            new AlertDialog.Builder(this).setMessage(toTakePicture ? "EasyPlayer????????????????????????????????????" : "EasyPlayer????????????????????????????????????").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(PlayActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE + (toTakePicture ? 0 : 1));
                }
            }).show();
        } else {
            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE + (toTakePicture ? 0 : 1));

            // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    // ????????????
    private boolean isLandscape() {
        int orientation = getResources().getConfiguration().orientation;
        return orientation == ORIENTATION_LANDSCAPE;
    }

    // ??????
    private void landscape() {
        mBinding.enterFullscreen.setSelected(true);

        LinearLayout container = mBinding.playerContainer;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setNavVisibility(false);

        // ???????????????,????????????????????????
        container.setOrientation(LinearLayout.HORIZONTAL);
        mBinding.renderFl.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mBinding.renderFl.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        mBinding.renderHolder.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mBinding.renderHolder.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        mRenderFragment.enterFullscreen();
    }

    // ??????
    private void vertical() {
        mBinding.enterFullscreen.setSelected(false);

        LinearLayout container = mBinding.playerContainer;

        // ??????,??????????????????
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setNavVisibility(true);

        // ???????????????,????????????????????????
        container.setOrientation(LinearLayout.VERTICAL);
        mBinding.renderFl.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.render_wnd_height);
        mBinding.renderFl.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        mBinding.renderHolder.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        mBinding.renderHolder.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        mRenderFragment.quiteFullscreen();
    }

    public void setNavVisibility(boolean visible) {
        if (visible) {
            mBinding.toolbar.setVisibility(View.VISIBLE);
        } else {
            mBinding.toolbar.setVisibility(View.GONE);
        }

//        if (!ViewConfiguration.get(this).hasPermanentMenuKey()) {
//            int newVis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//
//            if (!visible) {
//                // } else {
//                // newVis &= ~(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//                newVis |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
//            }
//
//            // If we are now visible, schedule a timer for us to go invisible.
//            // Set the new desired visibility.
//            getWindow().getDecorView().setSystemUiVisibility(newVis);
//        }
    }

    /* ====================== ?????? ====================== */

    protected void initSoundPool() {
        if (true)
            return;

        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioVolumn = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        mMaxVolume = (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            mSoundPool = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(attributes).build();
        } else {
            mSoundPool = new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 0);
        }

        mTalkPictureSound = mSoundPool.load("/system/media/audio/ui/camera_click.ogg", 1);
//        mActionStartSound = mSoundPool.load(this, R.raw.action_start, 1);
//        mActionStopSound = mSoundPool.load(this, R.raw.action_stop, 1);
    }

    protected void releaseSoundPool() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    /* ====================== ???????????? ====================== */

    private void onVideoDisplayed() {
        mBinding.liveVideoBarTakePicture.setEnabled(true);
        mBinding.liveVideoBarRecord.setEnabled(true);
        mBinding.msgTxt.append(String.format("[%s]\t%s\n", new SimpleDateFormat("HH:mm:ss").format(new Date()), "?????????"));
    }

    private void onPlayStart() {
        boolean enable = mRenderFragment.isAudioEnable();
        mBinding.liveVideoBarEnableAudio.setImageState(enable ? new int[]{android.R.attr.state_pressed} : new int[]{}, true);
        mBinding.liveVideoBarEnableAudio.setEnabled(true);
        mHandler.postDelayed(mTimerRunnable, 1000);

        mBinding.liveVideoBarTakePicture.setEnabled(false);
        mBinding.liveVideoBarRecord.setEnabled(false);
    }

    private void onPlayStop() {
        mBinding.liveVideoBarEnableAudio.setEnabled(false);
        mHandler.removeCallbacks(mTimerRunnable);
    }

    /* ====================== ???????????? ====================== */

    // ????????????
    public void onOpenFileDirectory(View view) {
        Intent i = new Intent(this, MediaFilesActivity.class);
        i.putExtra("play_url", url);
        startActivity(i);
    }

    /**
     * ????????????Socket???????????????
     */
    private void startReader() {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dis = new DataInputStream(mSocket.getInputStream());
                    while(true){
                        String msgRecv = dis.readUTF();
                        Log.e("Socket","msg from client:"+msgRecv);
                        dos.writeUTF(msgRecv);
                        dos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //??????socket???????????????????????????
    private void serverSendMessage(final String mServerSendMessage) {
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dos = new DataOutputStream(mSocket.getOutputStream());
                    dos.writeUTF(mServerSendMessage);
                    dos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //????????????
    private void startServerSocket(){
        Toast.makeText(PlayActivity.this,"?????????????????????", Toast.LENGTH_SHORT).show();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // ??????ServerSocket
                    serverSocket = new ServerSocket(5008);
                    InetAddress address = InetAddress.getLocalHost();
                    Log.e("Socket","--?????????????????????????????? :"+address);
                    // ????????????????????????????????????
                    while (true) {
                        mSocket = serverSocket.accept(); //?????????????????????
                        Log.e("Socket","????????????????????????" + mSocket.getInetAddress()+":"+mSocket.getLocalPort());
                        isSocketConnect=true;
                        startReader();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    isSocketConnect=false;
                }
            }
        });
    }

    // ??????/????????????
    public void onEnableOrDisablePlayAudio(View view) {
        boolean enable = mRenderFragment.toggleAudioEnable();
        ImageView mPlayAudio = (ImageView) view;
        mPlayAudio.setImageState(enable ? new int[]{android.R.attr.state_pressed} : new int[]{}, true);
    }

    // ??????
    public void onTakePicture(View view) {
        /*int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mRenderFragment.takePicture(FileUtil.getPictureName(url).getPath());

            if (mSoundPool != null) {
                mSoundPool.play(mTalkPictureSound, mAudioVolumn, mAudioVolumn, 1, 0, 1.0f);
            }
        } else {
            requestWriteStorage(true);
        }*/
        FileUtil.getPictureName("rtsp://192.168.1.254").getPath();
        new photoModeChange(PlayActivity.this).execute();
        //????????????
        //new downFileServer(PlayActivity.this).execute();
        //new getFilesServer(this).execute();
        //getPictureFiles();
        //addFileFromDire(Const.VIDEO_PATH);
    }

    public void socketLU(View view) {
        if (isSocketConnect){
            serverSendMessage("LU");
            Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            //startServerSocket();
        }
    }

    public void socketLD(View view) {
        if (isSocketConnect){
            serverSendMessage("LD");
            Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            //startServerSocket();
        }
    }

    private void getPictureFiles(){
        List<Photo> imgList = new ArrayList<Photo>();
        int i = 0;
        String[] arrayOfString = (new File(Const.ALBUM_PATH)).list();
        Log.e("okhttp", "Const.ALBUM_PATH:" +Const.ALBUM_PATH);
        if (arrayOfString != null && arrayOfString.length > 0) {
            int j = arrayOfString.length;
            while (i < j) {
                String str = arrayOfString[i];
                if (str != null && (str.endsWith(".JPG") || str.endsWith(".jpg"))) {
                    Photo photo = new Photo();
                    photo.setName(str);
                    photo.setDate(getImgDate(str));
                    imgList.add(photo);
                    Log.e("okhttp", "getName:"+photo.getName());
                }
                i++;
            }
        }
    }


    private void addFileFromDire(String paramString) {
        Log.e("okhttp","paramString:"+paramString);
        List<LocalVideo> localVideoList=new ArrayList<>();
        String[] videoArrayOfString = (new File(paramString)).list();
        if (videoArrayOfString != null && videoArrayOfString.length > 0) {
            int j = videoArrayOfString.length;
            for (int i = 0; i < j; i++) {
                String str = videoArrayOfString[i];
                if (str != null && str.endsWith(".MOV")) {
                    LocalVideo localVideo = new LocalVideo();
                    localVideo.setName(str);
                    localVideo.setFpath(paramString + str);
                    localVideo.setDate(getImgDate(str));
                    Log.e("okhttp","localVideo.Fpath:"+localVideo.getFpath());
                    if (str.length() >= 16)
                        if (str.indexOf("KP") >= 0) {
                            str = str.substring(4);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            try {
                                localVideo.setBegainDate(simpleDateFormat.parse(str));
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        } else {
                            String str1 = str.substring(0, 16);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MMdd_HHmmss");
                            try {
                                localVideo.setBegainDate(simpleDateFormat.parse(str1));
                            } catch (ParseException parseException1) {
                                parseException1.printStackTrace();
                            }
                        }
                    if (paramString.equals(Const.VIDEO_PATH)) {
                        localVideo.isClipVideo = false;
                    } else {
                        localVideo.isClipVideo = true;
                    }
                    localVideoList.add(localVideo);
                }
            }
        }
    }

    private String getImgDate(String paramString) {
        String str1 = "";
        String str2 = "";
        String str3 = "";
        if (TextUtils.isEmpty(paramString)) {
            str3 = "1970";
            str2 = "1";
            paramString = "1";
            return str3 + "/" + str2 + "/" + paramString;
        }
        try {
            str3 = paramString.substring(0, 4);
            str2 = paramString.substring(5, 7);
            paramString = paramString.substring(7, 9);
        } catch (Exception exception) {
            str3 = "1970";
            str2 = "1";
            str1 = "1";
        }
        return str3 + "/" + str2 + "/" + str1;
    }

    // ??????/????????????
    public void onRecordOrStop(View view) {
        //wife ??????
        //new videoModeChange(this).execute();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (mRenderFragment != null) {
                boolean recording = mRenderFragment.onRecordOrStop();
                //wife ??????
                /*ImageView mPlayAudio = (ImageView) view;
                if (recording) {
                    mPlayAudio.setImageState(recording ? new int[]{android.R.attr.state_checked} : new int[]{}, true);
                } else {
                    mPlayAudio.setImageState(recording ? new int[]{android.R.attr.state_checked} : new int[]{}, false);
                }*/
                ImageView mPlayAudio = (ImageView) view;
                mPlayAudio.setImageState(recording ? new int[]{android.R.attr.state_checked} : new int[]{}, true);

                if (recording)
                    mPlayAudio.postDelayed(mResetRecordStateRunnable, 200);
            }
        } else {
            requestWriteStorage(false);
        }
    }

    public void onTakePictureThumbClicked(View view) {
        String path = (String) view.getTag();
//        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "gallery_image_view");
//        Intent intent = new Intent(this, ImageActivity.class);
//        intent.putExtra("extra-uri", Uri.fromFile(new File(path)));
////        ActivityCompat.startActivity(this, intent, compat.toBundle());
//        startActivity(intent);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, ImageFragment.newInstance(Uri.fromFile(new File(path)))).addToBackStack(null).commit();
    }

    // ??????
    public void onFullscreen(View view) {
        /*ImageButton btn = (ImageButton) view;
        btn.setSelected(!btn.isSelected());

        if (btn.isSelected()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//??????
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//??????
        }*/

        Intent i = new Intent(this, MediaFilesActivity.class);
        i.putExtra("play_url", url);
        startActivity(i);

    }

    /* ====================== PlayFragment ====================== */

    /*
     * state???1???????????????2??????????????????3?????????????????????
     * */
    public void onEvent(PlayFragment playFragment, int state, int err, String msg) {
        mBinding.msgTxt.append(String.format("[%s]\t%s\t\n",
                new SimpleDateFormat("HH:mm:ss").format(new Date()),
                msg));
    }

    public void onRecordState(int status) {
        ImageView mPlayAudio = mBinding.liveVideoBarRecord;
        mPlayAudio.setImageState(status == 1 ? new int[]{android.R.attr.state_checked} : new int[]{}, true);
        mPlayAudio.removeCallbacks(mResetRecordStateRunnable);
    }

//    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
//
//    /**
//     * ??????????????????
//     */
//    public void onToggleOrientation() {
//        setRequestedOrientation(isLandscape() ?
//                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//    }
//
//    public boolean multiWindows() {
//        LinearLayout container = (LinearLayout) findViewById(R.id.player_container);
//        return container.getChildCount() > 1;
//    }

//    /**
//     * ???????????????????????????
//     */
//    public void onAddWindow() {
//        new AlertDialog.Builder(PlayActivity.this).setTitle("??????????????????").setItems(new CharSequence[]{"????????????????????????", "?????????????????????"}, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    final SharedPreferences preferences = getSharedPreferences("PlayListActivity", MODE_PRIVATE);
//                    JSONArray mArray;
//
//                    try {
//                        mArray = new JSONArray(preferences.getString("play_list", "[\"rtsp://121.41.73.249/1001_home.sdp\"]"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        mArray = new JSONArray();
//                    }
//
//                    final CharSequence[] array = new CharSequence[mArray.length()];
//
//                    if (array.length == 0) {
//                        Toast.makeText(PlayActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    for (int i = 0; i < array.length; i++) {
//                        array[i] = mArray.optString(i);
//                    }
//
//                    new AlertDialog.Builder(PlayActivity.this).setTitle("??????????????????").setItems(array, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            addVideoSource(String.valueOf(array[which]));
//                        }
//                    }).setNegativeButton(android.R.string.cancel, null).show();
//                } else {
//                    final EditText edit = new EditText(PlayActivity.this);
//                    final int hori = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
//                    final int verti = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
//                    edit.setPadding(hori, verti, hori, verti);
//
//                    final AlertDialog dlg = new AlertDialog.Builder(PlayActivity.this).setView(edit).setTitle("?????????????????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String url = String.valueOf(edit.getText());
//
//                            if (TextUtils.isEmpty(url)) {
//                                return;
//                            }
//
//                            final SharedPreferences preferences = getSharedPreferences("PlayListActivity", MODE_PRIVATE);
//                            JSONArray mArray;
//
//                            try {
//                                mArray = new JSONArray(preferences.getString("play_list", "[\"rtsp://121.41.73.249/1001_home.sdp\"]"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                mArray = new JSONArray();
//                            }
//
//                            mArray.put(url);
//                            preferences.edit().putString("play_list", String.valueOf(mArray)).apply();
//                            addVideoSource(url);
//                        }
//                    }).setNegativeButton("??????", null).create();
//                    dlg.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(DialogInterface dialogInterface) {
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
//                        }
//                    });
//                    dlg.show();
//                }
//            }
//        }).show();
//    }
//
//    /**
//     * ????????????????????????????????????PlayFragment????????????????????????,????????????????????????PlayFragment???
//     *
//     * @param url
//     */
//    private void addVideoSource(String url) {
//        final FrameLayout item = new FrameLayout(PlayActivity.this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        params.weight = 1;
//        item.setLayoutParams(params);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            item.setId(View.generateViewId());
//        } else {
//            item.setId(generateViewId());
//        }
//
//        mBinding.playerContainer.addView(item);
//        boolean useUDP = SPUtil.getUDPMode(this);
//        getSupportFragmentManager().beginTransaction().add(item.getId(), PlayFragment.newInstance(url, useUDP ? Client.TRANSTYPE_UDP : Client.TRANSTYPE_TCP, null)).commit();
//    }
//
//    /**
//     * ????????????????????????
//     *
//     * @param id
//     */
//    public void onRemoveVideoFragment(int id) {
//        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(id)).commit();
//        mBinding.playerContainer.removeView(mBinding.playerContainer.findViewById(id));
//    }
//
//    /**
//     * Generate a value suitable for use in View.setId(int)
//     * This value will not collide with ID values generated at build time by aapt for R.id.
//     *
//     * @return a generated ID value
//     */
//    public static int generateViewId() {
//        for (; ; ) {
//            final int result = sNextGeneratedId.get();
//            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
//            int newValue = result + 1;
//
//            if (newValue > 0x00FFFFFF)
//                newValue = 1; // Roll over to 1, not 0.
//
//            if (sNextGeneratedId.compareAndSet(result, newValue)) {
//                return result;
//            }
//        }
//    }
}
