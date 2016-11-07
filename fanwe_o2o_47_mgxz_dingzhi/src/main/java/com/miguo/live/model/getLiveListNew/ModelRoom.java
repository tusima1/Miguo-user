package com.miguo.live.model.getLiveListNew;

import com.fanwe.base.Body;

import java.util.List;

/**
 * 房间信息
 * Created by Administrator on 2016/7/27.
 */
public class ModelRoom extends Body {
    // 直播房间的创建时间
    private String create_time;
    private String play_url;
    // 是否开通直播回访 1开通0关闭
    private String playback_status;
    // 直播类型 1直播(直播回放)2点播
    private String live_type;
    //直播回放的房间号
    private String playback_room_id;
    private String title;
    //直播状态 0:未开始，1:直播中，2:已结束
    private String start_status;
    private String cover;
    private String start_time;
    private String chat_room_id;
    /**
     * uid : 200a3e14-3046-4b76-bc44-2208f40473db
     * host_user_id : 200a3e14-3046-4b76-bc44-2208f40473db
     * nickname : 18888888888
     * avatar :
     * tags : ["女神","呆萌","小富婆","霸王餐挑战"]
     */

    private ModelHost host;
    private String id;
    private String av_room_id;
    /**
     * geo_y : 70
     * geo_x : 130
     * shop_id : 4cb975c9-bf4c-4a23-95b1-9b7f3cc1c4b2
     * address : 浦沿
     * shop_name : 爆米花
     */

    private ModelLbs lbs;
    private String channel_id;
    /**
     * duration : 5396
     * file_id : 14651978969406208719
     * playset : [{"vbitrate":0,"definition":0,"vheight":0,"vwidth":0,"url":"http://200023194.vod.myqcloud.com/200023194_76cb4e51d43d4d0ba5d908089f35e115.f0.mp4"}]
     */

    private List<ModelRecordFile> fileset;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getPlayback_status() {
        return playback_status;
    }

    public void setPlayback_status(String playback_status) {
        this.playback_status = playback_status;
    }

    public String getLive_type() {
        return live_type;
    }

    public void setLive_type(String live_type) {
        this.live_type = live_type;
    }

    public String getPlayback_room_id() {
        return playback_room_id;
    }

    public void setPlayback_room_id(String playback_room_id) {
        this.playback_room_id = playback_room_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_status() {
        return start_status;
    }

    public void setStart_status(String start_status) {
        this.start_status = start_status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public ModelHost getHost() {
        return host;
    }

    public void setHost(ModelHost host) {
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAv_room_id() {
        return av_room_id;
    }

    public void setAv_room_id(String av_room_id) {
        this.av_room_id = av_room_id;
    }

    public ModelLbs getLbs() {
        return lbs;
    }

    public void setLbs(ModelLbs lbs) {
        this.lbs = lbs;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public List<ModelRecordFile> getFileset() {
        return fileset;
    }

    public void setFileset(List<ModelRecordFile> fileset) {
        this.fileset = fileset;
    }
}
