package org.easydarwin.easyplayer.entity;

import android.os.Environment;

/**
 * Author:bysd-2
 * Time:2021/12/2717:35
 */

public class Const {
    public static final String ALBUM_PATH;

    public static String AllmenuItem;

    public static String Auto_Power;

    public static String Auto_Recode;

    public static final String CACHE_ADV_PATH;

    public static final String CACHE_MAINPAGE_PATH;

    public static final String CLIP_PATH;

    public static String CONTINUE_SHOT_BURST;

    public static final String ClipVideo_PATH = "clipVideo";

    public static String Current_Mode;

    public static final int DELETE_FILE = 1;

    public static final int DOWNLOAD_CANCEL = 200;

    public static final int DOWNLOAD_COMPLETE = 150;

    public static final int DOWNLOAD_OVER = 100;

    public static final String FINISHALLACTIVITY_STRING = "finishAllActivity";

    public static String Firmware_update;

    public static String Get_hardware_capacity;

    public static final String LENOVOMALL_STRING = "https://lianxiangchepin.tmall.com/index.htm";

    public static final String LOCKEDVIDEO = "lockedVideo";

    public static String MOVIE_CYCLICREC_CUSTOM;

    public static String MOVIE_MOTIONDET_TIME_CUSTOM;

    public static String MOVIE_TIMELAPSEREC;

    public static String Movie_max_record_time;

    public static final String NETTYPEKE_STRING = "netTypeKey";

    public static final String NOTIFICATION_STRING = "notification_data";

    public static final String NotificationUrlKey = "noticifation_url";

    public static final String PARKDETECTION_STRING = "parkDetection";

    public static final int PHOTO_CHANGE = 2;

    public static final String PICTURE_STRING = "pictureChange";

    public static final String PUBLICLING_STRING = "http://www.huichewang.com";

    public static final String PUBLICMALL_STRING = "https://huichewang.tmall.com";

    public static String QueryStatus;

    public static final String Record_State = "recordState";

    public static String Rotate_Photos;

    public static String SELFTIMER_CUSTOM;

    public static String SETTING_COLOROPTIONS;

    public static String SETTING_CONTRAST;

    public static String SETTING_EXPOSURE;

    public static String SETTING_FOV;

    public static String SETTING_SATURATION;

    public static String SETTING_TV_ACUITY;

    public static String SETTING_TV_VIDEO_OUT;

    public static String SETTING_WHITEBALANCE;

    public static final String SOCKETERROR_INFO_STRING = "socketErrorInfo";

    public static final String STOREPICTURESUCCESS_STRING = "storePicSuccess";

    public static final String STORE_DIRECTORY;

    public static String Setting_DateFormat;

    public static String Setting_Luauage;

    public static String Setting_PL;

    public static String Success;

    public static final String TEMP_VIDEO;

    public static String TV_STATUS;

    public static int ToogleNetMessage = 0;

    public static final String UPLOADFILE_RESULT_STRING = "uploadFileResult";

    public static final String VIDEO_PATH;

    public static String WIFIAPP_CMD_VINLOGO_CUSTOM;

    public static final String WifiNameKey = "wifiNameKey";

    public static String captrue;

    public static String captrueSize;

    public static String captureNumber;

    public static String captureSize;

    public static String chakanSD_file_url;

    public static String checkVersionInfo;

    public static String cyclicRecord;

    public static String deleteOne;

    public static String deviceVersion;

    public static String fileList;

    public static String firmwareUpdate;

    public static String format;

    public static String getBattery;

    public static String getCard;

    public static String getDiskFree;

    public static String getDownload;

    public static String getHardWare;

    public static String getScreen;

    public static String getThurmbnail;

    public static String getUpdate;

    public static String getVersion;

    public static String get_fdisk_free_space;

    public static String get_thumbnail;

    public static String heartBeat;

    public static String heartBeat3036;

    public static String host;

    public static String hostString;

    public static String language;

    public static String motionDetection;

    public static String movieAudio;

    public static String movieDateInPrint;

    public static String movieEV;

    public static String movieGSensor;

    public static String movieHDR;

    public static String movieLive;

    public static String movieLiveView;

    public static String movieLiveViewStart;

    public static String movieMax;

    public static String movieRecord;

    public static String movieRecordBitrate;

    public static String movieRecordingTime;

    public static String movieVideoModeOff;

    public static String movieVideoModeOn;

    public static String movie_sensitivity;

    public static String nail;

    public static String parkDetection;

    public static String photoModeChange;

    public static String powerOff;

    public static String queryCurrent;

    public static String reconnectWifi;

    public static String recorder_command;

    public static String removeLast;

    public static String saveMenu;

    public static String setAutoRecording;

    public static String setDate;

    public static String setPassphrase;

    public static String setSSID;

    public static String setTime;

    public static String setVideoSize;

    public static int showShoppingMallLimit = 10;

    public static String supportCommand;

    public static String systemReset;

    public static String toogleCapture;

    public static String tvFormat;

    public static String url;

    public static String versionCheck;

    public static String videoModeChange;

    static {
        ToogleNetMessage = 100;
        Success = "0";
        recorder_command = "http://192.168.1.254/?custom=1&cmd=2001&par=";
        chakanSD_file_url = "http://192.168.1.254/?custom=1&cmd=3015";
        get_fdisk_free_space = "http://192.168.1.254/?custom=1&cmd=3017";
        movie_sensitivity = "http://192.168.1.254/?custom=1&cmd=2011&par=2";
        Firmware_update = "http://192.168.1.254/?custom=1&cmd=3013";
        get_thumbnail = "http://192.168.1.254/CARDV/MOVIE/2016_0101_184236_033.MOV?custom=1&cmd=4001";
        Get_hardware_capacity = "http://192.168.1.254/?custom=1&cmd=3022";
        Movie_max_record_time = "http://192.168.1.254/?custom=1&cmd=2009";
        checkVersionInfo = "http://www.huichewang.com/apk/version.jsp?platform=";
        url = "http://www.huichewang.com/api.do";
        host = "http://192.168.1.254/?";
        hostString = "http://192.168.1.254";
        nail = "http://192.168.1.254/NOVATEK/MOVIE/2014_0321_011922_002.MOV?";
        captrue = host + "custom=1&cmd=1001";
        captrueSize = host + "custom=1&cmd=1002&par=";
        captureNumber = host + "custom=1&cmd=1003";
        movieRecord = host + "custom=1&cmd=1001&par=1";
        captureSize = host + "custom=1&cmd=1002&par=1";
        setVideoSize = nail + "custom=1&cmd=2002&par=";
        cyclicRecord = host + "custom=1&cmd=2003&par=";
        movieHDR = host + "custom=1&cmd=2004&par=0";
        movieEV = host + "custom=1&cmd=2005&par=6";
        motionDetection = host + "custom=1&cmd=2006&par=";
        movieAudio = host + "custom=1&cmd=8804&par=";
        movieDateInPrint = host + "custom=1&cmd=2008&par=1";
        movieMax = host + "custom=1&cmd=2009";
        movieLive = host + "custom=1&cmd=2010&par=1";
        movieGSensor = host + "custom=1&cmd=2011&par=";
        parkDetection = host + "custom=1&cmd=3038&par=";
        setAutoRecording = host + "custom=1&cmd=2012&par=1";
        movieRecordBitrate = host + "custom=1&cmd=2013&str=400";
        movieLiveView = host + "custom=1&cmd=2014&str=300";
        movieLiveViewStart = host + "custom=1&cmd=2015&par=1";
        movieRecordingTime = host + "custom=1&cmd=2016";
        photoModeChange = host + "custom=1&cmd=3001&par=0";
        videoModeChange = host + "custom=1&cmd=3001&par=1";
        movieVideoModeOn = host + "custom=1&cmd=2006&par=1";
        movieVideoModeOff = host + "custom=1&cmd=2006&par=0";
        getBattery = host + "custom=1&cmd=3019";
        QueryStatus = host + "custom=1&cmd=3002";
        setSSID = host + "custom=1&cmd=3003&str=";
        setPassphrase = host + "custom=1&cmd=3004&str=";
        setDate = host + "custom=1&cmd=3005&str=";
        setTime = host + "custom=1&cmd=3006&str=";
        powerOff = host + "custom=1&cmd=3007&par=1";
        language = host + "custom=1&cmd=3008&par=1";
        tvFormat = host + "custom=1&cmd=3009&par=1";
        format = host + "custom=1&cmd=3010&par=1";
        systemReset = host + "custom=1&cmd=3011";
        getVersion = host + "custom=1&cmd=3012";
        firmwareUpdate = host + "custom=1&cmd=3013";
        queryCurrent = host + "custom=1&cmd=3014";
        fileList = host + "custom=1&cmd=3015";
        heartBeat = host + "custom=1&cmd=3016";
        versionCheck = "http://v2.huichewang.com/apk/version.jsp";
        getDiskFree = host + "custom=1&cmd=3017";
        reconnectWifi = host + "custom=1&cmd=3018";
        saveMenu = host + "custom=1&cmd=3021";
        getHardWare = host + "custom=1&cmd=3022";
        removeLast = host + "custom=1&cmd=3023";
        getCard = host + "custom=1&cmd=3024";
        getDownload = host + "custom=1&cmd=3025";
        getUpdate = host + "custom=1&cmd=3026";
        getThurmbnail = nail + "custom=1&cmd=4001";
        getScreen = nail + "custom=1&cmd=4002";
        heartBeat3036 = host + "custom=1&cmd=3036";
        supportCommand = host + "custom=1&cmd=3002";
        toogleCapture = host + "custom=1&cmd=3028&par=";
        deviceVersion = host + "custom=1&cmd=3012";
        AllmenuItem = host + "custom=1&cmd=3031&str=all";
        Rotate_Photos = host + "custom=1&cmd=8805&par=";
        TV_STATUS = host + "custom=1&cmd=8807&par=";
        Auto_Power = host + "custom=1&cmd=3007&par=";
        Current_Mode = host + "custom=1&cmd=8826";
        Auto_Recode = host + "custom=1&cmd=8804&par=";
        Setting_Luauage = host + "custom=1&cmd=3008&par=";
        Setting_PL = host + "custom=1&cmd=8114&par=";
        Setting_DateFormat = host + "custom=1&cmd=8806&par=";
        SETTING_TV_VIDEO_OUT = host + "custom=1&cmd=3009&par=";
        SETTING_TV_ACUITY = host + "custom=1&cmd=8813&par=";
        SETTING_FOV = host + "custom=1&cmd=8827&par=";
        SETTING_EXPOSURE = host + "custom=1&cmd=2005&par=";
        SETTING_SATURATION = host + "custom=1&cmd=8814&par=";
        SETTING_CONTRAST = host + "custom=1&cmd=8825&par=";
        SETTING_COLOROPTIONS = host + "custom=1&cmd=8815&par=";
        SETTING_WHITEBALANCE = host + "custom=1&cmd=8816&par=";
        SELFTIMER_CUSTOM = host + "custom=1&cmd=8822&par=";
        CONTINUE_SHOT_BURST = host + "custom=1&cmd=8821&par=";
        MOVIE_TIMELAPSEREC = host + "custom=1&cmd=8818&par=";
        WIFIAPP_CMD_VINLOGO_CUSTOM = host + "custom=1&cmd=8803&str=";
        MOVIE_CYCLICREC_CUSTOM = host + "custom=1&cmd=8819&par=";
        MOVIE_MOTIONDET_TIME_CUSTOM = host + "custom=1&cmd=8820&par=";
        deleteOne = hostString;
        STORE_DIRECTORY = Environment.getExternalStorageDirectory() + "/" + VersionInfo.directoryPath() + "/";
        ALBUM_PATH = STORE_DIRECTORY + "PHOTO/";
        VIDEO_PATH = STORE_DIRECTORY + "VIDEO/";
        CLIP_PATH = STORE_DIRECTORY + "Clip/";
        TEMP_VIDEO = STORE_DIRECTORY + "TemVideo/";
        CACHE_MAINPAGE_PATH = STORE_DIRECTORY + "cache/mainpage/";
        CACHE_ADV_PATH = STORE_DIRECTORY + "cache/adv/";
    }

}
