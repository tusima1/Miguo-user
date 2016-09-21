package com.fanwe.home.model;

import com.fanwe.base.Body;

/**
 * 房间信息
 * Created by Administrator on 2016/7/27.
 */
public class Room extends Body {
//    "page_total": "1",
//            "page": "1",
//            "body": [{
//        "cover": "http://pic1.mofang.com.tw/2014/0516/20140516051344912.jpg",        直播封面URL
//        "create_time": "1471438410369",                                                创建时间
//        "chat_room_id": "714384103",                                                    聊天室ID

//        "av_room_id": "714384103",
//                "id": "714384103",                                                              直播房间号 就是room_id
//        "title": "米果小站",

//        ----------------------------------------20160910 增加点播需要的数据--------------------------------------------------------------
//        "live_type": "2",			                                                直播类型  1 表示直播，2表示点播
//        "file_size": "620925",                                                           点播的文件大小
//        "duration": "3",                                                                 点播时长
//        "vid": "200023194_657c1001a5c8426aae62370fc5750c34",                             点播vid
//        "file_id": "14651978969262532999",                                               点播文件id
//        "playset": "[{\"url\":\"http:\\/\\/200023194.vod.myqcloud.com\\/200023194_657c1001a5c8426aae62370fc5750c34.f0.mp4\",\"definition\":0,\"vbitrate\":0,\"vheight\":0,\"vwidth\":0}]",
//         视频播放信息{url：播放地址，definition：格式， 0: ["", "原始"], 1: ["带水印", "原始"]，vbitrate：码率，单位：kbps，vheight：高度，单位：px，vwidth：宽度，单位：px}
//        ------------------------------------------------------------------------------------------------------------------------------------

    private String live_type;
    private String file_size;
    private String duration;
    private String vid;
    private String file_id;
    private String playset;
    //--------------------2016.09.21

    public String getLive_type() {
        return live_type;
    }

    public void setLive_type(String live_type) {
        this.live_type = live_type;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getPlayset() {
        return playset;
    }

    public void setPlayset(String playset) {
        this.playset = playset;
    }

    private String cover;

    private String create_time;

    private String chat_room_id;

    private Host host;

    private String av_room_id;
    private String inner_message;

    private String id;

    private String title;

    private Lbs lbs;

    public String getInner_message() {
        return inner_message;
    }

    public void setInner_message(String inner_message) {
        this.inner_message = inner_message;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getAv_room_id() {
        return av_room_id;
    }

    public void setAv_room_id(String av_room_id) {
        this.av_room_id = av_room_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Lbs getLbs() {
        return lbs;
    }

    public void setLbs(Lbs lbs) {
        this.lbs = lbs;
    }
}
