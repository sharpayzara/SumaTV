package com.ddm.live.constants;

/**
 * Created by cxx on 2016/8/29.
 */
public class Constants {

    public static final int MSG_TYPE_SYSTEM = -1;
    public static final int MSG_TYPE_NORMAL = 0;
    public static final int MSG_TYPE_JOIN = 1;
    public static final int MSG_TYPE_QUIT = 2;
    public static final int MSG_TYPE_FOCUS = 3;
    public static final int MSG_TYPE_DANMU = 4;
    public static final int MSG_TYPE_LIGHT = 5;
    public static final int MSG_TYPE_GIFT = 6;
    public static final int MSG_TYPE_PAUSE = 8;//主播按Home键
    public static final int MSG_TYPE_REBACK=9;//主播返回
    public static final int REQUEST_CODE_READ_WRITE=100;//请求读写权限
    public static final int REQUEST_CODE_CAMERA_AUDIO = 101;//请求相机和录音权限
    public static final int REQUEST_CODE_READ_CAMERA=102;//请求读取与相机权限
    public static final int REQUEST_CODE_LOCATION = 103;//请求定位权限
    public static final String MSG_FOCUS="关注了主播";
    public static final String MSG_JOIN="来了";
    public static final String MSG_QUIT="退出房间";
    public static final String MSG_LIHGT="我点亮了";
    public static final String SHARE_TITLE="小主直播，看见你的美";
    public static final int DANMU_ACCOUNT_ID=10;//弹幕购买账户
    public static final String PRESENT_GIFT_MASTER_DESC="赠送给您";
    public static final String PRESENT_GIFT_AUDIENCE_DESC="赠送给主播";
    /**
     * 正式环境
     */
    public static final String CLIENT_ID = "liveapi_id";
    public static final String CLIENT_SECRETE = "liveapi_secret";
    public static final String SERVER_URL = "http://liveapi.9ddm.com/api/";
    public static final String SHARE_SERVER_URL = "http://liveapi.9ddm.com/api/share/";

    /**
     * 测试环境
     */
//    public static final String CLIENT_ID = "client_id";
//    public static final String CLIENT_SECRETE = "client_secret";
//    public static final String SERVER_URL = "http://t.9ddm.com/api/";
//    public static final String SHARE_SERVER_URL = "http://t.9ddm.com/api/share/";


    /**
     * wsheng环境
     */
//    public static final String CLIENT_ID="client_id";
//    public static final String CLIENT_SECRETE="client_secret";
//    public static final String SERVER_URL="http://172.16.40.19:8000/api/";

    public static final String APP_ID = "wx81ec8ff55666bb9b";

    public static final String FAILED_CONNECT = "Failed to connect to ";
}
