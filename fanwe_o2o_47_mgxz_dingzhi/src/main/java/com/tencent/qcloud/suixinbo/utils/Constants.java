package com.tencent.qcloud.suixinbo.utils;


import com.tencent.av.sdk.AVRoomMulti;

/**
 * 静态函数
 */
public class Constants {
    public static final String BD_EXIT_APP = "bd_sxb_exit";

    public static final String USER_INFO = "user_info";

    public static final String USER_ID = "user_id";

    public static final String USER_SIG = "user_sig";

    public static final String USER_NICK = "user_nick";

    public static final String USER_SIGN = "user_sign";

    public static final String USER_AVATAR = "user_avatar";

    public static final String USER_ROOM_NUM = "user_room_num";

    public static final String LIVE_ANIMATOR = "live_animator";
    public static final String LOG_LEVEL = "log_level";

//    //
//    public static final int ACCOUNT_TYPE = 792;
//    //    //sdk appid 由腾讯分配
//    public static final int SDK_APPID = 1400001533;

//    public static final int SDK_APPID = 1400001692;
//
//    public static final int ACCOUNT_TYPE = 884;// 用户的账号类型

    public static final int SDK_APPID = 1400012005;//sdkappid,本应用的
//    public static final int SDK_APPID = 1252383683 ;//sdkappid,本应用的
//测试环境 账号。
   public static final int SDK_APPID_TEST = 1400018408;//sdkappid,本应用的


    public static final int ACCOUNT_TYPE = 6410;// 用户的账号类型
    //测试环境 账号类型。
    public static final int ACCOUNT_TYPE_Test = 8640;// 用户的账号类型
    public static final String ID_STATUS = "id_status";

    public static final int HOST = 1;

    public static final int MEMBER = 0;

    public static final int VIDEO_VIEW_MAX = 4;

    public static final int LOCATION_PERMISSION_REQ_CODE = 1;
    public static final int WRITE_PERMISSION_REQ_CODE = 2;


    public static final String APPLY_CHATROOM = "申请加入";

    public static final int IS_ALREADY_MEMBER = 10013;

    public static final int TEXT_TYPE = 0;
    public static final int MEMBER_ENTER = 1;
    public static final int MEMBER_EXIT = 2;
    public static final int HOST_LEAVE = 3;
    public static final int HOST_BACK = 4;

    public static final String ROOT_DIR = "/sdcard/Suixinbo/";

    /**
     * 自定义消息 类型开始
     */
    public static final int AVIMCMD_CUSTOM=0x100;
    public static final int AVIMCMD_RED_PACKET=AVIMCMD_CUSTOM+1;  //红包
    public static final int AVIMCMD_DANMU = AVIMCMD_RED_PACKET+1; //弹幕
    public static final int AVIMCOM_GIFT_SINGLE = AVIMCMD_DANMU+1; //小礼物 非连发
    public static final int AVIMCMD_REWARD = AVIMCMD_RED_PACKET+1; //弹幕
    public static final int AVIMCOM_GIFT = AVIMCMD_REWARD+1; //小礼物 非连发
    public static final int AVIMCOM_BIG_GIFT_SINGLE = AVIMCOM_GIFT+1; //大礼物 非连发
    public static final int AVIMCOM_BIG_GIFT_MULTI = AVIMCOM_BIG_GIFT_SINGLE+1; //大礼物 连发

    public static final int PALYBACK_CUSTOM = AVIMCOM_BIG_GIFT_MULTI+1; //点播普通消息
    public static final int PALYBACK_ENTERROOM = PALYBACK_CUSTOM+1; //点播进入房间
    public static final int PALYBACK_EXITROOM = PALYBACK_ENTERROOM+1; //点播退出房间
    public static final int PALYBACK_DESTROYROOM = PALYBACK_EXITROOM+1; //点播房间解散







    public static final int AVIMCMD_MULTI = 0x800;             // 多人互动消息类型

    public static final int AVIMCMD_MUlTI_HOST_INVITE = AVIMCMD_MULTI + 1;         // 多人主播发送邀请消息, C2C消息
    public static final int AVIMCMD_MULTI_CANCEL_INTERACT = AVIMCMD_MUlTI_HOST_INVITE + 1;       // 断开互动，Group消息，带断开者的imUsreid参数
    public static final int AVIMCMD_MUlTI_JOIN = AVIMCMD_MULTI_CANCEL_INTERACT + 1;       // 多人互动方收到AVIMCMD_Multi_Host_Invite多人邀请后，同意，C2C消息
    public static final int AVIMCMD_MUlTI_REFUSE = AVIMCMD_MUlTI_JOIN + 1;      // 多人互动方收到AVIMCMD_Multi_Invite多人邀请后，拒绝，C2C消息

    public static final int AVIMCMD_Multi_Host_EnableInteractMic = AVIMCMD_MUlTI_REFUSE + 1;  // 主播打开互动者Mic，C2C消息
    public static final int AVIMCMD_Multi_Host_DisableInteractMic = AVIMCMD_Multi_Host_EnableInteractMic + 1;// 主播关闭互动者Mic，C2C消息
    public static final int AVIMCMD_Multi_Host_EnableInteractCamera = AVIMCMD_Multi_Host_DisableInteractMic + 1; // 主播打开互动者Camera，C2C消息
    public static final int AVIMCMD_Multi_Host_DisableInteractCamera = AVIMCMD_Multi_Host_EnableInteractCamera + 1; // 主播打开互动者Camera，C2C消息
    public static final int AVIMCMD_MULTI_HOST_CANCELINVITE = AVIMCMD_Multi_Host_DisableInteractCamera + 1;
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_CAMERA = AVIMCMD_MULTI_HOST_CANCELINVITE + 1;
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_MIC = AVIMCMD_MULTI_HOST_CONTROLL_CAMERA + 1;

    public static final int AVIMCMD_Text = -1;         // 普通的聊天消息

    public static final int AVIMCMD_None = AVIMCMD_Text + 1;               // 无事件

    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_EnterLive = AVIMCMD_None + 1;          // 用户加入直播, Group消息  1
    public static final int AVIMCMD_ExitLive = AVIMCMD_EnterLive + 1;         // 用户退出直播, Group消息  2
    public static final int AVIMCMD_Praise = AVIMCMD_ExitLive + 1;           // 点赞消息, Demo中使用Group消息  3
    public static final int AVIMCMD_Host_Leave = AVIMCMD_Praise + 1;         // 主播离开, Group消息 ： 4
    public static final int AVIMCMD_Host_Back = AVIMCMD_Host_Leave + 1;         // 主播回来, Demo中使用Group消息 ： 5


    public static final String CMD_KEY = "userAction";
    public static final String CMD_PARAM = "actionParam";


    private static final String TAG = "AvConstants";
    private static final String PACKAGE = "com.tencent.qcloud.suixinbo";


    public static final int AVIMCMD_MANAGER = 0x900;             // 自定义控制台事件。
    public static final int AVIMCMD_Manager_exit = AVIMCMD_MANAGER + 1;         //后台强制关闭直播和点播 。

    public volatile static String inputYuvFilePath = "/sdcard/123.yuv";
    public volatile static int yuvWide = 320;
    public volatile static int yuvHigh = 240;
    public volatile static int yuvFormat = 0;
    public volatile static String outputYuvFilePath = "/sdcard/123";

    public volatile static String audioInputName = "1.pcm";
    public volatile static String audioOutputName = "1.pcm";
    public volatile static int sampleRate = 16000;
    public volatile static int channelNum = 1;
    public volatile static long auth_bits = 0;

    public static final String ACTION_START_CONTEXT_COMPLETE = PACKAGE
            + ".ACTION_START_CONTEXT_COMPLETE";
    public static final String ACTION_CLOSE_CONTEXT_COMPLETE = PACKAGE
            + ".ACTION_CLOSE_CONTEXT_COMPLETE";
    public static final String ACTION_ROOM_CREATE_COMPLETE = PACKAGE
            + ".ACTION_ROOM_CREATE_COMPLETE";
    public static final String ACTION_CLOSE_ROOM_COMPLETE = PACKAGE
            + ".ACTION_CLOSE_ROOM_COMPLETE";
    public static final String ACTION_SURFACE_CREATED = PACKAGE
            + ".ACTION_SURFACE_CREATED";
    public static final String ACTION_MEMBER_CHANGE = PACKAGE
            + ".ACTION_MEMBER_CHANGE";
    public static final String ACTION_CHANGE_AUTHRITY = PACKAGE
            + ".ACTION_CHANGE_AUTHRITY";

    public static final String ACTION_VIDEO_SHOW = PACKAGE
            + ".ACTION_VIDEO_SHOW";
    public static final String ACTION_VIDEO_CLOSE = PACKAGE
            + ".ACTION_VIDEO_CLOSE";
    public static final String ACTION_ENABLE_CAMERA_COMPLETE = PACKAGE
            + ".ACTION_ENABLE_CAMERA_COMPLETE";
    public static final String ACTION_SWITCH_CAMERA_COMPLETE = PACKAGE
            + ".ACTION_SWITCH_CAMERA_COMPLETE";
    public static final String ACTION_OUTPUT_MODE_CHANGE = PACKAGE
            + ".ACTION_OUTPUT_MODE_CHANGE";
    public static final String ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE = PACKAGE
            + ".ACTION_ENABLE_EXTERNAL_CAPTURE_COMPLETE";

    public static final String ACTION_HOST_ENTER = PACKAGE
            + ".ACTION_HOST_ENTER";

    public static final String ACTION_SWITCH_VIDEO = PACKAGE
            + ".ACTION_SWITCH_VIDEO";

    public static final String ACTION_HOST_LEAVE = PACKAGE
            + ".ACTION_HOST_LEAVE";


    public static final String ACTION_CAMERA_OPEN_IN_LIVE = PACKAGE
            + ".ACTION_CAMERA_OPEN_IN_LIVE";

    public static final String ACTION_CAMERA_CLOSE_IN_LIVE = PACKAGE
            + ".ACTION_CAMERA_CLOSE_IN_LIVE";

    private static final boolean LOCAL = true;
    private static final boolean REMOTE = false;

    public static final String EXTRA_RELATION_ID = "relationId";
    public static final String EXTRA_AV_ERROR_RESULT = "av_error_result";
    public static final String EXTRA_VIDEO_SRC_TYPE = "videoSrcType";
    public static final String EXTRA_IS_ENABLE = "isEnable";
    public static final String EXTRA_IS_FRONT = "isFront";
    public static final String EXTRA_IDENTIFIER = "identifier";
    public static final String EXTRA_SELF_IDENTIFIER = "selfIdentifier";
    public static final String EXTRA_ROOM_ID = "roomId";
    public static final String EXTRA_IS_VIDEO = "isVideo";
    public static final String EXTRA_IDENTIFIER_LIST_INDEX = "QQIdentifier";

    public static final String EXTRA_AUDIO_PENETRATE_VALUE = "audio_penetrate_value";


    public static final String ACTION_AUDIO_DATA_PENETRATE_MIX_SEND = PACKAGE
            + ".ACTION_AUDIO_DATA_PENETRATE_MIX_SEND";
    public static final String ACTION_AUDIO_DATA_PENETRATE_SPEAKER_SEND = PACKAGE
            + ".ACTION_AUDIO_DATA_PENETRATE_SPEAKER_SEND";
    public static final String ACTION_AUDIO_DATA_PENETRATE_MIC = PACKAGE
            + ".ACTION_AUDIO_DATA_PENETRATE_MIC";
    public static final String ACTION_AUDIO_DATA_PENETRATE_SEND = PACKAGE
            + ".ACTION_AUDIO_DATA_PENETRATE_SEND";
    public static final String ACTION_AUDIO_DATA_PENETRATE_PLAY = PACKAGE
            + ".ACTION_AUDIO_DATA_PENETRATE_PLAY";
    public static final String ACTION_AUDIO_DATA_PENETRATE_NETSTREAM = PACKAGE
            + ".ACTION_AUDIO_DATA_PENETRATE_NETSTREAM";


    public static final int INDEX_OUTPUT_MIC = 0;
    public static final int INDEX_INPUT_MIX_SEND = 1;
    public static final int INDEX_OUTPUT_SEND = 2;
    public static final int INDEX_INPUT_SPEAKER_SEND = 3;
    public static final int INDEX_OUTPUT_PLAY = 4;
    public static final int INDEX_OUTPUT_NETSTREAM = 5;

    public static final int DEMO_ERROR_BASE = -99999999;

    public static final int AUDIO_VOICE_CHAT_MODE = 0;
    /**
     * 空指针
     */
    public static final int DEMO_ERROR_NULL_POINTER = DEMO_ERROR_BASE + 1;

    public static final int AUTO_EXIT_ROOM = 101;



    public static final long HOST_AUTH = AVRoomMulti.AUTH_BITS_DEFAULT;//权限位；TODO：默认值是拥有所有权限。
    public static final long VIDEO_MEMBER_AUTH = AVRoomMulti.AUTH_BITS_DEFAULT;//权限位；TODO：默认值是拥有所有权限。
    public static final long NORMAL_MEMBER_AUTH = AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO;


    // public static final long NORMAL_MEMBER_AUTH = AVRoom.AUTH_BITS_JOIN_ROOM | AVRoom.AUTH_BITS_RECV_AUDIO;
    public static final String HOST_ROLE = "Host";
    public static final String VIDEO_MEMBER_ROLE = "VideoMember";
    public static final String NORMAL_MEMBER_ROLE = "NormalMember";
    /**
     * red_packet_id   红包场次

     */
    public static final  String  RED_PACKET_ID="red_packet_id";
    public static final  String  DANMU_ID = "danmu_id";
    public static final  String  DANMU_MESSAGE = "message";
    public static final  String  DANMU_USER_ID = "userId";
    public static final  String  DANMU_USER_USER_NAME = "userName";
    public static final  String  DANMU_USER_AVATAR_URL = "avatarUrl";
    public static final  String  DANMU_MONEY = "userdiamond";
    /**
     *  red_packet_duration  红包间隔时间
     */
    public static final  String  RED_PACKET_DURATION="red_packet_duration";

    public static final String GIFT_DANMU_ID = "1000";

}
